import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Turn {

	Player player;
	int rune;
	int power;
	Game game;
	
	public Turn(Player player, Game g){
		this.player = player;
		this.rune = 0;
		this.power = 0;
		this.game = g;
		for (Card c : this.player.playerDeck.constructs) {
			executeCardAction(c);
		}
	}
	
	public void leftButtonClick(Point loc) {
		Rectangle end = new Rectangle(1460,492, 91, 91);
		Rectangle playAllCardsInHand = new Rectangle(47,493,83,100);
		if(end.contains(loc)){
			this.player.playerDeck.endTurn();
			this.game.nextTurn();
			return;
		}
		if(playAllCardsInHand.contains(loc)){
			while(!this.player.playerDeck.hand.isEmpty()) {
				Card c = this.player.playerDeck.hand.get(0);
				this.executeCardAction(c);
				this.player.playerDeck.playCard(c);
			}
			
		}
		Card c = this.player.playerDeck.handleClick(loc);
		if(c != null) {
			executeCardAction(c);
			return;
		}
		if(staticCardList(this.game.gameDeck.constructs, loc)) {
			return;
		}
		if(staticCardList(this.game.gameDeck.hand, loc)) {
			return;
		}
	}
	
	public void executeCardAction(Card c) {
		for(Action a: c.getActions()) {
			if(a.action == Action.ActionType.HonorBoost) {
				this.player.inncrementHonor(a.magnitude);
			} else if(a.action == Action.ActionType.PowerBoost) {
				this.power += a.magnitude;
			} else if(a.action == Action.ActionType.RuneBoost) {
				this.rune += a.magnitude;
			} else if(a.action == Action.ActionType.DrawCard)
				player.playerDeck.drawNCards(a.magnitude);
		}
	}
	
	public boolean staticCardList(ArrayList<Card> s, Point p) {
		for(Card c : s) {
			if(c.getLocation().contains(p)) {
				if(c.getType() == Card.Type.Monster) {
					if(c.getCost() <= this.power) {
						this.power -= c.getCost();
						executeCardAction(c);
						this.game.gameDeck.hand.remove(c);
						if(!c.getName().equals("Cultist")) {
						this.game.gameDeck.discard.add(c);
						this.game.gameDeck.drawCard();
						}
					}
				} else {
					if(c.getCost() <= this.rune) {
						this.rune -= c.getCost();
						//Todo copy card
						this.player.playerDeck.addNewCardToDiscard(new Card(c));
						if (this.game.gameDeck.hand.contains(c)) {
						this.game.gameDeck.hand.remove(c);
						this.game.gameDeck.drawCard();
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
}
