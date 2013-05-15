import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

public class Deck {
	ArrayList<Card> notPlayed;
	ArrayList<Card> hand;
	ArrayList<Card> discard;
	ArrayList<Card> constructs;
	Random generator = new Random();
	Rectangle handLocation;
	DeckRender deckRend;

	public Deck() {
		this(new ArrayList<Card>(), new ArrayList<Card>(),
				new ArrayList<Card>(), new ArrayList<Card>(), null);
	}

	public Deck(Rectangle location) {
		this(new ArrayList<Card>(), new ArrayList<Card>(),
				new ArrayList<Card>(), new ArrayList<Card>(), location);
	}

	public Deck(ArrayList<Card> notPlayed, Rectangle location) {
		this(notPlayed, new ArrayList<Card>(), new ArrayList<Card>(),
				new ArrayList<Card>(), location);
	}

	public Deck(ArrayList<Card> notPlayed, ArrayList<Card> hand,
			ArrayList<Card> discard, ArrayList<Card> constructs,
			Rectangle location) {
		this.notPlayed = notPlayed;
		this.hand = hand;
		this.discard = discard;
		this.handLocation = location;
		this.constructs = constructs;
		this.deckRend = new DeckRender(this);
		this.deckRend.resetHandLocation();
	}

	public boolean drawCard() {
		// if the not played deck is gone, replace with discard deck
		if (notPlayed.size() == 0) {
			// if discard deck is also gone, you cannot draw anymore
			if (discard.size() == 0) {
				return false;
			}
			notPlayed.addAll(discard);
			discard.clear();
			shuffle();
		}
		// remove from not played and add to hand
		Card c = notPlayed.remove(0);
		hand.add(c);
		this.deckRend.resetHandLocation();
		return true;
	}

	public void shuffle() {
		// generate a random index and draw that card. replaces shuffling
		ArrayList<Card> temp = new ArrayList<Card>();
		while (notPlayed.size() > 0) {
			int i = generator.nextInt(notPlayed.size());
			temp.add(notPlayed.remove(i));
		}
		notPlayed.addAll(temp);
	}

	public void addNewCardToDiscard(Card c) {
		discard.add(c);
	}

	public boolean drawNCards(int n) {
		for (int i = 0; i < n; i++) {
			if (!drawCard()) {
				return false;
			}
		}
		return true;
	}

	public Card handleClick(Point p) {
		for (Card c : hand) {
			if (c.onCard(p)) {
				return playCard(c);
			}
		}
		return null;
	}

	public Card playCard(Card c) {
		if (!hand.remove(c)) {
			throw new IllegalArgumentException();
		}
		drawCard();
		return c;
	}

	public Boolean attemptCenterBanish(Point p) {
		for (Card c : hand) {
			if (c.onCard(p)) {
				hand.remove(c);
				discard.add(c);
				drawCard();
				return true;
			}
		}
		return false;
	}
	
	public Card attemptAskaraCenterBanish(Point p) {
		for (Card c : hand) {
			if (c.onCard(p)) {
				hand.remove(c);
				discard.add(c);
				drawCard();
				return c;
			}
		}
		return null;
	}

	public Card attemptDefeatMonster(Point p, int power) {
		if (handLocation.contains(p)) {
			for (Card c : hand) {
				if (c.onCard(p) && c.getType() == Card.Type.Monster
						&& c.getCost() <= power) {
					hand.remove(c);
					discard.add(c);
					drawCard();
					return c;
				}
			}
		}

		return null;
	}

	public Card attemptGetHero(Point p, int cost){
		
		if(handLocation.contains(p)) {
			for(Card c : hand) {
				if(c.onCard(p) && c.getType() == Card.Type.Hero && c.getCost() <= cost) {
					hand.remove(c);
					discard.add(c);
					drawCard();
					return c;
				}
			}
		}
		return null;
				
	}
	
	public Card attemptRajEffect(Point p, int worth){
		
		if(handLocation.contains(p)) {
			for(Card c : hand) {
				if(c.onCard(p) && c.getType() == Card.Type.Hero && c.getHonorWorth() <= worth) {
					hand.remove(c);
					drawCard();
					return c;
				}
			}
		}
		return null;
				
	}

	public Boolean canAMonsterBeDefeated(int power) {
		for (Card c : hand) {
			if (c.getCost() <= power && c.getType() == Card.Type.Monster) {
				return true;
			}
		}
		return false;
	}
	
}
