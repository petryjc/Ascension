
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;


public class Deck {
	///////////////Used to draw cards within Deck Location////////////
	public static final float cardDimensionRatio = 2; //height divided by width
	int yBorder = 4;  // Space between cards and top/bottom
	int xBorder = 4;  // Space between cards and edges
	/////////////////////////////////////////////////////////////////
	
	ArrayList<Card> notPlayed;
	ArrayList<Card> hand;
	ArrayList<Card> discard;
	ArrayList<Card> constructs;
	Random generator = new Random();
	Rectangle handLocation;
	DeckRender deckRend;
	
	
	public Deck() {
		this(new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), null);
	}
	
	public Deck(Rectangle location) {
		this(new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), location);
	}

	public Deck(ArrayList<Card> notPlayed, Rectangle location) {
		this(notPlayed, new ArrayList<Card>(), new ArrayList<Card>(), new ArrayList<Card>(), location);
	}
	
	public Deck(ArrayList<Card> notPlayed, ArrayList<Card> hand, ArrayList<Card> discard, ArrayList<Card> constructs, Rectangle location) {
		this.notPlayed = notPlayed;
		this.hand = hand;
		this.discard = discard;
		this.handLocation = location;
		this.constructs = constructs;
		this.deckRend = new DeckRender(this);
		this.deckRend.resetHandLocation();
	}

	public boolean drawCard() {
		//if the not played deck is gone, replace with discard deck
		if(notPlayed.size() == 0) {
			//if discard deck is also gone, you cannot draw anymore
			if(discard.size() == 0) {
				return false;
			}
			notPlayed.addAll(discard);
			discard.clear();
			shuffle();
		}
		//remove from not played and add to hand
		Card c = notPlayed.remove(0);
		hand.add(c);
		this.deckRend.resetHandLocation();
		return true;
	}
	
	public void shuffle() {
		//generate a random index and draw that card.  replaces shuffling
		ArrayList<Card> temp = new ArrayList<Card>();
		while(notPlayed.size() > 0) {
			int i = generator.nextInt(notPlayed.size());
			temp.add(notPlayed.remove(i));
		}
		notPlayed.addAll(temp);
	}
	
	public void addNewCardToDiscard(Card c) {
		discard.add(c);
	}
	
	public boolean drawNCards(int n) {
		for(int i = 0; i < n; i++) {
			if(!drawCard()) {
				System.out.println("Here");
				return false;
			}
		}
		return true;
	}
	
	public Card handleClick(Point p) {
		if(handLocation.contains(p)) {
			for(Card c : hand) {
				if(c.onCard(p)) {
					return playCard(c);
				}
			}
		}
		return null;
	}
	
	public Card playCard(Card c) {
		if(!hand.remove(c)) {
			throw new IllegalArgumentException();
		}
		drawCard();
		return c;
	}
}
