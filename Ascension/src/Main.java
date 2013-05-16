import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;

public class Main {

	private static Rectangle centerRow = new Rectangle(204, 253, 1168, 167);

	public static ArrayList<Card> getCenterDeck(String filename) {
		ArrayList<Card> cards = new ArrayList<Card>();

		File file = new File(filename);
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			// Clean out the first line, used as comments to explain process
			scanner.nextLine();
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
				for (int i = 1; i <= Integer.parseInt(tokens[6]); i++) {
					Action.ActionType actionType;
					if (tokens[6 + (4 * i - 3)].equals("RuneBoost")) {
						actionType = Action.ActionType.RuneBoost;
					} else if (tokens[6 + (4 * i - 3)].equals("PowerBoost")) {
						actionType = Action.ActionType.PowerBoost;
					} else if (tokens[6 + (4 * i - 3)].equals("HonorBoost")) {
						actionType = Action.ActionType.HonorBoost;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("ForcedDeckBanish")) {
						actionType = Action.ActionType.ForcedDeckBanish;
					} else if (tokens[6 + (4 * i - 3)].equals("Discard")) {
						actionType = Action.ActionType.Discard;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("OptionalDeckBanish")) {
						actionType = Action.ActionType.OptionalDeckBanish;
					} else if (tokens[6 + (4 * i - 3)].equals("CenterBanish")) {
						actionType = Action.ActionType.CenterBanish;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("HonorAndRuneBoost")) {
						actionType = Action.ActionType.HonorAndRuneBoost;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("ConstructRuneBoost")) {
						actionType = Action.ActionType.ConstructRuneBoost;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("MechanaConstructRuneBoost")) {
						actionType = Action.ActionType.MechanaConstructRuneBoost;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("EnterAiyanaState")) {
						actionType = Action.ActionType.EnterAiyanaState;
					} else if (tokens[6 + (4 * i - 3)].equals("HeroRuneBoost")) {
						actionType = Action.ActionType.HeroRuneBoost;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("MonsterPowerBoost")) {
						actionType = Action.ActionType.MonsterPowerBoost;
					} else if (tokens[6 + (4 * i - 3)].equals("DefeatMonster")) {
						actionType = Action.ActionType.DefeatMonster;
					} else if (tokens[6 + (4 * i - 3)].equals("DrawCard")) {
						actionType = Action.ActionType.DrawCard;
					} else if (tokens[6 + (4 * i - 3)].equals("FreeCard")) {
						actionType = Action.ActionType.FreeCard;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("EnterVoidMesmer")) {
						actionType = Action.ActionType.EnterVoidMesmer;
					} else if (tokens[6 + (4 * i - 3)].equals("HandBanish")) {
						actionType = Action.ActionType.HandBanish;
					} else if (tokens[6 + (4 * i - 3)].equals("HeavyOrMystic")) {
						actionType = Action.ActionType.HeavyOrMystic;
					} else if (tokens[6 + (4 * i - 3)].equals("LunarStag")) {
						actionType = Action.ActionType.LunarStag;
					} else if (tokens[6 + (4 * i - 3)].equals("AskaraOfFate")) {
						actionType = Action.ActionType.AskaraOfFate;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("AskaraCenterBanish")) {
						actionType = Action.ActionType.AskaraCenterBanish;
					} else if (tokens[6 + (4 * i - 3)].equals("NookHound")) {
						actionType = Action.ActionType.NookHound;
					} else if (tokens[6 + (4 * i - 3)].equals("AskaraDiscard")) {
						actionType = Action.ActionType.AskaraDiscard;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("TwofoldAskaraPlayed")) {
						actionType = Action.ActionType.TwofoldAskaraPlayed;
					} else if (tokens[6 + (4 * i - 3)].equals("RajAction")) {
						actionType = Action.ActionType.RajAction;
					} else if (tokens[6 + (4 * i - 3)].equals("CetraAction")) {
						actionType = Action.ActionType.CetraAction;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("TabletOfTimesDawn")) {
						actionType = Action.ActionType.TabletOfTimesDawn;
					} else if (tokens[6 + (4 * i - 3)].equals("YggdrasilStaff")) {
						actionType = Action.ActionType.YggdrasilStaff;
					} else if (tokens[6 + (4 * i - 3)].equals("AvatarGolem")) {
						actionType = Action.ActionType.AvatarGolem;
					} else if (tokens[6 + (4 * i - 3)].equals("KorAction")) {
						actionType = Action.ActionType.KorAction;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("MechanaInitiate")) {
						actionType = Action.ActionType.MechanaInitiate;
					} else if (tokens[6 + (4 * i - 3)].equals("HedronCannon")) {
						actionType = Action.ActionType.HedronCannon;
					} else if (tokens[6 + (4 * i - 3)].equals("Voidthirster")) {
						actionType = Action.ActionType.Voidthirster;
					} else if (tokens[6 + (4 * i - 3)].equals("XeronAction")) {
						actionType = Action.ActionType.XeronAction;

					} else if (tokens[6 + (4 * i - 3)]
							.equals("SeaTyrantAction")) {
						actionType = Action.ActionType.SeaTyrantAction;

					} else if (tokens[6 + (4 * i - 3)].equals("RocketCourier")) {
						actionType = Action.ActionType.RocketCourier;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("HedronLinkDevice")) {
						actionType = Action.ActionType.HedronLinkDevice;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("CorrosiveWidowAction")) {
						actionType = Action.ActionType.CorrosiveWidowAction;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("HeroTopOfDeck")) {
						actionType = Action.ActionType.HeroTopOfDeck;
					} else if (tokens[6 + (4 * i - 3)]
							.equals("OptionalDiscard")) {
						actionType = Action.ActionType.OptionalDiscard;
					} else {
						throw new UnsupportedOperationException(
								"Unrecognized token name: "
										+ tokens[6 + (4 * i - 3)]);
					}
					int magnitude = Integer.parseInt(tokens[6 + (4 * i - 2)]);
					int dependency = Integer.parseInt(tokens[6 + (4 * i - 1)]);
					int onUnite = Integer.parseInt(tokens[6 + (4 * i)]);
					Action action;
					if (onUnite == 1) {
						action = new Action(magnitude, actionType, dependency,
								true);
					} else {
						action = new Action(magnitude, actionType, dependency);
					}
					actions.add(action);
				}
				int honorWorth = Integer.parseInt(tokens[5]);
				for (int i = 0; i < Integer.parseInt(tokens[1]); i++) {
					Card card = new Card(type, faction, cost, actions, name,
							honorWorth);
					cards.add(card);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if(scanner != null)
				scanner.close();
		}

		return cards;
	}

