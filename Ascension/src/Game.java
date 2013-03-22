import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Locale.Category;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Game extends JComponent {

	public int totalHonor;
	public ArrayList<Player> players;
	public Deck gameDeck;
	public MouseListen theListener;
	public Turn currentTurn;
	
	private static Rectangle centerRow = new Rectangle(204,253,1168,167);
	
	private static Rectangle handLoc = new Rectangle(184,670,1224,160);
	private static Rectangle playedLoc = new Rectangle(184,460,1224,161);
	private static Rectangle constructs = new Rectangle(52, 856, 1495, 160);
	
	Game(int honor) {
		this(honor, null, null);
	}
	
	
	Game(int honor, ArrayList<Player> players, Deck gameDeck) {
		this.totalHonor = honor;
		this.players = players;
		this.gameDeck = gameDeck;
		
	}

	int i;

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		
		Image image_back = new ImageIcon(this.getClass().getResource("Background.jpg")).getImage();
		g2.drawImage(image_back,0,0,1600,900,null);
		
		
		if(this.gameDeck.hand == null || this.currentTurn == null || this.currentTurn.player.playerDeck == null) {
			return;
		}
		for(Card c:this.gameDeck.hand) {
			g2.drawImage(c.getImage(), c.getLocation().x,c.getLocation().y,c.getLocation().width,c.getLocation().height,null);
		}
		for(Card c:this.currentTurn.player.playerDeck.played) {
			g2.drawImage(c.getImage(), c.getLocation().x,c.getLocation().y,c.getLocation().width,c.getLocation().height,null);
		}
		for(Card c:this.currentTurn.player.playerDeck.constructs) {
			g2.drawImage(c.getImage(), c.getLocation().x,c.getLocation().y,c.getLocation().width,c.getLocation().height,null);
		}
		for(Card c:this.currentTurn.player.playerDeck.hand){
			g2.drawImage(c.getImage(), c.getLocation().x,c.getLocation().y,c.getLocation().width,c.getLocation().height,null);
		}
		
	}
	
	
	public void play() {

		this.theListener = new MouseListen(null, this);
		this.addMouseListener(this.theListener);

		nextTurn();
		
		while (this.totalHonor > 0) {
			try {
				Thread.sleep(50);
				repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}

	}
	
	public void nextTurn() {
		if(currentTurn == null) {
			this.currentTurn = new Turn(players.get(0));
		} else {
			int i = players.indexOf(currentTurn.player);
			int n = (i++) % players.size();
			this.currentTurn = new Turn(players.get(n));
		}
		
		this.currentTurn.player.startingHand();
		this.theListener.setTurn(this.currentTurn);
	}
	
	public void loadGame(){
		ArrayList<Card> start = new ArrayList<Card>();
		PlayerDeck startingDeck = new PlayerDeck(start, null, null, null);
		
		Player p1 = new Player(startingDeck);
		
		this.players.add(p1);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		frame.setSize(1620, 940);
		frame.setVisible(true);

		ArrayList<Card> notPlayed = DeckTest.randomCardList();
		ArrayList<Card> hand = new ArrayList<Card>();
		ArrayList<Card> discard = new ArrayList<Card>();
		
		Deck d = new Deck(notPlayed, hand, discard, centerRow);
		d.drawNCards(6);
		
		ArrayList<Card> notPlayed2 = new ArrayList<Card>();
		
		ArrayList<Action> action1 = new ArrayList<Action>();
		action1.add(new Action(1, Action.ActionType.RuneBoost));
		for(int i = 0; i < 8; i++) {
			notPlayed2.add(new Card(Card.Type.Hero, Card.Faction.Enlightened,0,action1, "Apprentice"));
		}
		
		ArrayList<Action> action2 = new ArrayList<Action>();
		action2.add(new Action(1, Action.ActionType.PowerBoost));
		for(int i = 0; i < 2; i++) {
			notPlayed2.add(new Card(Card.Type.Hero, Card.Faction.Enlightened,0,action2, "Militia"));
		}
		
		ArrayList<Card> hand2 =  new ArrayList<Card>();
		ArrayList<Card> discard2 =  new ArrayList<Card>();
		
		PlayerDeck pD = new PlayerDeck(notPlayed2, hand2, discard2, handLoc, playedLoc, constructs);
		
		ArrayList<Player> plays = new ArrayList<Player>();
		
		Game g = new Game(100,plays,d);
		
	
		g.players.add(new Player(pD));
		
		frame.add(g);

		frame.setVisible(true);
		frame.setSize(1621, 941);
		
		g.play();
		
	}
}
