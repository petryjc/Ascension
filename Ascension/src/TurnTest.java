import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

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
		g = new Game(100, pList, new Deck());
		g.gameDeck = new Deck();
		g.descriptions = ResourceBundle.getBundle(
				"CardDescription", new Locale("en", "EN"));
		t = new Turn(g.players.get(0), g);
		t.optionPane = new TestOptionPane(JOptionPane.YES_OPTION);
		t.player.playerDeck.generator = new Random(11142);
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
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		t.executeCard(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 1);

		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.HonorBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList,
				"Test");
		t.executeCard(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 3);
	}

	@Test
	public void testExecuteActionPower() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.PowerBoost));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		t.executeCard(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 1);
		assertEquals(t.player.honorTotal, 0);

		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.PowerBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList,
				"Test");
		t.executeCard(c);
		assertEquals(t.rune, 0);
		assertEquals(t.power, 3);
		assertEquals(t.player.honorTotal, 0);
	}

	@Test
	public void testExecuteActionRune() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.RuneBoost));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		t.executeCard(c);
		assertEquals(t.rune, 1);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 0);

		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.RuneBoost));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList,
				"Test");
		t.executeCard(c);
		assertEquals(t.rune, 3);
		assertEquals(t.power, 0);
		assertEquals(t.player.honorTotal, 0);
	}

	@Test
	public void testExecuteActionDraw() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.DrawCard));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		int before = this.t.player.playerDeck.hand.size();
		t.executeCard(c);
		assertEquals(before + 1, this.t.player.playerDeck.hand.size());

		actionList.clear();
		actionList.add(new Action(2, Action.ActionType.DrawCard));
		c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList,
				"Test");
		before = this.t.player.playerDeck.hand.size();
		t.executeCard(c);
		assertEquals(before + 2, this.t.player.playerDeck.hand.size());

	}

	
	@Test
	public void testExecuteActionDiscard() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.Discard));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		
		final Card c1 = new Card(new Rectangle(0, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test1");
		pList.get(0).playerDeck.hand.add(c1);

		final Card c2 = new Card(new Rectangle(100, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test2");
		pList.get(0).playerDeck.hand.add(c2);
		
		final Card c3 = new Card(new Rectangle(200, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test3");
		pList.get(0).playerDeck.hand.add(c3);
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(t.turnState, Turn.TurnState.Discard);
					assertEquals(t.turnStateMagnitude, 2);
					assertTrue(pList.get(0).playerDeck.hand.contains(c1));
					assertTrue(pList.get(0).playerDeck.hand.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
					assertFalse(pList.get(0).playerDeck.discard.contains(c1));
					assertFalse(pList.get(0).playerDeck.discard.contains(c2));
					assertFalse(pList.get(0).playerDeck.discard.contains(c3));
					
					t.leftButtonClick(new Point(50,50));

					assertEquals(t.turnState, Turn.TurnState.Discard);
					assertEquals(t.turnStateMagnitude, 1);
					assertFalse(pList.get(0).playerDeck.hand.contains(c1));
					assertTrue(pList.get(0).playerDeck.hand.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
					assertTrue(pList.get(0).playerDeck.discard.contains(c1));
					assertFalse(pList.get(0).playerDeck.discard.contains(c2));
					assertFalse(pList.get(0).playerDeck.discard.contains(c3));
					c2.setLocation(new Rectangle(100, 0, 100, 100));
					t.leftButtonClick(new Point(150,50));

					//assertEquals(t.turnState, Turn.TurnState.Default);
					//assertEquals(t.turnStateMagnitude, 0);
					assertFalse(pList.get(0).playerDeck.hand.contains(c1));
					assertFalse(pList.get(0).playerDeck.hand.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
					assertTrue(pList.get(0).playerDeck.discard.contains(c1));
					assertTrue(pList.get(0).playerDeck.discard.contains(c2));
					assertFalse(pList.get(0).playerDeck.discard.contains(c3));
				} catch (InterruptedException e) {
				} catch (IllegalMonitorStateException e1) {
				}

			}
		});
		thread.start();
		
		t.executeCard(c);
		

	}


	@Test
	public void testExecuteActionOptionalDiscardYes() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.OptionalDiscard));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		
		final Card c1 = new Card(new Rectangle(0, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test1");
		pList.get(0).playerDeck.hand.add(c1);

		final Card c2 = new Card(new Rectangle(100, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test2");
		pList.get(0).playerDeck.hand.add(c2);
		
		final Card c3 = new Card(new Rectangle(200, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test3");
		pList.get(0).playerDeck.hand.add(c3);
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(t.turnState, Turn.TurnState.Discard);
					assertEquals(t.turnStateMagnitude, 2);
					assertTrue(pList.get(0).playerDeck.hand.contains(c1));
					assertTrue(pList.get(0).playerDeck.hand.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
					assertFalse(pList.get(0).playerDeck.discard.contains(c1));
					assertFalse(pList.get(0).playerDeck.discard.contains(c2));
					assertFalse(pList.get(0).playerDeck.discard.contains(c3));
					
					t.leftButtonClick(new Point(50,50));

					assertEquals(t.turnState, Turn.TurnState.Discard);
					assertEquals(t.turnStateMagnitude, 1);
					assertFalse(pList.get(0).playerDeck.hand.contains(c1));
					assertTrue(pList.get(0).playerDeck.hand.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
					assertTrue(pList.get(0).playerDeck.discard.contains(c1));
					assertFalse(pList.get(0).playerDeck.discard.contains(c2));
					assertFalse(pList.get(0).playerDeck.discard.contains(c3));
					c2.setLocation(new Rectangle(100, 0, 100, 100));
					t.leftButtonClick(new Point(150,50));

					//assertEquals(t.turnState, Turn.TurnState.Default);
					//assertEquals(t.turnStateMagnitude, 0);
					assertFalse(pList.get(0).playerDeck.hand.contains(c1));
					assertFalse(pList.get(0).playerDeck.hand.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
					assertTrue(pList.get(0).playerDeck.discard.contains(c1));
					assertTrue(pList.get(0).playerDeck.discard.contains(c2));
					assertFalse(pList.get(0).playerDeck.discard.contains(c3));
				} catch (InterruptedException e) {
				} catch (IllegalMonitorStateException e1) {
				}

			}
		});
		thread.start();
		
		t.executeCard(c);
		

	}

	@Test
	public void testExecuteActionOptionalDiscardNo() {
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.OptionalDiscard));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.executeCard(c);
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
	}
	
	@Test
	public void testForcedDeckDiscard() {
		
		//Todo Fix 
//		ArrayList<Action> actionList = new ArrayList<Action>();
//		actionList.add(new Action(2, Action.ActionType.ForcedDeckBanish));
//		Card c = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
//				actionList, "Test");
//		assertEquals(t.turnState, Turn.TurnState.Default);
//		assertEquals(t.turnStateMagnitude, 0);
//		t.executeCard(c);
//		assertEquals(t.turnState, Turn.TurnState.DeckBanish);
//		assertEquals(t.turnStateMagnitude, 2);
	}

	@Test
	public void testStaticCardList() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		PlayerDeck d = new PlayerDeck();
		d.played.add(new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test"));
		d.hand.add(new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test"));
		d.discard.add(new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test"));
		d.notPlayed.add(new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test"));
		Player p1 = new Player(d, "gabe");
		Player p2 = new Player(d, "jack");
		ArrayList<Player> ps = new ArrayList<Player>();
		ps.add(p1);
		ps.add(p2);
		Game g = new Game(100);
		g.players = ps;
		Turn t1 = new Turn(p1, g);

		t1.leftButtonClick(new Point(1461, 493));

	}

	@Test
	public void testExecuteActionCenterBanish() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.CenterBanish));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Mechana, 1,
				actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		final Card c1 = new Card(new Rectangle(0, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test");
		g.gameDeck.hand.add(c1);

		final Card c2 = new Card(new Rectangle(100, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test2");
		g.gameDeck.hand.add(c2);

		final Card c3 = new Card(new Rectangle(200, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test3");
		g.gameDeck.hand.add(c3);

		g.gameDeck.notPlayed.add(new Card());
		g.gameDeck.notPlayed.add(new Card());
		g.gameDeck.notPlayed.add(new Card());

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(t.turnState, Turn.TurnState.CenterBanish);
					assertEquals(t.turnStateMagnitude, 3);
					assertTrue(g.gameDeck.hand.contains(c1));
					assertTrue(g.gameDeck.hand.contains(c2));
					assertTrue(g.gameDeck.hand.contains(c3));
					assertFalse(g.gameDeck.discard.contains(c1));
					assertFalse(g.gameDeck.discard.contains(c2));
					assertFalse(g.gameDeck.discard.contains(c3));

					t.leftButtonClick(new Point(50, 50));
					assertEquals(t.turnState, Turn.TurnState.CenterBanish);
					assertEquals(t.turnStateMagnitude, 2);
					// assertFalse(g.gameDeck.hand.contains(c1));
					assertTrue(g.gameDeck.hand.contains(c2));
					assertTrue(g.gameDeck.hand.contains(c3));
					// assertTrue(g.gameDeck.discard.contains(c1));
					assertFalse(g.gameDeck.discard.contains(c2));
					assertFalse(g.gameDeck.discard.contains(c3));

					t.leftButtonClick(new Point(150, 50));
					assertEquals(t.turnState, Turn.TurnState.CenterBanish);
					assertEquals(t.turnStateMagnitude, 1);
					assertFalse(g.gameDeck.hand.contains(c1));
					assertFalse(g.gameDeck.hand.contains(c2));
					assertTrue(g.gameDeck.hand.contains(c3));
					assertTrue(g.gameDeck.discard.contains(c1));
					assertTrue(g.gameDeck.discard.contains(c2));
					assertFalse(g.gameDeck.discard.contains(c3));

					t.leftButtonClick(new Point(250, 50));
					assertEquals(t.turnState, Turn.TurnState.Default);
					assertEquals(t.turnStateMagnitude, 0);
					assertFalse(g.gameDeck.hand.contains(c1));
					assertFalse(g.gameDeck.hand.contains(c2));
					assertFalse(g.gameDeck.hand.contains(c3));
					assertTrue(g.gameDeck.discard.contains(c1));
					assertTrue(g.gameDeck.discard.contains(c2));
					assertTrue(g.gameDeck.discard.contains(c3));

				} catch (InterruptedException e) {
				} catch (IllegalMonitorStateException e1) {
				}

			}
		});
		thread.start();
		t.executeCard(testCard);

	}

	
	@Test
	public void testExecuteActionAskaraCenterBanish() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.AskaraCenterBanish));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		final Card c1 = new Card(new Rectangle(0,0,100,100),Card.Type.Monster,Card.Faction.Void,3,null,"Test");
		g.gameDeck.hand.add(c1);
		
		final Card c2 = new Card(new Rectangle(100,0,100,100),Card.Type.Construct,Card.Faction.Lifebound,3,null,"Test2");
		g.gameDeck.hand.add(c2);
		
		final Card c3 = new Card(new Rectangle(200,0,100,100),Card.Type.Construct,Card.Faction.Lifebound,3,null,"Test3");
		g.gameDeck.hand.add(c3);
		
		g.gameDeck.notPlayed.add(new Card());
		g.gameDeck.notPlayed.add(new Card());
		g.gameDeck.notPlayed.add(new Card());
		
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(t.turnState, Turn.TurnState.AskaraCenterBanish);
					assertEquals(t.turnStateMagnitude, 1);
					assertTrue(g.gameDeck.hand.contains(c1));
					assertTrue(g.gameDeck.hand.contains(c2));
					assertTrue(g.gameDeck.hand.contains(c3));
					assertFalse(g.gameDeck.discard.contains(c1));
					assertFalse(g.gameDeck.discard.contains(c2));
					assertFalse(g.gameDeck.discard.contains(c3));
					assertEquals(t.player.honorTotal, 0);

					t.leftButtonClick(new Point(50,50));
					assertEquals(t.turnState, Turn.TurnState.Default);
					assertEquals(t.turnStateMagnitude, 0);
					assertFalse(g.gameDeck.hand.contains(c1));
					assertTrue(g.gameDeck.hand.contains(c2));
					assertTrue(g.gameDeck.hand.contains(c3));
					assertTrue(g.gameDeck.discard.contains(c1));
					assertFalse(g.gameDeck.discard.contains(c2));
					assertFalse(g.gameDeck.discard.contains(c3));
					assertEquals(t.player.honorTotal, 3);

				} catch (InterruptedException e) {}
				catch (IllegalMonitorStateException e1) {}
				
			}
		});
		thread.start();
		t.executeCard(testCard);
	}
	

	@Test
	public void testExecuteActionOptionalDeckBanishYes() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.OptionalDeckBanish));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1,
				actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.player.playerDeck.drawNCards(5);
		final Card toBanish = t.player.playerDeck.hand.get(1);
		toBanish.setLocation(new Rectangle(0, 0, 100, 100));
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(Turn.TurnState.DeckBanish, t.turnState);
					assertEquals(1, t.turnStateMagnitude);

					t.leftButtonClick(new Point(50, 50));
					assertEquals(Turn.TurnState.Default, t.turnState);
					assertFalse(t.player.playerDeck.hand.contains(toBanish));
					assertTrue(t.game.gameDeck.discard.contains(toBanish));
				} catch (InterruptedException e) {
				} catch (IllegalMonitorStateException e1) {
				}

			}
		});
		thread.start();
		t.executeCard(testCard);

	}

	@Test
	public void testExecuteActionOptionalDeckBanishNo() {
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.OptionalDeckBanish));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1,
				actionList, "Test");
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
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
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
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Mechana, 1,
				actionList, "Test");
		assertEquals(t.constructRune, 0);
		t.executeCard(testCard);
		assertEquals(t.constructRune, 1);
	}

	@Test
	public void testExecuteActionMechanaConstructRuneBoost() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2,
				Action.ActionType.MechanaConstructRuneBoost));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Mechana, 1,
				actionList, "Test");
		assertEquals(t.mechanaConstructRune, 0);
		t.executeCard(testCard);
		assertEquals(t.mechanaConstructRune, 2);
	}

	@Test
	public void testExecuteActionEnterAiyanaState() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.EnterAiyanaState));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test");
		assertFalse(t.AiyanaState);
		t.executeCard(testCard);
		assertTrue(t.AiyanaState);
	}

	@Test
	public void testBoostMonsterPower() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.MonsterPowerBoost));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		assertEquals(0, t.monsterPower);
		t.executeCard(testCard);
		assertEquals(3, t.monsterPower);
	}

	@Test
	public void testBoostHeroRune() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(2, Action.ActionType.HeroRuneBoost));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1,
				actionList, "Test");
		assertEquals(0, t.heroRune);
		t.executeCard(testCard);
		assertEquals(2, t.heroRune);
	}

	@Test
	public void testDefeatMonsterWNoMiddleMonster() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.DefeatMonster));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test");
		assertEquals(Turn.TurnState.Default, t.turnState);
		t.executeCard(testCard);
		assertEquals(t.player.honorTotal, 1);
	}

	@Test
	public void testDefeatMonsterWMiddleMonster() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(3, Action.ActionType.DefeatMonster));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test");
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
		actionList.add(new Action(1, Action.ActionType.PowerBoost, 0));
		actionList.add(new Action(3, Action.ActionType.OptionalDeckBanish));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		pList.get(0).playerDeck.hand.add(c);
		pList.get(0).playerDeck.drawNCards(2);
		assertEquals(5, pList.get(0).playerDeck.hand.size());
		t.playAll();
		assertEquals(0, pList.get(0).playerDeck.hand.size());
		assertEquals(4, t.rune + t.power);
	}

	@Test
	public void testNookHound() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.NookHound));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		int handStartingSize = t.player.playerDeck.hand.size();
		t.executeCard(testCard);
		assertEquals(t.player.playerDeck.hand.size(), handStartingSize + 1);
	}
	
	@Test
	public void testVoidMesmer() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.EnterVoidMesmer));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertFalse(t.VoidMesmerState);
		t.executeCard(testCard);
		assertTrue(t.VoidMesmerState);
	}
	
	@Test
	public void testAskaraOfFate() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.AskaraOfFate));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		int startingHandSize = t.player.playerDeck.hand.size();
		t.executeCard(testCard);
		assertEquals(t.player.playerDeck.hand.size(), startingHandSize + 2);
	}
	
	@Test
	public void testHeavyOrMystic() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.HeavyOrMystic));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test");
		t.executeCard(testCard);
		assertEquals(t.player.playerDeck.hand.get(0).getName(), Main.getMystic().getName());
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		t.executeCard(testCard);
		assertEquals(t.player.playerDeck.hand.get(1).getName(), Main.getHeavyInfantry().getName());
	}
	
	@Test
	public void testLunarStag() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.LunarStag));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1, actionList, "Test");
		t.executeCard(testCard);
		assertEquals(t.rune, 2);
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		t.executeCard(testCard);
		assertEquals(t.player.honorTotal, 2);
	}
	
	@Test
	public void testFreeCard() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.FreeCard));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Common, 1, actionList, "Test");
		t.executeCard(testCard);
		assertEquals(t.turnState, Turn.TurnState.FreeCard);
		assertEquals(t.turnStateMagnitude, 1);
	}
	
	@Test
	public void testPlayAllWAcceptedBanish() throws InterruptedException {
		pList.get(0).playerDeck.drawNCards(2);
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.CenterBanish));
		actionList.add(new Action(15, Action.ActionType.PowerBoost, 0));
		Card c = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1,
				actionList, "Test");
		pList.get(0).playerDeck.hand.add(c);
		pList.get(0).playerDeck.drawNCards(2);

		final Card c1 = new Card(new Rectangle(0, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test");
		g.gameDeck.hand.add(c1);
		g.gameDeck.notPlayed.add(new Card());

		assertEquals(5, pList.get(0).playerDeck.hand.size());
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(2, pList.get(0).playerDeck.hand.size());
					assertEquals(2, t.rune + t.power);
					assertEquals(Turn.TurnState.CenterBanish, t.turnState);
					assertEquals(1, t.turnStateMagnitude);

					t.leftButtonClick(new Point(50, 50));

					assertEquals(Turn.TurnState.Default, t.turnState);
					assertEquals(0, t.turnStateMagnitude);

				} catch (InterruptedException e) {
				} catch (IllegalMonitorStateException e1) {
				}

			}
		});
		thread.start();
		this.t.playAll();
		assertEquals(0, pList.get(0).playerDeck.hand.size());
		assertEquals(19, t.rune + t.power);
	}
	
	@Test
	public void testFreeCardHero() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.FreeCard));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.player.playerDeck.drawNCards(5);
		t.rune = 17;
		
		final Card c1 = new Card(new Rectangle(0,0,100,100),Card.Type.Construct,Card.Faction.Lifebound,100,null,"Test");
		g.gameDeck.hand.add(c1);
		
		g.gameDeck.notPlayed.add(new Card());
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(Turn.TurnState.FreeCard, t.turnState);
					assertEquals(1, t.turnStateMagnitude);
					t.leftButtonClick(new Point(50,50));
					assertEquals(Turn.TurnState.Default,t.turnState);
					assertEquals(0, t.turnStateMagnitude);
					assertTrue(pList.get(0).playerDeck.discard.contains(c1));
					assertEquals(17, t.rune);
				} catch (InterruptedException e) {}
				catch (IllegalMonitorStateException e1) {}
				
			}
		});
		thread.start();
		t.executeCard(testCard);
	}
	
	@Test
	public void testFreeCardMonster() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.FreeCard));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.player.playerDeck.drawNCards(5);
		t.power = 17;
		
		ArrayList<Action> monsterActionList = new ArrayList<Action>();
		monsterActionList.add(new Action(73, Action.ActionType.HonorBoost));
		final Card c1 = new Card(new Rectangle(0,0,100,100),Card.Type.Monster,Card.Faction.Void,100,monsterActionList,"Test");
		g.gameDeck.hand.add(c1);
		
		g.gameDeck.notPlayed.add(new Card());
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(Turn.TurnState.FreeCard, t.turnState);
					assertEquals(1, t.turnStateMagnitude);
					t.leftButtonClick(new Point(50,50));
					assertEquals(Turn.TurnState.Default,t.turnState);
					assertEquals(0, t.turnStateMagnitude);
					assertTrue(g.gameDeck.discard.contains(c1));
					assertEquals(17, t.power);
					assertEquals(73, pList.get(0).honorTotal);
				} catch (InterruptedException e) {}
				catch (IllegalMonitorStateException e1) {}
				
			}
		});
		thread.start();
		t.executeCard(testCard);
		
	}


	@Test
	public void testExecuteActionEnterMesmerState() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.EnterVoidMesmer));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test");
		assertFalse(t.VoidMesmerState);
		t.executeCard(testCard);
		assertTrue(t.VoidMesmerState);
	}

	@Test
	public void testExecuteActionLunarStag() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.LunarStag));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test");
		int rune_bef = t.rune;
		t.executeCard(testCard);
		assertEquals(rune_bef+2, t.rune);
	}

	@Test
	public void testExecuteFreeCard() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.FreeCard));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Lifebound, 1,
				actionList, "Test");
		t.executeCard(testCard);
	}

	@Test
	public void testTestForMechana() {
		Card c = new Card();
		t.HedronLinkDeviceState = false;
		c.setFaction(Card.Faction.Lifebound);
		assertFalse(t.testForMechana(c));
		t.HedronLinkDeviceState = true;
		assertTrue(t.testForMechana(c));
		c.setFaction(Card.Faction.Mechana);
		assertTrue(t.testForMechana(c));
		t.HedronLinkDeviceState = false;
		assertTrue(t.testForMechana(c));
	}
	
	@Test
	public void testHandleRocketCourier() {
		t.RocketCourierState = 4;
		Card c = new Card();
		c.setName("THIS IS A TEST");
		t.handleRocketCourier(c);
		assertTrue(t.player.playerDeck.hand.get(0).getName().equals("THIS IS A TEST"));
		assertEquals(t.RocketCourierState, 3);
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		t.handleRocketCourier(c);
		assertTrue(t.player.playerDeck.discard.get(0).getName().equals("THIS IS A TEST"));
		assertEquals(t.RocketCourierState, 3);
		t.optionPane = new TestOptionPane(JOptionPane.YES_OPTION);
		t.handleRocketCourier(c);
		assertEquals(t.RocketCourierState, 2);
		t.handleRocketCourier(c);
		assertEquals(t.RocketCourierState, 1);
		t.handleRocketCourier(c);
		assertEquals(t.RocketCourierState, 0);
		t.handleRocketCourier(c);
		assertEquals(t.RocketCourierState, -1);
	}
	
	@Test
	public void testRocketCourier() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.RocketCourier));
		Card testCard = new Card(Card.Type.Construct, Card.Faction.Mechana, 1,
				actionList, "Test");
		t.executeCard(testCard);
		assertEquals(t.RocketCourierState, 1);
		t.executeCard(testCard);
		assertEquals(t.RocketCourierState, 2);
	}
	
	@Test
	public void testTablet() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.TabletOfTimesDawn));
		Card testCard = new Card(Card.Type.Construct, Card.Faction.Enlightened, 1,
				actionList, "Test");
		t.executeCard(testCard);
		assertTrue(t.game.extraTurn);
		assertFalse(t.player.playerDeck.constructs.contains(testCard));
		Card notTablet = new Card();
		notTablet.setName("Not_the_Tablet_of_Times_Dawn");
		t.player.playerDeck.constructs.add(notTablet);
		t.executeCard(testCard);
		assertTrue(t.game.extraTurn);
		assertFalse(t.player.playerDeck.constructs.contains(testCard));
		Card tablet = new Card();
		tablet.setName("Tablet_of_Times_Dawn");
		t.player.playerDeck.constructs.add(tablet);
		t.executeCard(testCard);
		assertTrue(t.game.extraTurn);
		assertFalse(t.player.playerDeck.constructs.contains(testCard));
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		t.executeCard(testCard);
	}
	
	@Test
	public void testAvatarGolem() {
		t.power = 0;
		t.player.honorTotal = 0;
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.AvatarGolem));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Mechana, 1,
				actionList, "Test");
		t.executeCard(testCard);
		assertEquals(t.power, 2);
		assertEquals(t.player.honorTotal, 0);
		Card c1 = new Card();
		Card c2 = new Card();
		Card c3 = new Card();
		Card c4 = new Card();
		c1.setFaction(Card.Faction.Enlightened);
		c2.setFaction(Card.Faction.Lifebound);
		c3.setFaction(Card.Faction.Mechana);
		c4.setFaction(Card.Faction.Void);
		t.player.playerDeck.constructs.add(c1);
		t.player.playerDeck.constructs.add(c2);
		t.player.playerDeck.constructs.add(c3);
		t.player.playerDeck.constructs.add(c4);
		t.executeCard(testCard);
		assertEquals(t.power, 4);
		assertEquals(t.player.honorTotal, 4);
		t.player.playerDeck.constructs.add(c1);
		t.player.playerDeck.constructs.add(c1);
		t.player.playerDeck.constructs.add(c1);
		t.player.playerDeck.constructs.add(c1);
		t.player.playerDeck.constructs.add(c2);
		t.player.playerDeck.constructs.add(c2);
		t.player.playerDeck.constructs.add(c2);
		t.player.playerDeck.constructs.add(c3);
		t.player.playerDeck.constructs.add(c3);
		t.player.playerDeck.constructs.add(c3);
		t.player.playerDeck.constructs.add(c3);
		t.player.playerDeck.constructs.add(c3);
		t.player.playerDeck.constructs.add(c3);
		t.player.playerDeck.constructs.add(c4);
		t.player.playerDeck.constructs.add(c4);
		t.player.playerDeck.constructs.add(c4);
		t.player.playerDeck.constructs.add(c4);
		t.player.playerDeck.constructs.add(c4);
		t.executeCard(testCard);
		assertEquals(t.power, 6);
		assertEquals(t.player.honorTotal, 8);

	}
	
	@Test
	public void testYggdrasilStaff() {
		t.power = 0;
		t.player.honorTotal = 0;
		t.rune = 10;
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.YggdrasilStaff));
		Card testCard = new Card(Card.Type.Construct, Card.Faction.Lifebound, 1,
				actionList, "Test");
		t.executeCard(testCard);
		assertEquals(t.power, 1);
		assertEquals(t.player.honorTotal, 3);
		assertEquals(t.rune, 6);
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		t.executeCard(testCard);
		assertEquals(t.power, 2);
		assertEquals(t.player.honorTotal, 3);
		assertEquals(t.rune, 6);
		t.optionPane = new TestOptionPane(JOptionPane.YES_OPTION);
		t.executeCard(testCard);
		assertEquals(t.power, 3);
		assertEquals(t.player.honorTotal, 6);
		assertEquals(t.rune, 2);
		t.executeCard(testCard);
		assertEquals(t.power, 4);
		assertEquals(t.player.honorTotal, 6);
		assertEquals(t.rune, 2);
	}
	
	@Test
	public void testMechanaInitiate() {
		t.power = 0;
		t.rune = 0;
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(4, Action.ActionType.MechanaInitiate));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Mechana, 1,
				actionList, "Test");
		t.executeCard(testCard);
		assertEquals(t.power, 0);
		assertEquals(t.rune, 1);
		t.optionPane = new TestOptionPane(JOptionPane.NO_OPTION);
		t.executeCard(testCard);
		assertEquals(t.power, 1);
		assertEquals(t.rune, 1);
	}
	
	@Test
	public void testAttemptCardPurchase(){
		
	}
	
	@Test
	public void testRaj(){

		
		ArrayList<Action> actionList = new ArrayList<Action>();
		
		actionList.add(new Action(0, Action.ActionType.RajAction));
		
		Card c1 = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test0");
		Card c2 = new Card(new Rectangle(0,0,100,100),Card.Type.Hero, Card.Faction.Void, 3, null, "Test1");
		Card c3 = new Card(new Rectangle(100,100,100,100),Card.Type.Hero, Card.Faction.Mechana, 3, null, "Test2");
		
		this.t.player.playerDeck.hand.add(c2);
		
		this.t.game.gameDeck.hand.add(c3);
		
		Thread thread = new Thread(new Runnable() {
		
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(Turn.TurnState.RajTurnState, t.turnState);
					assertEquals(1, t.turnStateMagnitude);
					t.leftButtonClick(new Point(50,50));
					assertEquals(Turn.TurnState.RajTurnState2, t.turnState);
					t.leftButtonClick(new Point(150,150));
					assertEquals(Turn.TurnState.Default,t.turnState);
					System.out.println("Here");
					
				} catch (InterruptedException e) {}
				
			}
		});
		thread.start();
		t.executeCard(c1);

		
	}
	
	@Test
	public void testAskaraDiscardEnlightened() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.AskaraDiscard));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		final Card c1 = new Card(new Rectangle(0, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Enlightened, 3, null, "Test1");
		pList.get(0).playerDeck.hand.add(c1);

		final Card c2 = new Card(new Rectangle(100, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test2");
		pList.get(0).playerDeck.hand.add(c2);
		
		final Card c3 = new Card(new Rectangle(200, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test3");
		pList.get(0).playerDeck.hand.add(c3);
		
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(Turn.TurnState.AskaraDiscard, t.turnState);
					assertEquals(2, t.turnStateMagnitude);
					t.leftButtonClick(new Point(50,50));
					assertEquals(Turn.TurnState.Default,t.turnState);
					assertEquals(0, t.turnStateMagnitude);
					assertTrue(pList.get(0).playerDeck.discard.contains(c1));
					assertTrue(pList.get(0).playerDeck.hand.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
				} catch (InterruptedException e) {}
				catch (IllegalMonitorStateException e1) {}
				
			}
		});
		thread.start();
		t.executeCard(testCard);
	}
	
	@Test
	public void testAskaraDiscardNotEnlightened() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.AskaraDiscard));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		final Card c1 = new Card(new Rectangle(0, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test1");
		pList.get(0).playerDeck.hand.add(c1);

		final Card c2 = new Card(new Rectangle(100, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test2");
		pList.get(0).playerDeck.hand.add(c2);
		
		final Card c3 = new Card(new Rectangle(200, 0, 100, 100),
				Card.Type.Construct, Card.Faction.Lifebound, 3, null, "Test3");
		pList.get(0).playerDeck.hand.add(c3);
		
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(Turn.TurnState.AskaraDiscard, t.turnState);
					assertEquals(2, t.turnStateMagnitude);
					t.leftButtonClick(new Point(50,50));
					assertEquals(Turn.TurnState.AskaraDiscard, t.turnState);
					assertEquals(1, t.turnStateMagnitude);
					assertTrue(pList.get(0).playerDeck.discard.contains(c1));
					assertTrue(pList.get(0).playerDeck.hand.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
					c2.setLocation(new Rectangle(100, 0, 100, 100));
					t.leftButtonClick(new Point(150,50));
					assertEquals(Turn.TurnState.Default,t.turnState);
					assertEquals(0, t.turnStateMagnitude);
					assertTrue(pList.get(0).playerDeck.discard.contains(c1));
					assertTrue(pList.get(0).playerDeck.discard.contains(c2));
					assertTrue(pList.get(0).playerDeck.hand.contains(c3));
				} catch (InterruptedException e) {}
				catch (IllegalMonitorStateException e1) {}
				
			}
		});
		thread.start();
		t.executeCard(testCard);
	}
	
	@Test
	public void testTwofoldAskaraPlayedNoHero() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.TwofoldAskaraPlayed));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		t.executeCard(testCard);
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
	}
	
	@Test
	public void testTwofoldAskaraPlayedWithHero() {
		ArrayList<Action> actionList = new ArrayList<Action>();
		actionList.add(new Action(1, Action.ActionType.TwofoldAskaraPlayed));
		Card testCard = new Card(Card.Type.Hero, Card.Faction.Void, 1, actionList, "Test");
		assertEquals(t.turnState, Turn.TurnState.Default);
		assertEquals(t.turnStateMagnitude, 0);
		assertEquals(t.rune, 0);
		ArrayList<Action> actionList2 = new ArrayList<Action>();
		actionList2.add(new Action(10, Action.ActionType.RuneBoost));
		final Card c1 = new Card(new Rectangle(0, 0, 100, 100),
				Card.Type.Hero, Card.Faction.Lifebound, 3, actionList2, "Test1");
		pList.get(0).playerDeck.played.add(c1);

		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(10);
					assertEquals(Turn.TurnState.TwofoldAskara, t.turnState);
					t.leftButtonClick(new Point(50,50));
					assertEquals(t.rune, 10);
					assertEquals(t.turnState, Turn.TurnState.Default);
					assertEquals(t.turnStateMagnitude, 0);
					System.out.println("Here");
				} catch (InterruptedException e) {}
				catch (IllegalMonitorStateException e1) {}
				
			}
		});
		thread.start();
		t.executeCard(testCard);
	}
	
	@Test
	public void testTabletOfTimesDawn(){
		ArrayList<Action> actionList = new ArrayList<Action>();
		
		actionList.add(new Action(0, Action.ActionType.AskaraDiscard));//change to the correct SHIT
		
		Card c1 = new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, actionList, "Test0");
		
		//Todo doesn't work
		//t.executeCard(c1);
		
		//CHECK THE BOOLEAN IN GAME
	}
}


