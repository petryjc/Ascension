import java.util.ArrayList;


public class Player {
	
	public PlayerDeck playerDeck;
	public int honorTotal;
	public String name;
	public boolean corrosiveWidow;
	public boolean seaTyrant;
	
	public Player(){
		
	}
	
	public Player(PlayerDeck deck, String name){
		this.playerDeck = deck;
		this.honorTotal = 0;
		this.name = name;
		this.seaTyrant = false;
		this.corrosiveWidow = false;
	}
	
	public void incrementHonor(int incre){
		this.honorTotal += incre;
	}
	
	public void startingHand(){
		if (this.playerDeck.hand.size() == 0) {
			this.playerDeck.drawNCards(5);
		}
	}
	
	public static Player getNewPlayer(String name) {

		ArrayList<Card> notPlayed2 = new ArrayList<Card>();
		
		ArrayList<Action> action1 = new ArrayList<Action>();
		action1.add(new Action(1, Action.ActionType.RuneBoost));
		for(int i = 0; i < 8; i++) {
			notPlayed2.add(new Card(Card.Type.Hero, Card.Faction.Common,0,action1, "Apprentice"));
		}
		
		ArrayList<Action> action2 = new ArrayList<Action>();
		action2.add(new Action(1, Action.ActionType.PowerBoost));
		for(int i = 0; i < 2; i++) {
			notPlayed2.add(new Card(Card.Type.Hero, Card.Faction.Common,0,action2, "Militia"));
		}
		
		ArrayList<Card> hand2 =  new ArrayList<Card>();
		ArrayList<Card> discard2 =  new ArrayList<Card>();
		
		PlayerDeck pD = new PlayerDeck(notPlayed2, hand2, discard2, Game.handLoc, Game.playedLoc, Game.constructs);
		pD.shuffle();
		
		return new Player(pD, name);
	}

	public void flipTyrantConstructsBool() {
		this.seaTyrant=!this.seaTyrant;
		
	}
	public void flipWidowConstructsBool(){
		this.corrosiveWidow = !this.corrosiveWidow;
	}

}
