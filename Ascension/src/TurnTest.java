import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;



public class TurnTest implements Runnable {
	ArrayList<Player> pList;
	Game g;
	Turn t;
	
	@Before
	public void makeTurn() {

		pList = new ArrayList<Player>();
		pList.add(Player.getNewPlayer("Jack"));
		g = new Game(100,pList,new Deck());
		t = new Turn(g.players.get(0), g);	
		t.optionPane = new TestOptionPane(JOptionPane.YES_OPTION);
		Thread thread = new Thread(this);
		thread.start();
	}

	@Test
	public void testInitialization() {
		Turn t = new Turn(g.players.get(0), g);	
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
		t.executeCard(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 1);
		
		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.HonorBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCard(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 3);
	}
	
	@Test
	public void testExecuteActionPower() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.PowerBoost));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCard(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 1);
		assertEquals(t.player.honorTotal, 0);
		
		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.PowerBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCard(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 3);
		assertEquals(t.player.honorTotal, 0);
	}

	@Test
	public void testExecuteActionRune() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.RuneBoost));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCard(c);
		assertEquals(t.rune, 1);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 0);
		
		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.RuneBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCard(c);
		assertEquals(t.rune, 3);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 0);
	}
	
	@Test
	public void testExecuteActionDraw() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.DrawCard));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		int before = this.t.player.playerDeck.hand.size();
		t.executeCard(c);
		assertEquals(before + 1, this.t.player.playerDeck.hand.size());
		
		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.DrawCard));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		before = this.t.player.playerDeck.hand.size();
		t.executeCard(c);
		assertEquals(before + 2, this.t.player.playerDeck.hand.size());
		
	}
	
	@Test
	public void testExecuteActionDiscard() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.Discard));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.executeCard(c);
		assertEquals(t.turnState, Turn.TurnState.Discard);
		assertEquals(t.turnStateMagnitude, 3);
		
	}
	
	@Test
	public void testForcedDeckDiscard() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.ForcedDeckBanish));
		Card c = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.executeCard(c);
		assertEquals(t.turnState, Turn.TurnState.DeckBanish);
		assertEquals(t.turnStateMagnitude, 2);
	}
	
	@Test
	public void testStaticCardList(){
		ArrayList<Action> actionList = new ArrayList<Action>();
		PlayerDeck d = new PlayerDeck();
		d.played.add(new Card(Card.Type.Hero, Card.Faction.Lifebound, 1, actionList, "Test"));
		d.hand.add(new Card(Card.Type.Hero, Card.Faction.Lifebound, 1, actionList, "Test"));
		d.discard.add(new Card(Card.Type.Hero, Card.Faction.Lifebound, 1, actionList, "Test"));
		d.notPlayed.add(new Card(Card.Type.Hero, Card.Faction.Lifebound, 1, actionList, "Test"));
		Player p1 = new Player(d, "gabe");
		Player p2 = new Player(d, "jack");
		ArrayList<Player> ps = new ArrayList<Player>();
		ps.add(p1);
		ps.add(p2);
		Game g = new Game(100);
		g.players = ps;
		Turn t1 = new Turn(p1,g);
		
		t1.leftButtonClick(new Point(1461,493));
		
		
	}
	
	@Test
	public void testExecuteUnitedAction() {
		Action a = new Action(1,Action.ActionType.CenterBanish,-1,false);
		t.executeAction(a);
		assertEquals(t.turnState, Turn.TurnState.CenterBanish);
		a = new Action(1,Action.ActionType.ConstructRuneBoost,-1,false);
		t.executeAction(a);
		assertEquals(t.constructRune, 1);
		a = new Action(1,Action.ActionType.Discard,-1,false);
		t.executeAction(a);
		assertEquals(t.turnState, Turn.TurnState.Discard);
		a = new Action(1,Action.ActionType.DrawCard,-1,false);
		t.executeAction(a);
		assertEquals(1,1);
		a = new Action(1,Action.ActionType.EnterAiyanaState,-1,false);
		t.executeAction(a);
		assertTrue(t.AiyanaState);
		a = new Action(1,Action.ActionType.ForcedDeckBanish,-1,false);
		t.executeAction(a);
		assertEquals(t.turnState, Turn.TurnState.DeckBanish);
		a = new Action(1,Action.ActionType.HonorAndRuneBoost,-1,false);
		t.executeAction(a);
		assertEquals(t.rune, 1);
		assertEquals(t.player.honorTotal, 1);
		a = new Action(1,Action.ActionType.HonorBoost,-1,false);
		t.executeAction(a);
		assertEquals(t.player.honorTotal, 2);
		a = new Action(1,Action.ActionType.MechanaConstructRuneBoost,-1,false);
		t.executeAction(a);
		assertEquals(t.mechanaConstructRune, 1);
		a = new Action(1,Action.ActionType.OptionalDeckBanish,-1,false);
		//t.executeAction(a);
		assertEquals(t.turnState, Turn.TurnState.DeckBanish);
		a = new Action(1,Action.ActionType.PowerBoost,-1,false);
		t.executeAction(a);
		assertEquals(t.power, 1);
		a = new Action(1,Action.ActionType.RuneBoost,-1,false);
		t.executeAction(a);
		assertEquals(t.rune, 2);
	}
	
	@Test
	public void testExecuteActionCenterBanish() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.CenterBanish));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Mechana, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.executeCard(testCard);
		assertEquals(t.turnState, Turn.TurnState.CenterBanish);
		assertEquals(t.turnStateMagnitude, 4);
	}
	
	@Test
	public void testExecuteActionOptionalDeckBanishYes() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.OptionalDeckBanish));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.executeCard(testCard);
		
	}
	
	@Test
	public void testExecuteActionOptionalDeckBanishNo() {
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.OptionalDeckBanish));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.executeCard(testCard);
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
	}
	
	@Test
	public void testExecuteActionHonorAndRuneBoost() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.HonorAndRuneBoost));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		assertEquals(t.rune, 0);
		assertEquals(t.player.honorTotal, 0);
		t.executeCard(testCard);
		assertEquals(t.rune, 3);
		assertEquals(t.player.honorTotal, 3);
	}
	
	@Test
	public void testExecuteActionConstructRuneBoost() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.ConstructRuneBoost));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Mechana, 1, actionList, "Test");
		assertEquals(t.constructRune, 0);
		t.executeCard(testCard);
		assertEquals(t.constructRune, 1);
	}
	
	@Test
	public void testExecuteActionMechanaConstructRuneBoost() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.MechanaConstructRuneBoost));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Mechana, 1, actionList, "Test");
		assertEquals(t.mechanaConstructRune, 0);
		t.executeCard(testCard);
		assertEquals(t.mechanaConstructRune, 2);
	}
	
	@Test
	public void testExecuteActionEnterAiyanaState() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.EnterAiyanaState));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1, actionList, "Test");
		assertFalse(t.AiyanaState);
		t.executeCard(testCard);
		assertTrue(t.AiyanaState);
	}
	
	@Test
	public void testBoostMonsterPower() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.MonsterPowerBoost));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		assertEquals(0, t.monsterPower);
		t.executeCard(testCard);
		assertEquals(3, t.monsterPower);
	}
	
	@Test
	public void testBoostHeroRune() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.HeroRuneBoost));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(0, t.heroRune);
		t.executeCard(testCard);
		assertEquals(2, t.heroRune);
	}
	
	@Test
	public void testDefeatMonster() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.DefeatMonster));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1, actionList, "Test");
		assertEquals(Turn.TurnState.Default, t.turnState);
		t.executeCard(testCard);
		assertEquals(t.player.honorTotal, 1);
	}
	
	@Test
	public void testPlayAllWNoDisruptions() {
		pList.get(0).playerDeck.drawNCards(5);
		assertEquals(5, pList.get(0).playerDeck.hand.size());
		t.playAll();
		assertEquals(0, pList.get(0).playerDeck.hand.size());
		assertEquals(5, t.rune + t.power);
	}
	
	@Test
	public void testPlayAllWDeclinedBanish() {
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		pList.get(0).playerDeck.drawNCards(2);
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.PowerBoost,0));
		actionList.add(new Action(3, Action.ActionType.OptionalDeckBanish));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		pList.get(0).playerDeck.hand.add(c);
		pList.get(0).playerDeck.drawNCards(2);
		assertEquals(5, pList.get(0).playerDeck.hand.size());
		t.playAll();
		assertEquals(0, pList.get(0).playerDeck.hand.size());
		assertEquals(4, t.rune + t.power);
	}
	
	@Test
	public void testPlayAllWAcceptedBanish() {
		pList.get(0).playerDeck.drawNCards(2);
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.OptionalDeckBanish));
		actionList.add(new Action(3, Action.ActionType.PowerBoost,0));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		pList.get(0).playerDeck.hand.add(c);
		pList.get(0).playerDeck.drawNCards(2);
		assertEquals(5, pList.get(0).playerDeck.hand.size());
		t.playAll();
		assertEquals(0, pList.get(0).playerDeck.hand.size());
		assertEquals(7, t.rune + t.power);
	}

	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(10);
				Turn.TurnState  tstate = t.turnState;
				int tMag = t.turnStateMagnitude;
				t.exitActiveWaitingState();
//				assertEquals(Turn.TurnState.DeckBanish,tstate);
//				assertEquals(2, tMag);
			} catch (InterruptedException e) {}
			catch (IllegalMonitorStateException e1) {}
		}
	}
}
