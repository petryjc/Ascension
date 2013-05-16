import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

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
	public boolean isTest = false;
	Image image_back;
	Image card_back;
	Image honor_symbol;
	Image rune_symbol;
	Image power_symbol;
	public boolean playing;
	public boolean extraTurn;
	public Player firstPlayer;
	boolean firstTurn;
	JFrame discardFrame;
	IOptionPane optionpane;
	
	ResourceBundle descriptions;

	public static Rectangle handLoc = new Rectangle(184, 670, 1224, 160);
	public static Rectangle playedLoc = new Rectangle(184, 460, 1224, 161);
	public static Rectangle constructs = new Rectangle(52, 856, 1495, 160);

	Game(int honor) {
		this(honor, null, null);
	}

	Game(int honor, ArrayList<Player> players, Deck gameDeck) {
		this.gameHonor = honor;
		this.players = players;
		this.gameDeck = gameDeck;
		this.extraTurn = false;
		this.optionpane = new DefaultOptionPane();

		this.theListener = new MouseListen(null);
		this.addMouseListener(this.theListener);

		this.image_back = new ImageIcon(this.getClass().getResource(
				"Background.jpg")).getImage();
		this.card_back = new ImageIcon(this.getClass().getResource(
				"CardBackground.jpg")).getImage();
		this.power_symbol = new ImageIcon(this.getClass().getResource(
				"PowerSymbol.jpg")).getImage();
		this.rune_symbol = new ImageIcon(this.getClass().getResource(
				"RuneSymbol.jpg")).getImage();
		this.honor_symbol = new ImageIcon(this.getClass().getResource(
				"HonorSymbol.jpg")).getImage();

		this.playing = true;
		this.firstTurn = true;

		this.discardFrame = new JFrame();
		this.discardFrame.setLocation(100, 300);
		this.discardFrame.setSize(1420, 300);
		this.discardFrame.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				discardFrame.setVisible(false);
				DeckRender
						.nullOutCardLocation(currentTurn.player.playerDeck.discard);
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		JComponent comp = new JComponent() {
			@Override
			protected void paintComponent(Graphics g) {
				Graphics2D g2 = (Graphics2D) g;
				DeckRender.setCardListWithinLocation(
						currentTurn.player.playerDeck.discard, new Rectangle(
								100, 50, 1220, 200));
				for (Card c : currentTurn.player.playerDeck.discard) {
					paintCard(c, g2);
				}
			}
		};
		comp.addMouseListener(this.theListener);
		this.discardFrame.add(comp);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		g2.drawImage(image_back, 0, 0, 1600, 900, null);

		// draw the four visible card sets
		if (this.gameDeck.hand == null || this.currentTurn == null
				|| this.currentTurn.player.playerDeck == null) {
			return;
		}
		for (Card c : this.gameDeck.hand) {
			paintCard(c, g2);
		}
		for (Card c : this.currentTurn.player.playerDeck.played) {
			paintCard(c, g2);
		}
		for (Card c : this.currentTurn.player.playerDeck.constructs) {
			paintCard(c, g2);
		}
		for (Card c : this.currentTurn.player.playerDeck.hand) {
			paintCard(c, g2);
		}

		// Draw the table with players and their current honor
		g2.setFont(new Font("TimesNewRoman", 30, 20));
		g2.drawString(currentTurn.player.name, 5, 0 + 28);
		g2.drawString(currentTurn.player.honorTotal + "", 170, 28);
		this.players.remove(currentTurn.player);
		for (int i = 0; i < this.players.size(); i++) {
			Rectangle r = new Rectangle(0, (i + 1) * 30, 200, 30);
			g2.draw(r);
			g2.drawString(players.get(i).name, 5, (i + 1) * 30 + 28);
			g2.drawString(players.get(i).honorTotal + "", 170,
					(i + 1) * 30 + 28);
		}
		this.players.add(this.players.size(), currentTurn.player);
		if (this.firstTurn) {
			this.firstTurn = false;
			this.firstPlayer = this.players.get(this.players.size() - 1);
		}
		// Draw the additional game state info
		g2.setFont(new Font("TimesNewRoman", 30, 50));
		g2.drawString(this.gameHonor + "", 370, 100);
		g2.drawString(currentTurn.rune + "", 585, 100);
		g2.drawString(currentTurn.power + "", 790, 100);

		if (this.discardFrame.isVisible()) {
			this.discardFrame.getContentPane().repaint();
		}
		
		//Draw the text over the buttons and stuff that was a part of the background
		g2.setFont(new Font("TimesNewRoman", 20, 20));
		printSimpleString(descriptions.getString("Portal"), 160, 25, 330,g2);
		printSimpleString(descriptions.getString("Void"), 160, 1400, 330, g2);
		printSimpleString(descriptions.getString("Play_All"), 95, 45, 565, g2);
		printSimpleString(descriptions.getString("End_Turn"), 95, 1462, 565, g2);
		printSimpleString(descriptions.getString("Deck"), 140, 32, 765, g2);
		printSimpleString(descriptions.getString("Discard"), 140, 1430, 765, g2);
	}
	
	private void printSimpleString(String s, int width, int XPos, int YPos, Graphics2D g2d){  
        int stringLen = (int)  
            g2d.getFontMetrics().getStringBounds(s, g2d).getWidth();  
        int start = width/2 - stringLen/2;  
        g2d.drawString(s, start + XPos, YPos);  
	} 

	protected void paintCard(Card c, Graphics2D g2) {
		Rectangle loc = c.getLocation();
		if (loc == null)
			return;
		// draw background
		g2.drawImage(card_back, loc.x, loc.y, loc.width, loc.height, null);

		// draw cost
		g2.setFont(new Font("TimesNewRoman", 10, 10));
		if (c.getType() == Card.Type.Monster) {
			g2.drawImage(power_symbol, (int) (loc.x + loc.width * 0.85),
					(int) (loc.y + loc.height * 0.03),
					(int) (loc.width * 0.15), (int) (loc.height * 0.07), null);
			g2.setColor(Color.white);
		} else {
			g2.drawImage(rune_symbol, (int) (loc.x + loc.width * 0.85),
					(int) (loc.y + loc.height * 0.03),
					(int) (loc.width * 0.15), (int) (loc.height * 0.07), null);
		}
		g2.drawString(c.getCost() + "", (int) (loc.x + loc.width * 0.89),
				(int) (loc.y + loc.height * 0.09));
		g2.setColor(Color.black);

		// draw name
		String name = c.getName().replace('_', ' ');
		g2.setFont(scaleFont(name, new Rectangle((int) (loc.width * 0.8),
				(int) (loc.height * 0.1)), (Graphics) g2));
		g2.drawString(name, (int) (loc.x + loc.width * 0.05),
				(int) (loc.y + loc.height * 0.10));

		// draw card image
		g2.drawImage(c.getImage(), (int) (loc.x + loc.width * 0.05),
				(int) (loc.y + loc.height * 0.13), (int) (loc.width * 0.9),
				(int) (loc.height * 0.35), null);

		// draw type/faction
		String factiontype = c.getFaction() + " " + c.getType();
		g2.setFont(scaleFont(factiontype, new Rectangle(
				(int) (loc.width * 0.9), (int) (loc.height * 0.08)),
				(Graphics) g2));
		g2.drawString(factiontype, (int) (loc.x + loc.width * 0.05),
				(int) (loc.y + loc.height * 0.56));

		// Description rectangle
		Rectangle r = new Rectangle((int) (loc.x + loc.width * 0.05),
				(int) (loc.y + loc.height * 0.58), (int) (loc.width * 0.9),
				(int) (loc.height * 0.40));
		g2.setColor(Color.LIGHT_GRAY);
		g2.fill(r);

		// draw honor
		g2.setFont(new Font("TimesNewRoman", 10, 10));
		g2.setColor(Color.red);
		g2.drawImage(honor_symbol, (int) (loc.x),
				(int) (loc.y + loc.height * 0.92), (int) (loc.width * 0.17),
				(int) (loc.height * 0.09), null);
		g2.drawString(c.getHonorWorth() + "", (int) (loc.x + loc.width * 0.06),
				(int) (loc.y + loc.height * 0.99));
		g2.setColor(Color.black);

		// draw card description
		try {
			g2.setColor(Color.black);
			g2.setFont(new Font("TimesNewRoman", 10, 10));
			ArrayList<String> lines = getTextInLines(
					descriptions.getString(c.getName()),
					(int) (loc.width * 0.9), g2);
			for (int i = 0; i < lines.size(); i++) {
				g2.drawString(lines.get(i), (int) (loc.x + loc.width * 0.06),
						(int) (loc.y + loc.height * 0.56 + g2.getFont()
								.getSize() * (i + 1)));
			}
		} catch (Exception e) {
			// System.out.println("Missing card reference of " + c.getName() +
			// " in CardDescription");
		}

	}

	public Font scaleFont(String text, Rectangle2D rect, Graphics g) {
		float nextTry = 18.0f;
		Font font = new Font("TimesNewRoman", 0, 20);

		while (nextTry > 2) {
			font = g.getFont().deriveFont(nextTry);
			FontMetrics fm = g.getFontMetrics(font);
			int width = fm.stringWidth(text);
			if (width <= rect.getWidth() && nextTry <= rect.getHeight())
				return font;
			nextTry *= .9;
		}
		return font;
	}

	public ArrayList<String> getTextInLines(String s, int width, Graphics g) {
		ArrayList<String> a = new ArrayList<String>();
		String[] temp = s.split(" ");
		a.add(temp[0]);
		Font font = g.getFont();
		for (int i = 1; i < temp.length; i++) {
			String ts = a.get(a.size() - 1) + " " + temp[i];
			FontMetrics fm = g.getFontMetrics(font);
			int swidth = fm.stringWidth(ts);
			if (swidth < width) {
				a.remove(a.size() - 1);
				a.add(ts);
			} else {
				a.add(temp[i]);
			}
		}
		return a;
	}

	public void play() {
		for (Player player : this.players) {
			player.startingHand();
		}
		nextTurn();

		while (this.playing) {
			try {
				if (!this.isTest) {
					Thread.sleep(50);
				} else {
					decrementHonor(1);
				}
				repaint();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	public void decrementHonor(int n) {
		this.gameHonor -= n;
	}

	public void nextTurn() {
		if (this.extraTurn) {
			ArrayList<Player> tempPlayerArray = new ArrayList<Player>();
			tempPlayerArray.add(this.players.get(this.players.size() - 1));
			for (int i = 0; i < this.players.size() - 1; i++) {
				tempPlayerArray.add(this.players.get(i));
			}
			this.players = tempPlayerArray;
			this.extraTurn = false;
		}
		if (this.players.get(0).equals(this.firstPlayer) && this.gameHonor <= 0) {
			this.playing = false;
			endGame();
		}
		this.currentTurn = new Turn(this.players.get(0), this);
		this.theListener.setTurn(this.currentTurn);
	}

	public String endGame() {

		int highestAmount = 0;
		ArrayList<String> winners = new ArrayList<String>();

		for (Player p : this.players) {
			System.out.println(p.name);

			p.playerDeck.discard.addAll(p.playerDeck.notPlayed);
			p.playerDeck.notPlayed.clear();
			p.playerDeck.discard.addAll(p.playerDeck.played);
			p.playerDeck.played.clear();
			p.playerDeck.discard.addAll(p.playerDeck.hand);
			p.playerDeck.hand.clear();
			p.playerDeck.discard.addAll(p.playerDeck.constructs);
			p.playerDeck.constructs.clear();

			for (Card c : p.playerDeck.discard) {
				p.honorTotal += c.getHonorWorth();
				System.out.println(c.getName() + ": " + c.getHonorWorth());
			}

			System.out.println("Total Honor for " + p.name + ": "
					+ p.honorTotal);

		}
		int numOfPlayers = this.players.size();
		for (int i = 0; i < numOfPlayers; i++) {
			Player current = this.players.get(i);
			if (current.honorTotal > highestAmount) {
				winners.clear();
				highestAmount = current.honorTotal;
				winners.add(current.name);
			} else if (current.honorTotal == highestAmount) {
				winners.add(current.name);
			} else {
				continue;
			}
		}

		if (winners.size() == 1) {
			System.out.println(winners.get(0) + " wins");
			return winners.get(0) + " wins";
		} else {
			String save = "";
			for (String s : winners) {
				System.out.print(s + ", ");
				save += s;
			}
			System.out.print("have tied");
			return save + " have tied";
		}

	}

}
