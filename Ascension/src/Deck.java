import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;


public class Deck {
	ArrayList<Card> _notPlayed;
	ArrayList<Card> _hand;
	ArrayList<Card> _discard;
	Random generator = new Random();
	Rectangle location;
	Image i;

	public Deck() {
		this(new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), null);
	}
	
	public Deck(Rectangle location) {
		this(new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), location);
	}

	public Deck(ArrayList<Card> notPlayed, Rectangle location) {
		this(notPlayed, new ArrayList<Card>(), new ArrayList<Card>(), location);
	}
	
	public Deck(ArrayList<Card> notPlayed, ArrayList<Card> hand, ArrayList<Card> discard, Rectangle location) {
		_notPlayed = notPlayed;
		_hand = hand;
		_discard = discard;
		this.location = location;
	}

	public boolean drawCard() {
		//if the not played deck is gone, replace with discard deck
		if(_notPlayed.size() == 0) {
			//if discard deck is also gone, you cannot draw anymore
			if(_discard.size() == 0) {
				return false;
			}
			_notPlayed.addAll(_discard);
			_discard.clear();
			shuffle();
		}
		//remove from not played and add to hand
		Card c = _notPlayed.remove(0);
		_hand.add(c);
		return true;
	}
	
	public void shuffle() {
		//generate a random index and draw that card.  replaces shuffling
		ArrayList<Card> temp = new ArrayList<Card>();
		while(_notPlayed.size() > 0) {
			int i = generator.nextInt(_notPlayed.size());
			temp.add(_notPlayed.remove(i));
		}
		_notPlayed.addAll(temp);
	}
	
	public void addNewCardToDiscard(Card c) {
		_discard.add(c);
	}
	
	public boolean drawNCards(int n) {
		for(int i = 0; i < n; i++) {
			if(!drawCard())
				return false;
		}
		return true;
	}

	public void handleClick(Point p) {
		if(location.contains(p)) {
			for(Card c : _hand) {
				//c.hand
			}
		}
	}
}
