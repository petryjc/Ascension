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
		

}
