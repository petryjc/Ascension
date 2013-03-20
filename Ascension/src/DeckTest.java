import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;


public class DeckTest {

	ArrayList<Card> notPlayedOrigional;
	ArrayList<Card> handOrigional;
	ArrayList<Card> discardOrigional;
	Deck d;
	
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
		
		d = new Deck(notPlayed, hand, discard);
	}
	
	@Test
	public void testInitialization() {
		Deck d = new Deck();
		assertNotNull(d);
	}
	
	@Test
	public void testDeaultConstructor() {
		Deck d = new Deck();
		assertEquals(new ArrayList<Card>(), d._notPlayed);
		assertEquals(new ArrayList<Card>(), d._hand);
		assertEquals(new ArrayList<Card>(), d._discard);
	}
	
	@Test
	public void testConstructorWNotPlayed() {
		ArrayList<Card> notPlayed = randomCardList();
		Deck d = new Deck(notPlayed);
		assertEquals(notPlayed, d._notPlayed);
		assertEquals(new ArrayList<Card>(), d._hand);
		assertEquals(new ArrayList<Card>(), d._discard);
	}
	
	@Test
	public void testConstructorWAll() {
		ArrayList<Card> notPlayed = randomCardList();
		ArrayList<Card> hand = randomCardList();
		ArrayList<Card> discard = randomCardList();
		Deck d = new Deck(notPlayed, hand, discard);
		assertEquals(notPlayed, d._notPlayed);
		assertEquals(hand, d._hand);
		assertEquals(discard, d._discard);
	}
	
	@Test
	public void testDrawCardWEmpty() {
		Deck d = new Deck();
		assertFalse(d.DrawCard());
	}
	
	@Test
	public void testDrawCardWNormal() {
		assertTrue(d.DrawCard());
		
		assertTrue(notPlayedOrigional.containsAll(d._notPlayed));
		assertFalse(d._notPlayed.containsAll(notPlayedOrigional));
		
		assertTrue(d._hand.containsAll(handOrigional));
		assertFalse(handOrigional.containsAll(d._hand));
		
		assertEquals(notPlayedOrigional.size() - 1, d._notPlayed.size());
		assertEquals(handOrigional.size() + 1, d._hand.size());
		
		notPlayedOrigional.removeAll(d._notPlayed);
		d._hand.removeAll(handOrigional);
		assertEquals(d._hand.get(0), notPlayedOrigional.get(0));
	}
	
	@Test
	public void testShuffle() {
		d._notPlayed = new ArrayList<Card>();
		d._notPlayed.add(new Card());d._notPlayed.add(new Card());
		d._notPlayed.add(new Card());d._notPlayed.add(new Card());
		d._notPlayed.add(new Card());d._notPlayed.add(new Card());
		notPlayedOrigional = new ArrayList<Card>();
		notPlayedOrigional.addAll(d._notPlayed);
		d.generator = new Random(12345432);
		d.Shuffle();
		assertTrue(notPlayedOrigional.containsAll(d._notPlayed));
		assertTrue(d._notPlayed.containsAll(notPlayedOrigional));
		boolean pass = false;
		for(int i = 0; i < d._notPlayed.size(); i++) {
			pass |= (d._notPlayed.get(i) != notPlayedOrigional.get(i));
		}
		assertTrue(pass);
	}
	
	@Test
	public void testAddNewCardToDiscard() {
		
		Card c = new Card();
		d.AddNewCardToDiscard(c);
		assertTrue(d._discard.containsAll(discardOrigional));
		assertFalse(discardOrigional.containsAll(d._discard));
		d._discard.removeAll(discardOrigional);
		assertEquals(c, d._discard.get(0));
	}
	
	public static ArrayList<Card> randomCardList() {
		ArrayList<Card> ret = new ArrayList<Card>();
		Random generator = new Random();
		int c = generator.nextInt(2) + 1;
		for(int i = 0; i < c; i++) {
			ret.add(new Card());
		}
		return ret;
	}

}
