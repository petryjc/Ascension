import java.awt.Point;
import java.awt.Rectangle;




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
	}
	
	public void leftButtonClick(Point loc) {
		Rectangle end = new Rectangle(1460,492, 91, 91);
		if(end.contains(loc)){
			this.player.playerDeck.endTurn();
			this.game.nextTurn();
		}
		else{
			Card c = this.player.playerDeck.handleClick(loc);
			if(c != null) {
				executeCardAction(c);
			}
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
			}
		}
	}
	
}
