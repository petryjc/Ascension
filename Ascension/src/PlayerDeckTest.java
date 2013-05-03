import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class PlayerDeckTest extends DeckTest{


	ArrayList<Card> playedOrigional;
	Rectangle playedLocation;
	ArrayList<Card> constructOrigional;
	Rectangle constructLocation;
	PlayerDeck d;
	
	@Before
	public void makeDeck() {
		ArrayList<Card> notPlayed = randomCardList();
		notPlayedOrigional = new ArrayList<Card>();
		notPlayedOrigional.addAll(notPlayed);
		
		ArrayList<Card> hand = randomCardList();
		handOrigional = new ArrayList<Card>();
		handOrigional.addAll(hand);
		
		ArrayList<Card> discard = randomCardList();
		discardOrigional = new ArrayList<Card>();
		discardOrigional.addAll(discard);
		
		ArrayList<Card> played = randomCardList();
		playedOrigional = new ArrayList<Card>();
		playedOrigional.addAll(played);
		
		ArrayList<Card> construct = randomCardList();
		constructOrigional = new ArrayList<Card>();
		constructOrigional.addAll(construct);
		
		handLocation = new Rectangle(0,500,1000,300);
		playedLocation = new Rectangle(0,200,1000,300);
		constructLocation = new Rectangle(0,0,1000,300);
		d = new PlayerDeck(notPlayed, hand, discard, played, construct, handLocation, playedLocation, constructLocation);
		super.d = d;
	}
	
	@Test
	public void testInitialization() {
		PlayerDeck pd = new PlayerDeck();
		assertNotNull(pd);
	}
	
	@Test
	public void testDeaultConstructor() {
		PlayerDeck d = new PlayerDeck();
		assertEquals(new ArrayList<Card>(), d.notPlayed);
		assertEquals(new ArrayList<Card>(), d.hand);
		assertEquals(new ArrayList<Card>(), d.discard);
	}
	
	@Test
	public void testConstructorWNotPlayed() {
		ArrayList<Card> notPlayed = randomCardList();
		PlayerDeck d = new PlayerDeck(notPlayed, handLocation, playedLocation, constructLocation);
		assertEquals(notPlayed, d.notPlayed);
		assertEquals(new ArrayList<Card>(), d.hand);
		assertEquals(new ArrayList<Card>(), d.discard);
	}
	
	@Test
	public void testConstructorW3() {
		ArrayList<Card> notPlayed = randomCardList();
		ArrayList<Card> hand = randomCardList();
		ArrayList<Card> discard = randomCardList();
		PlayerDeck d = new PlayerDeck(notPlayed, hand, discard, handLocation, playedLocation, constructLocation);
		assertEquals(notPlayed, d.notPlayed);
		assertEquals(hand, d.hand);
		assertEquals(discard, d.discard);
	}
	
	@Test
	public void testConstructorWAll() {
		ArrayList<Card> notPlayed = randomCardList();
		ArrayList<Card> hand = randomCardList();
		ArrayList<Card> discard = randomCardList();
		ArrayList<Card> played = randomCardList();
		ArrayList<Card> constructs = randomCardList();
		PlayerDeck d = new PlayerDeck(notPlayed, hand, discard, played, constructs, handLocation, playedLocation, constructLocation);
		assertEquals(notPlayed, d.notPlayed);
		assertEquals(hand, d.hand);
		assertEquals(discard, d.discard);
		assertEquals(played, d.played);
		assertEquals(constructs, d.constructs);
	}
	
	@Test
	public void testPlayCardFirst() {
		d.playCard(d.hand.get(0));
		
		assertTrue(d.played.containsAll(playedOrigional) && d.constructs.containsAll(constructOrigional));
		assertFalse(playedOrigional.containsAll(d.played) && constructOrigional.containsAll(d.constructs));
		
		assertFalse(d.hand.containsAll(handOrigional));
		assertTrue(handOrigional.containsAll(d.hand));
		
		assertEquals(playedOrigional.size() + 1 + constructOrigional.size(), d.played.size() + d.constructs.size());
		assertEquals(handOrigional.size() - 1, d.hand.size());
	}
	
	@Test
	public void testPlayCardLast() {
		d.playCard(d.hand.get(d.hand.size() - 1));
		
		assertTrue(d.played.containsAll(playedOrigional) && d.constructs.containsAll(constructOrigional));
		assertFalse(playedOrigional.containsAll(d.played) && constructOrigional.containsAll(d.constructs));
		
		assertFalse(d.hand.containsAll(handOrigional));
		assertTrue(handOrigional.containsAll(d.hand));
		
		assertEquals(playedOrigional.size() + 1 + constructOrigional.size(), d.played.size() + d.constructs.size());
		assertEquals(handOrigional.size() - 1, d.hand.size());
	}
	
	@Test
	public void testPlayCardMiddle() {
		d.playCard(d.hand.get((d.hand.size() - 1)/2));
		
		assertTrue(d.played.containsAll(playedOrigional) && d.constructs.containsAll(constructOrigional));
		assertFalse(playedOrigional.containsAll(d.played) && constructOrigional.containsAll(d.constructs));
		
		assertFalse(d.hand.containsAll(handOrigional));
		assertTrue(handOrigional.containsAll(d.hand));
		
		assertEquals(playedOrigional.size() + 1 + constructOrigional.size(), d.played.size() + d.constructs.size());
		assertEquals(handOrigional.size() - 1, d.hand.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPlayCardFailure() {
		d.playCard(new Card());
	}
	
	@Test
	public void testClick() {
		d.handleClick(new Point(150, 150));
	}
	
	@Test
	public void testEndTurn(){
		PlayerDeck pd = new PlayerDeck();
		for(int i = 0; i < 6; i++){
			Card c = new Card();
			Card c2 = new Card();
			pd.hand.add(c);
			pd.played.add(c2);
		}
		assertEquals(pd.hand.size(), 6);
		assertEquals(pd.played.size(),6);
		assertEquals(pd.discard.size(), 0);
		pd.endTurn();
		assertEquals(pd.hand.size(), 0);
		assertEquals(pd.played.size(),0);
		assertEquals(pd.discard.size(), 12);
		
	}
	

	@Test
	public void testAttemptDiscard() {
		assertNotNull(d.attemptDiscard(new Point(handLocation.x + handLocation.width
						/ d.hand.size() / 2 - 10, handLocation.y
						+ d.handLocation.height / 4)));
		// check that it discarded something
		assertEquals(handOrigional.size() - 1, d.hand.size());
		assertEquals(discardOrigional.size() + 1, d.discard.size());
	}
	

	@Test
	public void testHandleClickLast() {
		assertNotNull(d.attemptDiscard(new Point(handLocation.x + handLocation.width
				- handLocation.width / d.hand.size() / 2 + 10,
				handLocation.y + d.handLocation.height / 4)));
		// check that it discarded something
		assertEquals(handOrigional.size() - 1, d.hand.size());
		assertEquals(discardOrigional.size() + 1, d.discard.size());
	}

	@Test
	public void testHandleClickNothing() {
		assertNull(d.attemptDiscard(new Point(handLocation.x, handLocation.y)));
		// check that it discarded nothing
		assertEquals(handOrigional.size(), d.hand.size());
		assertEquals(discardOrigional.size(), d.discard.size());

	}
	
	@Test
	public void testAttemptDeckBanish() {
		assertTrue(d.attemptDeckBanish(new Point(handLocation.x + handLocation.width
						/ d.hand.size() / 2 - 10, handLocation.y
						+ d.handLocation.height / 4)) != null);
		// Check that it banished something.
		assertEquals(handOrigional.size() - 1, d.hand.size());
		// Check that no other fields were altered.
		assertEquals(discardOrigional.size(), d.discard.size());
		assertEquals(notPlayedOrigional.size(), d.notPlayed.size());
		assertEquals(constructOrigional.size(), d.constructs.size());
		assertEquals(playedOrigional.size(), d.played.size());
	}
	
	@Test
	public void testAttemptDeckBanishLast() {
		assertTrue(d.attemptDeckBanish(new Point(handLocation.x + handLocation.width
				- handLocation.width / d.hand.size() / 2 + 10,
				handLocation.y + d.handLocation.height / 4)) != null);
		// Check that it banished something.
		assertEquals(handOrigional.size() - 1, d.hand.size());
		// Check that no other fields were altered.
		assertEquals(discardOrigional.size(), d.discard.size());
		assertEquals(notPlayedOrigional.size(), d.notPlayed.size());
		assertEquals(constructOrigional.size(), d.constructs.size());
		assertEquals(playedOrigional.size(), d.played.size());
	}
	
	@Test
	public void testAttemptDeckBanishOnNothing() {
		assertTrue(d.attemptDeckBanish(new Point(handLocation.x, handLocation.y)) == null);
		// Check that it discarded nothing.
		assertEquals(handOrigional.size(), d.hand.size());
		assertEquals(discardOrigional.size(), d.discard.size());
		assertEquals(notPlayedOrigional.size(), d.notPlayed.size());
		assertEquals(constructOrigional.size(), d.constructs.size());
		assertEquals(playedOrigional.size(), d.played.size());

	}
}
