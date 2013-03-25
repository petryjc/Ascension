import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

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
	Image image_back;
	
	private static Rectangle centerRow = new Rectangle(204,253,1168,167);
	
	public static Rectangle handLoc = new Rectangle(184,670,1224,160);
	public static Rectangle playedLoc = new Rectangle(184,460,1224,161);
	public static Rectangle constructs = new Rectangle(52, 856, 1495, 160);
	
	Game(int honor) {
		this(honor, null, null);
	}
	
	
	
	Game(int honor, ArrayList<Player> players, Deck gameDeck) {
		this.totalHonor = honor;
		this.players = players;
		this.gameDeck = gameDeck;
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.drawImage(image_back,0,0,1600,900,null);
		
		//draw the four visible card sets
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
		
		//Draw the table with players and their current honor
		g2.setFont(new Font("TimesNewRoman",30,20));
		g2.drawString(currentTurn.player.name, 5, 0 + 28);
		g2.drawString(currentTurn.player.honorTotal + "", 170, 28);
		this.players.remove(currentTurn.player);
		for(int i = 0; i < this.players.size(); i++) {
				Rectangle r = new Rectangle(0,(i + 1)*30,200,30);
				g2.draw(r);
				g2.drawString(players.get(i).name, 5, (i + 1)*30 + 28);
				g2.drawString(players.get(i).honorTotal + "", 170, (i + 1)*30 + 28);
		}
		this.players.add(currentTurn.player);
		//Draw the additional game state info
		g2.setFont(new Font("TimesNewRoman",30,50));
		g2.drawString(currentTurn.player.honorTotal + "", 370, 100);
		g2.drawString(currentTurn.rune + "", 585, 100);
		g2.drawString(currentTurn.power + "", 790, 100);
	}
	
	public void play() {

		this.theListener = new MouseListen(null);
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
			this.currentTurn = new Turn(players.get(0),this);
		} else {
			int i = players.indexOf(currentTurn.player);
			int n = (i + 1) % players.size();
			System.out.println(n);
			this.currentTurn = new Turn(players.get(n), this);
		}
		
		this.currentTurn.player.startingHand();
		this.theListener.setTurn(this.currentTurn);
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		frame.setSize(1620, 940);
		frame.setVisible(true);

		ArrayList<Card> notPlayed = DeckTest.randomCardList();
		ArrayList<Card> hand = new ArrayList<Card>();
		ArrayList<Card> discard = new ArrayList<Card>();
		
		Deck d = new Deck(notPlayed, hand, discard, getTopCards(),centerRow);
		d.drawNCards(6);
		
		ArrayList<Player> plays = new ArrayList<Player>();
		
		Game g = new Game(100,plays,d);
		g.image_back = new ImageIcon(g.getClass().getResource("Background.jpg")).getImage();
	
		g.players.add(Player.getNewPlayer("Jack"));
		g.players.add(Player.getNewPlayer("Gabe"));
		
		frame.add(g);

		frame.setVisible(true);
		frame.setSize(1621, 941);
		
		g.play();
		
	}

	private static ArrayList<Card> getTopCards() {
		ArrayList<Card> cards = new ArrayList<Card>();
		
		//Mystic
		ArrayList<Action> action1 = new ArrayList<Action>();
		action1.add(new Action(2, Action.ActionType.RuneBoost));
		cards.add(new Card(new Rectangle(1118, 27, 128, 166),Card.Type.Hero, Card.Faction.Enlightened, 3, action1,"Mystic"));
		
		//Heavy Infantry
		ArrayList<Action> action2 = new ArrayList<Action>();
		action2.add(new Action(2, Action.ActionType.PowerBoost));
		cards.add(new Card(new Rectangle(1277, 27, 128, 166),Card.Type.Hero, Card.Faction.Enlightened, 2, action2,"Heavy Infantry"));
		

		//Heavy Infantry
		ArrayList<Action> action3 = new ArrayList<Action>();
		action3.add(new Action(1, Action.ActionType.HonorBoost));
		cards.add(new Card(new Rectangle(1426, 27, 128, 166),Card.Type.Monster, Card.Faction.Enlightened, 2, action3,"Cultist"));
		
		return cards;
	}
}
