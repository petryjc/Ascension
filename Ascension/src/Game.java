import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class Game extends JComponent {
	

	int i;
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.BLACK);
		g2.fill(gameDeck.location);
		g2.draw(gameDeck.location);
	}

	
	public int totalHonor;
	public ArrayList<Player> players;
	public Deck gameDeck;
	
	Game(int honor, ArrayList<Player> players){
		this(honor, players, null);
	}
	
	Game(int honor, ArrayList<Player> players, Deck gameDeck){
		this.totalHonor = honor;
		this.players = players;
		this.gameDeck = gameDeck;
	}
	
	
	public void playTheGame(){

		JComponent panel = new JPanel();
		
		
		
//		while(this.totalHonor >= 0){
			
			
				Turn thisTurn = new Turn(players.get(0), this.gameDeck);
				
				thisTurn.interact(panel);
				
			
			
//		}
				while(true) {
					try{
						Thread.sleep(400);
						this.repaint();
					}
					catch(Exception e) {
						
					}
				}
					
		
		
	}
	
	public static void main(String[] args) {
		JFrame frame  = new JFrame();
		frame.setSize(1000, 1000);
		
		ArrayList<Player> pList = new ArrayList<Player>();
		Player p1 = new Player();
		p1.playerDeck = new PlayerDeck(new ArrayList<Card>());
		p1.playerDeck.location = new Rectangle(0,1000,1000,500);
		pList.add(p1);
		pList.add(new Player());
		Game g = new Game(100,pList);
		frame.add(g);
		frame.setVisible(true);
		if(g.getGraphics() == null)
			System.out.println("Double Fail");
		g.gameDeck = new Deck(new Rectangle(0,500,1000,1000));
		g.playTheGame();
		
		
	}
}