	public static Card getMystic() {
		// Mystic
		ArrayList<Action> action1 = new ArrayList<Action>();
		action1.add(new Action(2, Action.ActionType.RuneBoost));
		return new Card(new Rectangle(1118, 27, 128, 166), Card.Type.Hero,
				Card.Faction.Common, 3, action1, "Mystic", 1);
	}

	public static Card getHeavyInfantry() {
		ArrayList<Action> action2 = new ArrayList<Action>();
		action2.add(new Action(2, Action.ActionType.PowerBoost));
		return new Card(new Rectangle(1277, 27, 128, 166), Card.Type.Hero,
				Card.Faction.Common, 2, action2, "Heavy_Infantry", 1);
	}

	public static ArrayList<Card> getTopCards() {
		ArrayList<Card> cards = new ArrayList<Card>();

		cards.add(getMystic());

		cards.add(getHeavyInfantry());

		// Cultist
		ArrayList<Action> action3 = new ArrayList<Action>();
		action3.add(new Action(1, Action.ActionType.HonorBoost));
		cards.add(new Card(new Rectangle(1426, 27, 128, 166),
				Card.Type.Monster, Card.Faction.Common, 2, action3, "Cultist",
				1));

		return cards;
	}


	static Game game;
	static boolean inMenu;
	static IOptionPane optionPane = new DefaultOptionPane();
	
	public static JComponent menuComp(){
		
		final JLabel playerNames = new JLabel("Enter Player Names: ");
		final JLabel honorLab = new JLabel("Enter Total Honor");
		final JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(5,2));
		final JButton start = new JButton("Start");
		
		JPanel p1 = new JPanel();
		final JTextField name1 = new JTextField("", 20);
		p1.setSize(100,150);
		p1.add(name1);
		
		JPanel p2 = new JPanel();
		final JTextField name2 = new JTextField("", 20);
		p2.setSize(100,150);
		p2.add(name2);
		
		JPanel p3 = new JPanel();
		final JTextField name3 = new JTextField("", 20);
		p3.setSize(100,150);
		p3.add(name3);
		
		JPanel p4 = new JPanel();
		final JTextField name4 = new JTextField("", 20);
		p4.setSize(100,150);
		p4.add(name4);
		
