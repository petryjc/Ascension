import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.Test;


public class DeckTest {

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
		ArrayList<Card> notPlayed = randomCardList();
		ArrayList<Card> notPlayedOrigional = new ArrayList<Card>();
		notPlayedOrigional.addAll(notPlayed);
		
		ArrayList<Card> hand = randomCardList();
		ArrayList<Card> handOrigional = new ArrayList<Card>();
		handOrigional.addAll(hand);
		
		ArrayList<Card> discard = randomCardList();
		ArrayList<Card> discardOrigional = new ArrayList<Card>();
		discardOrigional.addAll(discard);
		
		Deck d = new Deck(notPlayed, hand, discard);
		assertTrue(d.DrawCard());
		
		//make sure sizes changed appropriately
		assertEquals(notPlayedOrigional.size() - 1, notPlayed.size());
		assertEquals(handOrigional.size() + 1, hand.size());
		assertEquals(discardOrigional.size(), discard.size());
		
		//use set logic to make sure all sets are correct
		assertFalse(notPlayed.containsAll(notPlayedOrigional));
		assertTrue(notPlayedOrigional.containsAll(notPlayed));
		
		assertFalse(handOrigional.containsAll(hand));
		assertTrue(hand.containsAll(handOrigional));
		
		assertTrue(discard.containsAll(discardOrigional));
		assertTrue(discardOrigional.containsAll(discard));
	}
	
	private static ArrayList<Card> randomCardList() {
		ArrayList<Card> ret = new ArrayList<Card>();
		Random generator = new Random();
		int c = generator.nextInt(10) + 1;
		for(int i = 0; i < c; i++) {
			ret.add(new Card());
		}
		return ret;
	}

}
