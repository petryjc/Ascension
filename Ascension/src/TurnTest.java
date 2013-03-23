import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;



public class TurnTest {
	ArrayList<Player> pList;
	Game g;
	Turn t;
	
	@Before
	public void makeTurn() {

		pList = new ArrayList<Player>();
		pList.add(Player.getNewPlayer("Jack"));
		g = new Game(100,pList,new Deck());
		t = new Turn(g.players.get(0), g);	
	}

	@Test
	public void testInitialization() {
		Turn t = new Turn(null, null);	
		assertTrue(t != null);
	}

	@Test
	public void testInitializationParams() {
		assertEquals(t.player, pList.get(0));
		assertEquals(t.rune, 0);
		assertEquals(t.power, 0);
		assertEquals(t.game, g);
	}
	
	@Test
	public void testExecuteActionHonor() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.HonorBoost));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCardAction(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 1);
		
		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.HonorBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCardAction(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 3);
	}
	
	@Test
	public void testExecuteActionPower() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.PowerBoost));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCardAction(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 1);
		assertEquals(t.player.honorTotal, 0);
		
		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.PowerBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCardAction(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 3);
		assertEquals(t.player.honorTotal, 0);
	}

	@Test
	public void testExecuteActionRune() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.RuneBoost));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCardAction(c);
		assertEquals(t.rune, 1);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 0);
		
		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.RuneBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCardAction(c);
		assertEquals(t.rune, 3);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 0);
	}
}
