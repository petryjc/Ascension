import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Turn {

	Player player;
	int rune;
	int power;
	Game game;
	TurnState turnState;
	int turnStateMagnitude;
	
	public Turn(Player player, Game g){
		this.player = player;
		this.rune = 0;
		this.power = 0;
		this.game = g;
		this.turnState = TurnState.Default;
		for (Card c : this.player.playerDeck.constructs) {
			executeCardAction(c);
		}
	}
	
	public void leftButtonClick(Point loc) {
		
		switch (turnState) {
		case Default: 
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
			break;
		case Discard:
			if(this.player.playerDeck.attemptDiscard(loc)) {
				this.turnStateMagnitude--;
				if(this.turnStateMagnitude < 1) {
					this.turnState = TurnState.Default;
				}
			}
			break;
		default:
			break;
		}
		
	}
	
	public void executeCardAction(Card c) {
		for(Action a: c.getActions()) {
			switch (a.action) {
			case HonorBoost:
				this.player.inncrementHonor(a.magnitude);
				this.game.decrementHonor(a.magnitude);
				break;
			case PowerBoost:
				this.power += a.magnitude;
				break;
			case RuneBoost:
				this.rune += a.magnitude;
				break;
			case DrawCard:
				player.playerDeck.drawNCards(a.magnitude);
				break;
			case Discard:
				this.turnState = TurnState.Discard;
				this.turnStateMagnitude = a.magnitude;
				break;
			default:
				break;
			}
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
	
	public enum TurnState {
		Default, Discard
	}
}