		JPanel p5 = new JPanel();
		JButton but1 = new JButton("English");
		JButton but2 = new JButton("\uD55C\uAD6D\uC758");
		JButton but3 = new JButton("Español");
		p5.setSize(100,150);
		but1.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent _ev)
	        {
	        	game.descriptions = ResourceBundle.getBundle(
	    				"CardDescription", new Locale("en", "EN"));
	        	start.setText(game.descriptions.getString("Start"));
	        	playerNames.setText(game.descriptions.getString("MenuPlayHeader") + ":");
	        	honorLab.setText(game.descriptions.getString("Enter"));
	        }
	    });
		
		but2.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent _ev)
	        {
	        	game.descriptions = ResourceBundle.getBundle(
	    				"CardDescription", new Locale("kr", "KR"));
	        	start.setText(game.descriptions.getString("Start"));
	        	playerNames.setText(game.descriptions.getString("MenuPlayHeader") + ":");
	        	honorLab.setText(game.descriptions.getString("Enter"));
	        }
	    });
		
		but3.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent _ev)
	        {
	        	game.descriptions = ResourceBundle.getBundle(
	    				"CardDescription", new Locale("sp", "SP"));
	        	start.setText(game.descriptions.getString("Start"));
	        	playerNames.setText(game.descriptions.getString("MenuPlayHeader") + ":");
	        	honorLab.setText(game.descriptions.getString("Enter"));
	        }
	    });
		p5.add(but1);
		p5.add(but2);
		p5.add(but3);

		JPanel p6 = new JPanel();
		final JTextField honor = new JTextField("", 10);
		p6.setSize(100,150);
		start.addActionListener(new ActionListener()
	    {
	        public void actionPerformed(ActionEvent _ev)
	        {
	        	if(name1.getText().equals("") || name2.getText().equals("")){
	        		optionPane.showMessageDialog(pan, game.descriptions.getString("PlayerWarn"), "", JOptionPane.PLAIN_MESSAGE);
        		} else {
        			ArrayList<Player> pList = new ArrayList<Player>();
        			pList.add(Player.getNewPlayer(name1.getText()));
        			pList.add(Player.getNewPlayer(name2.getText()));
        			if(!name3.getText().equals("")){
        				pList.add(Player.getNewPlayer(name3.getText()));
        			}
        			if(!name4.getText().equals("")){
        				pList.add(Player.getNewPlayer(name4.getText()));
        			}
        			game.players = pList;
        			try{
        				game.gameHonor =Integer.parseInt(honor.getText());
        				if(game.gameHonor > 0 ){
        					inMenu = false;
        				}else{
        					optionPane.showMessageDialog(pan, game.descriptions.getString("HonorWarning"), "", JOptionPane.PLAIN_MESSAGE);
        				}
        			}catch (NumberFormatException e){
        				optionPane.showMessageDialog(pan, game.descriptions.getString("HonorWarning"), "", JOptionPane.PLAIN_MESSAGE);
        			}
        				
        		}
	        }
	    });
		
		p6.add(honorLab, BorderLayout.WEST);
		p6.add(honor);
		JPanel p7 = new JPanel();
		p7.setSize(100,150);
		p7.add(start);
		
		
		JPanel blank1 = new JPanel();
		blank1.setSize(100,150);
		JPanel blank2 = new JPanel();
		blank2.setSize(100,150);
		
		pan.add(p5);
		pan.add(blank1);
		pan.add(playerNames);
		pan.add(blank2);
		pan.add(p1);
		pan.add(p2);
		pan.add(p3);
		pan.add(p4);
		pan.add(p6);
		pan.add(p7);
		
		return pan;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame frame = new JFrame("Main Menu");
		frame.setVisible(true);
		frame.setSize(510, 300);
		
		
		JComponent menu = menuComp();
		frame.add(menu);
		
		frame.pack();
		
		ArrayList<Card> hand = new ArrayList<Card>();
		ArrayList<Card> discard = new ArrayList<Card>();
		ArrayList<Card> centerDeck = getCenterDeck("src/centerDeck.txt");

		Deck d = new Deck(centerDeck, hand, discard, getTopCards(), centerRow);
		d.shuffle();
		d.drawNCards(6);
		game = new Game(10, new ArrayList<Player>(), d);
		game.descriptions = ResourceBundle.getBundle(
				"CardDescription", new Locale("en", "EN"));

		inMenu = true;
		while(inMenu){
			menu.repaint();			
		}
		
		frame.dispose();
		
		
		JFrame gameframe = new JFrame("Ascension");
		
		gameframe.setSize(1620, 940);
		gameframe.setVisible(true);
		gameframe.add(game);

		game.play();

		gameframe.dispose();

	}

}
