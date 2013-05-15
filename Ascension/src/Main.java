import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Main {

	public static boolean waiting;
	public static String s1;
	public static String s2;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		IOptionPane opt = new DefaultOptionPane();

		ArrayList<Card> hand = new ArrayList<Card>();
		ArrayList<Card> discard = new ArrayList<Card>();

		ArrayList<Card> centerDeck = getCenterDeck("src/centerDeck.txt");

		Deck d = new Deck(centerDeck, hand, discard, getTopCards(), centerRow);

		d.shuffle();
		d.drawNCards(6);

		ArrayList<Player> plays = new ArrayList<Player>();

		Game g = new Game(100, plays, d);

		Object[] objects = { "English", "Espanol", "Korean" };

		int choice = opt.showOptionDialog(g, "Pick a Language", "",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
				objects);

		if (choice == JOptionPane.NO_OPTION) {
			g.country = "SP";
			g.language = "sp";
		} else if (choice == JOptionPane.CANCEL_OPTION) {
			g.country = "KR";
			g.language = "kr";
		} else {
			g.country = "US";
			g.language = "en";
		}

		String amount;

		while ((amount = JOptionPane.showInputDialog(g,
				"Enter the amount of honor you want?")) == null
				|| !amount.matches("^-?[0-9]+(\\.[0-9]+)?$"))
			;
		g.gameHonor = Integer.parseInt(amount);

		JFrame frame = new JFrame();

		frame.setSize(1620, 940);
		frame.setVisible(true);

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
