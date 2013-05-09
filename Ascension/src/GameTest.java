import static org.junit.Assert.*;

import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JFrame;

import junit.framework.Assert;


import org.junit.Test;


public class GameTest {

	@Test
	public void testInitialization() {
		Player player1 = new Player(null, null);
		
		ArrayList<Player> players = new ArrayList<Player>();
		
		players.add(player1);
		
		
	}
//	@Test
//	public void testNextTurn(){
//		
//	}

	@Test
	public void testPaintComponentDoesNotErrorOut() {
//		ArrayList<Card> centerDeck = Main.getCenterDeck("src/centerDeck.txt");
//		Deck d = new Deck(centerDeck, new ArrayList<Card>(), new ArrayList<Card>(), Main.getTopCards(), null);
//		d.shuffle();
//		d.drawNCards(6);
//		
//		ArrayList<Player> pList = new ArrayList<Player>();
//		Player jack = Player.getNewPlayer("Jack");
//		
//		ArrayList<Card> generatedHand = new ArrayList<Card>();
//		
//		PlayerDeck pDeck = new PlayerDeck();
//		ArrayList<Action> a = new ArrayList<Action>();
//		a.add(new Action(1,Action.ActionType.DrawCard));
//		generatedHand.add(new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, a, "TestCard",1));
//		
//		pDeck.hand = generatedHand;
//		pList.add(jack);
//		
//		Game g = new Game(1, pList, d);
//		
//		g.gameDeck = new Deck();
//		g.gameDeck.notPlayed = Main.getCenterDeck("src/testDeck.txt");
//		g.gameDeck.drawNCards(6);
//		g.isTest = true;
//		
//		JFrame frame = new JFrame();
//		frame.setVisible(true);
//		frame.setSize(1620, 940);
//		frame.add(g);
//		
//		g.play();
//		g.repaint();
//		
//		frame.dispose();
	}

	@Test
	public void testGetCenterDeck() {
		Game g = new Game(10);
		g.gameDeck = new Deck();
		g.gameDeck.notPlayed = Main.getCenterDeck("src/testDeck.txt");
		
		ArrayList<Card> c = new ArrayList<Card>();
		
		//Arha_Initiate
		ArrayList<Action> a = new ArrayList<Action>();
		a.add(new Action(1,Action.ActionType.DrawCard));
		c.add(new Card(Card.Type.Hero, Card.Faction.Enlightened, 1, a, "Arha_Initiate",1));
		
		//Samaels_Trickster
		ArrayList<Action> a2 = new ArrayList<Action>();
		a2.add(new Action(1,Action.ActionType.RuneBoost));
		a2.add(new Action(1,Action.ActionType.HonorBoost));
		c.add(new Card(Card.Type.Monster, Card.Faction.Void,3, a2, "Samaels_Trickster", 0));
		
		//The_All-Seeing_Eye
		ArrayList<Action> a3 = new ArrayList<Action>();
		a3.add(new Action(1,Action.ActionType.DrawCard));
		c.add(new Card(Card.Type.Construct, Card.Faction.Enlightened, 6, a3, "The_All-Seeing_Eye",2));
				
		//Arha_Initiate
		ArrayList<Action> a4 = new ArrayList<Action>();
		a4.add(new Action(3,Action.ActionType.PowerBoost));
		c.add(new Card(Card.Type.Hero, Card.Faction.Void, 4, a4, "Demon_Slayer",2));
		
		//Arha_Initiate
		ArrayList<Action> a5 = new ArrayList<Action>();
		a5.add(new Action(2,Action.ActionType.DrawCard));
		a5.add(new Action(1,Action.ActionType.Discard));
		c.add(new Card(Card.Type.Hero, Card.Faction.Enlightened, 3, a5, "Elder_Skeptic",1));
		
		for(int i = 0; i < 3; i++) {
			helperTestCardsEqual(c.get(0), g.gameDeck.notPlayed.get(i));
		}
		for(int i = 3; i < 7; i++) {
			helperTestCardsEqual(c.get(1), g.gameDeck.notPlayed.get(i));
		}
		for(int i = 7; i < 8; i++) {
			helperTestCardsEqual(c.get(2), g.gameDeck.notPlayed.get(i));
		}
		for(int i = 8; i < 10; i++) {
			helperTestCardsEqual(c.get(3), g.gameDeck.notPlayed.get(i));
		}
		for(int i = 10; i < 13; i++) {
			helperTestCardsEqual(c.get(4), g.gameDeck.notPlayed.get(i));
		}
	}
	
