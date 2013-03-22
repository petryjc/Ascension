import java.awt.Color;
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
	
	private static Rectangle handLoc = new Rectangle(182,670,1110,150);
	private static Rectangle playedLoc = new Rectangle(335,470,1050,150);
	private static Rectangle centerRow = new Rectangle(210,260,1070,150);
	private static Rectangle constructs = new Rectangle(60, 860, 1540, 895);
	
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
		
		
		Image image_card = new ImageIcon(this.getClass().getResource("BasicUser.jpg")).getImage();
		
		for(Player p:this.players){
			if(p.yourTurn){
				for(Card c:p.playerDeck.hand){
					g2.drawImage(image_card, c.getLocation().x,c.getLocation().y,c.getLocation().width,c.getLocation().height,null);
				}
				for(Card c:this.gameDeck.hand) {
					g2.drawImage(image_card, c.getLocation().x,c.getLocation().y,c.getLocation().width,c.getLocation().height,null);
				}
				for(Card c:p.playerDeck.played) {
					g2.drawImage(image_card, c.getLocation().x,c.getLocation().y,c.getLocation().width,c.getLocation().height,null);
				}
			}else{
				continue;
			}
		}
		
		
	
	
	}
	
	
	public void play() {


		while (this.totalHonor > 0) {
			try {
				
				for(int i = 0; i < this.players.size(); i++){
					
					Player current = this.players.get(i);
					
					current.playerDeck.drawNCards(5);
					
					current.yourTurn = true;
					
					Turn thisTurn = new Turn(current, this.gameDeck);
					
					
					this.addMouseListener(new MouseListen(thisTurn));
					
					this.repaint();
					
					while(current.yourTurn){
						
						Thread.sleep(50);
						this.repaint();
						
					}
					
				}
				
				Thread.sleep(400);
				this.repaint();
				
			} catch (Exception e) {
				System.out.println("Errored out.");
			}
		}
		
		

	}
	public void loadGame(){
		ArrayList<Card> start = new ArrayList<Card>();
		PlayerDeck startingDeck = new PlayerDeck(start, null, null, null);
		
		Player p1 = new Player(startingDeck);
		
		this.players.add(p1);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		frame.setSize(1600, 900);
		frame.setVisible(true);

		ArrayList<Card> notPlayed = DeckTest.randomCardList();
		ArrayList<Card> hand = DeckTest.randomCardList();
		ArrayList<Card> discard = DeckTest.randomCardList();
		
		Deck d = new Deck(notPlayed, hand, discard, centerRow);
		
		ArrayList<Card> notPlayed2 = DeckTest.randomCardList();
		ArrayList<Card> hand2 = DeckTest.randomCardList();
		ArrayList<Card> discard2 = DeckTest.randomCardList();
		
		PlayerDeck pD = new PlayerDeck(notPlayed2, hand2, discard2, handLoc, playedLoc, constructs);
		
		ArrayList<Player> plays = new ArrayList<Player>();
		
		Game g = new Game(100,plays,d);
		
	
		g.players.add(new Player(pD));
		
		frame.add(g);
		
		g.play();
		
		g.addMouseListener(g.theListener);
		
		
		
		frame.setVisible(true);
		
	}
}
