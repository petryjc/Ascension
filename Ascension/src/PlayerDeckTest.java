import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;


public class PlayerDeckTest extends DeckTest{

	ArrayList<Card> playedOrigional;
	ArrayList<Card> constructOrigional;
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
		
		d = new PlayerDeck(notPlayed, hand, discard, played, construct);
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
		assertEquals(new ArrayList<Card>(), d._notPlayed);
		assertEquals(new ArrayList<Card>(), d._hand);
		assertEquals(new ArrayList<Card>(), d._discard);
	}
	
	@Test
	public void testConstructorWNotPlayed() {
		ArrayList<Card> notPlayed = randomCardList();
		PlayerDeck d = new PlayerDeck(notPlayed);
		assertEquals(notPlayed, d._notPlayed);
		assertEquals(new ArrayList<Card>(), d._hand);
		assertEquals(new ArrayList<Card>(), d._discard);
	}
	
	@Test
	public void testConstructorW3() {
		ArrayList<Card> notPlayed = randomCardList();
		ArrayList<Card> hand = randomCardList();
		ArrayList<Card> discard = randomCardList();
		PlayerDeck d = new PlayerDeck(notPlayed, hand, discard);
		assertEquals(notPlayed, d._notPlayed);
		assertEquals(hand, d._hand);
		assertEquals(discard, d._discard);
	}
	
	@Test
	public void testConstructorWAll() {
		ArrayList<Card> notPlayed = randomCardList();
		ArrayList<Card> hand = randomCardList();
		ArrayList<Card> discard = randomCardList();
		ArrayList<Card> played = randomCardList();
		ArrayList<Card> constructs = randomCardList();
		PlayerDeck d = new PlayerDeck(notPlayed, hand, discard, played, constructs);
		assertEquals(notPlayed, d._notPlayed);
		assertEquals(hand, d._hand);
		assertEquals(discard, d._discard);
		assertEquals(played, d._played);
		assertEquals(constructs, d._constructs);
	}
	
	@Test
	public void testPlayCardFirst() {
		d.playCard(d._hand.get(0));
		
		assertTrue(d._played.containsAll(playedOrigional));
		assertFalse(playedOrigional.containsAll(d._played));
		
		assertFalse(d._hand.containsAll(handOrigional));
		assertTrue(handOrigional.containsAll(d._hand));
		
		assertEquals(playedOrigional.size() + 1, d._played.size());
		assertEquals(handOrigional.size() - 1, d._hand.size());
	}
	
	@Test
	public void testPlayCardLast() {
		d.playCard(d._hand.get(d._hand.size() - 1));
		
		assertTrue(d._played.containsAll(playedOrigional));
		assertFalse(playedOrigional.containsAll(d._played));
		
		assertFalse(d._hand.containsAll(handOrigional));
		assertTrue(handOrigional.containsAll(d._hand));
		
		assertEquals(playedOrigional.size() + 1, d._played.size());
		assertEquals(handOrigional.size() - 1, d._hand.size());
	}
	
	@Test
	public void testPlayCardMiddle() {
		d.playCard(d._hand.get((d._hand.size() - 1)/2));
		
		assertTrue(d._played.containsAll(playedOrigional));
		assertFalse(playedOrigional.containsAll(d._played));
		
		assertFalse(d._hand.containsAll(handOrigional));
		assertTrue(handOrigional.containsAll(d._hand));
		
		assertEquals(playedOrigional.size() + 1, d._played.size());
		assertEquals(handOrigional.size() - 1, d._hand.size());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testPlayCardFailure() {
		d.playCard(new Card());
	}
}
