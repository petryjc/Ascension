import java.util.ArrayList;


public class PlayerDeck extends Deck {

	ArrayList<Card> _played;
	ArrayList<Card> _constructs;
	
	public PlayerDeck() {
		super();
	}

	public PlayerDeck(ArrayList<Card> notPlayed) {
		super(notPlayed);
	}
	
	public PlayerDeck(ArrayList<Card> notPlayed, ArrayList<Card> hand, ArrayList<Card> discard) {
		super(notPlayed, hand, discard);
	}
	
	public PlayerDeck(ArrayList<Card> notPlayed, ArrayList<Card> hand, ArrayList<Card> discard, ArrayList<Card> played, ArrayList<Card> constructs) {
		super(notPlayed, hand, discard);
		_played = played;
		_constructs = constructs;
	}

	public void PlayCard(Card c) {
		if(!super._hand.remove(c)) {
			throw new IllegalArgumentException();
		}
		//TODO add to played or construct depending upon card type
		_played.add(c);
	}
}
