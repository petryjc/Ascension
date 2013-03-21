import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Game extends JFrame {
	
	private int totalHonor;
	private ArrayList<Player> players;
	private Deck gameDeck;
	
	Game(int honor, ArrayList<Player> players, Deck gameDeck){
		this.totalHonor = honor;
		this.players = players;
		this.gameDeck = gameDeck;
	}
	
	
	public JComponent playTheGame(){

		JComponent panel = new JPanel();
		
		
//		while(this.totalHonor >= 0){
			
			
				Turn thisTurn = new Turn(players.get(0), this.gameDeck);
				
				thisTurn.interact(panel);
				
				return panel;
			
			
//		}
		
		
	}
	
	public static void main(String[] args) {
		JFrame frame  = new JFrame();
		
		ArrayList<Player> pList = new ArrayList<Player>();
		pList.add(new Player());
		pList.add(new Player());
		Game g = new Game(100,pList,new Deck());
		frame.add(g.playTheGame());
		
		
		frame.setVisible(true);
		

//		
//		JComponent panel = new JPanel();
//		
//		//Turn test = new Turn(tester, this.ga);
//		
//		JFrame frame  = new JFrame();
//		//frame.add(test.interact(panel));
//		frame.setVisible(true);
	}
}