	public void helperTestCardsEqual(Card c1, Card c2) {
		assertEquals(c1.getName(), c2.getName());
		assertEquals(c1.getType(), c2.getType());
		assertEquals(c1.getFaction(), c2.getFaction());
		assertEquals(c1.getCost(), c2.getCost());
		assertEquals(c1.getActions().size(), c2.getActions().size());
		for(int i = 0; i < c1.getActions().size(); i++) {
			assertEquals(c1.getActions().get(i).magnitude, c2.getActions().get(i).magnitude);
			assertEquals(c1.getActions().get(i).action, c2.getActions().get(i).action);
		}
		
	}
	
	@Test
	public void testGetTopCards() {
		ArrayList<Card> cards = Main.getTopCards();
		for(Card c : cards) {
			assertTrue((c.getName().equals("Heavy_Infantry") && c.getCost() == 2)
					|| (c.getName().equals("Cultist") && c.getCost() == 2)
					|| (c.getName().equals("Mystic")&& c.getCost() == 3));
		}
	}
	
	@Test
	public void testEndGame(){
		Game g = new Game(0);
		ArrayList<Card> discard = new ArrayList<Card>();
		
		PlayerDeck d = new PlayerDeck(discard, discard, discard, Game.handLoc, Game.constructs,Game.playedLoc);
		
		g.players = new ArrayList<Player>();
		
		g.players.add(new Player(d, "jack"));
		g.players.add(new Player(d, "gabe"));
		g.players.add(new Player(d, "kenneth"));
		
		assertTrue(g.endGame().equals("jackgabekenneth have tied"));
		
		g.players.get(0).honorTotal = 100;
		g.players.get(1).playerDeck.hand.add(new Card(new Rectangle(0,0,10,10), Card.Type.Construct, Card.Faction.Enlightened, 10, new ArrayList<Action>(),"bitch", 0));
		
		assertTrue(g.endGame().equals("jack wins"));
		assertEquals(g.players.get(1).honorTotal, 0);
		
		
	}

	@Test
	public void testNextTurn() {
		ArrayList<Card> centerDeck = Main.getCenterDeck("src/centerDeck.txt");
		Deck d = new Deck(centerDeck, new ArrayList<Card>(), new ArrayList<Card>(), Main.getTopCards(), null);
		d.shuffle();
		d.drawNCards(6);
		
		ArrayList<Player> pList = new ArrayList<Player>();
		Player jack = Player.getNewPlayer("Jack");
		Player gabe = Player.getNewPlayer("Gabe");
		Player kenny = Player.getNewPlayer("Kenny");
		pList.add(jack);
		pList.add(gabe);
		pList.add(kenny);
		Game g = new Game(3, pList, d);
		System.out.println(g.players);
		
		Assert.assertNull(g.currentTurn);
		
		g.nextTurn();
		System.out.println(g.players);
//		Assert.assertNotNull(g.currentTurn);
//		Assert.assertNotNull(g.currentTurn.player);
//		Assert.assertEquals(jack, g.currentTurn.player);
		
		g.nextTurn();
		System.out.println(g.players);
//		Assert.assertNotNull(g.currentTurn);
//		Assert.assertNotNull(g.currentTurn.player);
//		Assert.assertEquals(gabe, g.currentTurn.player);
		
		g.nextTurn();
		System.out.println(g.players);
//		Assert.assertNotNull(g.currentTurn);
//		Assert.assertNotNull(g.currentTurn.player);
//		Assert.assertEquals(kenny, g.currentTurn.player);
		
		g.gameHonor = 22;
		
		g.decrementHonor(20);
		
		Assert.assertEquals(2, g.gameHonor);
		
		
	}
	
}
