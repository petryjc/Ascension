import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Game extends JComponent {

	public int gameHonor;
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
		this.gameHonor = honor;
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
		g2.drawString(this.gameHonor + "", 370, 100);
		g2.drawString(currentTurn.rune + "", 585, 100);
		g2.drawString(currentTurn.power + "", 790, 100);
	}
	
	public void play() {

		this.theListener = new MouseListen(null);
		this.addMouseListener(this.theListener);

		nextTurn();
		
		
		
		while (this.gameHonor > 0) {
			try {
				Thread.sleep(50);
				repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}
		int highestAmount = 0;
		ArrayList<String> winners = new ArrayList<String>();
		
		for(Player p:this.players){
			p.playerDeck.discard.addAll(p.playerDeck.notPlayed);
			p.playerDeck.notPlayed.clear();
			p.playerDeck.discard.addAll(p.playerDeck.played);
			p.playerDeck.played.clear();
			p.playerDeck.discard.addAll(p.playerDeck.hand);
			p.playerDeck.hand.clear();
			p.playerDeck.discard.addAll(p.playerDeck.constructs);
			p.playerDeck.constructs.clear();
			
			for(Card c:p.playerDeck.discard){
				p.honorTotal += c.getHonorWorth();
			}
			
			if(p.honorTotal > highestAmount){
				highestAmount = p.honorTotal;
				winners.clear();
				winners.add(p.name);
			}else if (p.honorTotal == highestAmount ){
				for(String s:winners){
					if(s.equals("none")){
						winners.add(p.name);
					}else{
						continue;
					}
				}
			}
		}
		
		if(winners.size() == 1){
			System.out.println(winners.get(0) + " WINS!!!!!!!");
		}else{
			for(String s:winners){
				System.out.println(s + " ");
			}
			System.out.println("have tied");
		}

	}
	
	public void decrementHonor(int n) {
		this.gameHonor -= n;
	}
	
	public void nextTurn() {
		if(currentTurn == null) {
			this.currentTurn = new Turn(players.get(0),this);
		} else {
			int i = players.indexOf(currentTurn.player);
			int n = (i + 1) % players.size();
			this.currentTurn = new Turn(players.get(n), this);
		}
		
		this.currentTurn.player.startingHand();
		this.theListener.setTurn(this.currentTurn);
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
	
	public static ArrayList<Card> getCenterDeck(String filename) {
		ArrayList<Card> cards = new ArrayList<Card>();
		
		File file = new File(filename);
		try {
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				String cardInfo = scanner.nextLine();
				String[] tokens = cardInfo.split(" ");
				String name = tokens[0];
				Card.Type type;
				if (tokens[2].equals("Hero")) {
					type = Card.Type.Hero;
				} else if (tokens[2].equals("Construct")) {
					type = Card.Type.Construct;
				} else {
					type = Card.Type.Monster;
				}
				Card.Faction faction;
				if (tokens[3].equals("Enlightened")) {
					faction = Card.Faction.Enlightened;
				} else if (tokens[3].equals("Void")) {
					faction = Card.Faction.Void;
				} else if (tokens[3].equals("Lifebound")) {
					faction = Card.Faction.Lifebound;
				} else {
					faction = Card.Faction.Mechana;
				}
				int cost = Integer.parseInt(tokens[4]);
				ArrayList<Action> actions = new ArrayList<Action>();
				for (int i = 1; i <= Integer.parseInt(tokens[5]); i++) {
					Action.ActionType actionType;
					if (tokens[5+(2*i-1)].equals("RuneBoost")) {
						actionType = Action.ActionType.RuneBoost;
					} else if (tokens[5+(2*i-1)].equals("PowerBoost")) {
						actionType = Action.ActionType.PowerBoost;
					} else if (tokens[5+(2*i-1)].equals("HonorBoost")) {
						actionType = Action.ActionType.HonorBoost;
					} else {
						actionType = Action.ActionType.DrawCard;
					}
					int magnitude = Integer.parseInt(tokens[5 + (2*i)]);
					Action action = new Action(magnitude, actionType);
					actions.add(action);
				}
				for (int i = 0; i < Integer.parseInt(tokens[1]); i++) {
					Card card = new Card(type, faction, cost, actions, name);
					cards.add(card);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return cards;
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		frame.setSize(1620, 940);
		frame.setVisible(true);
		
		ArrayList<Card> hand = new ArrayList<Card>();
		ArrayList<Card> discard = new ArrayList<Card>();
		
		ArrayList<Card> centerDeck = getCenterDeck("src/centerDeck.txt");
		
		Deck d = new Deck(centerDeck, hand, discard, getTopCards(),centerRow);
		d.shuffle();
		d.drawNCards(6);
		
		ArrayList<Player> plays = new ArrayList<Player>();
		
		Game g = new Game(5,plays,d);
		g.image_back = new ImageIcon(g.getClass().getResource("Background.jpg")).getImage();
		
		g.players.add(Player.getNewPlayer("Jack"));
		g.players.add(Player.getNewPlayer("Gabe"));
		g.players.add(Player.getNewPlayer("Kenny"));
		
		frame.add(g);
		
		frame.setVisible(true);
		frame.setSize(1621, 941);
		
		g.play();
		
		frame.dispose();
		
	}
}
