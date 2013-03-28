import static org.junit.Assert.*;

import java.util.ArrayList;


import org.junit.Test;


public class GameTest {

	@Test
	public void testInitialization() {
		Player player1 = new Player(null, null);
		
		ArrayList<Player> players = new ArrayList<Player>();
		
		players.add(player1);
		
		
	}
	
	@Test
	public void testGetCenterDeck() {
		Game g = new Game(10);
		g.gameDeck = new Deck();
		g.gameDeck.notPlayed = Game.getCenterDeck("src/testDeck.txt");
		ArrayList<Action> a = new ArrayList<Action>();
		a.add(new Action(1,Action.ActionType.RuneBoost));
		ArrayList<Card> c = new ArrayList<Card>();
		c.add(new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, a, "TestCard"));
		assertEquals(c.get(0).getName(), g.gameDeck.notPlayed.get(0).getName());
		assertEquals(c.get(0).getType(), g.gameDeck.notPlayed.get(0).getType());
		assertEquals(c.get(0).getFaction(), g.gameDeck.notPlayed.get(0).getFaction());
		assertEquals(c.get(0).getCost(), g.gameDeck.notPlayed.get(0).getCost());
		assertEquals(c.get(0).getActions().get(0).magnitude, g.gameDeck.notPlayed.get(0).getActions().get(0).magnitude);
		assertEquals(c.get(0).getActions().get(0).action, g.gameDeck.notPlayed.get(0).getActions().get(0).action);
	}

}
