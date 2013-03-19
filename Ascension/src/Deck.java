import java.util.ArrayList;
import java.util.Random;


public class Deck {
	ArrayList<Card> _notPlayed;
	ArrayList<Card> _hand;
	ArrayList<Card> _discard;

	public Deck() {
		this(new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>());
	}

	public Deck(ArrayList<Card> notPlayed) {
		this(notPlayed, new ArrayList<Card>(), new ArrayList<Card>());
	}
	
	public Deck(ArrayList<Card> notPlayed, ArrayList<Card> hand, ArrayList<Card> discard) {
		_notPlayed = notPlayed;
		_hand = hand;
		_discard = discard;
	}

	public boolean DrawCard() {
		//if the not played deck is gone, replace with discard deck
		if(_notPlayed.size() == 0) {
			//if discard deck is also gone, you cannot draw anymore
			if(_discard.size() == 0) {
				return false;
			}
			_notPlayed.addAll(_discard);
			_discard.clear();
		}
		//generate a random index and draw that card.  replaces shuffling
		Random generator = new Random();
		int i = generator.nextInt(_notPlayed.size());
		//remove from not played and add to hand
		Card c = _notPlayed.remove(i);
		_hand.add(c);
		return true;
	}
	
	public void AddCardToDiscard(Card c) {
		_discard.add(c);
	}
	
}
