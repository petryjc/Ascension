


public class Turn {

	Player player;
	Deck gameDeck;
	int rune;
	int power;
	
	public Turn(Player player, Deck gameDeck){
		this.player = player;
		this.player.startingHand();
		this.gameDeck = gameDeck;
	}
	
	
	
}
