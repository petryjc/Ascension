import java.awt.Rectangle;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {

	private static Rectangle centerRow = new Rectangle(204, 253, 1168, 167);

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
					} else if (tokens[6 + (4 * i - 3)].equals("OptionalDeckBanish")) {
						actionType = Action.ActionType.OptionalDeckBanish;
					} else if (tokens[6 + (4 * i - 3)].equals("CenterBanish")) {
						actionType = Action.ActionType.CenterBanish;
					} else if (tokens[6 + (4 * i - 3)].equals("HonorAndRuneBoost")) {
						actionType = Action.ActionType.HonorAndRuneBoost;
					} else if (tokens[6 + (4 * i - 3)].equals("ConstructRuneBoost")) {
						actionType = Action.ActionType.ConstructRuneBoost;
					} else if (tokens[6 + (4 * i - 3)].equals("MechanaConstructRuneBoost")) {
						actionType = Action.ActionType.MechanaConstructRuneBoost;
					} else if (tokens[6 + (4 * i - 3)].equals("EnterAiyanaState")) {
						actionType = Action.ActionType.EnterAiyanaState;
					} 
					else {
						actionType = Action.ActionType.DrawCard;
					}
					int magnitude = Integer.parseInt(tokens[6 + (4 * i - 2)]);
					int dependency = Integer.parseInt(tokens[6 + (4 * i - 1)]);
					int onUnite =  Integer.parseInt(tokens[6 + (4 * i)]);
					Action action;
					if (onUnite == 1) {
						action = new Action(magnitude, actionType, dependency, true);
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
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return cards;
	}

	public static ArrayList<Card> getTopCards() {
		ArrayList<Card> cards = new ArrayList<Card>();

		// Mystic
		ArrayList<Action> action1 = new ArrayList<Action>();
		action1.add(new Action(2, Action.ActionType.RuneBoost));
		cards.add(new Card(new Rectangle(1118, 27, 128, 166), Card.Type.Hero,
				Card.Faction.Enlightened, 3, action1, "Mystic", 1));

		// Heavy Infantry
		ArrayList<Action> action2 = new ArrayList<Action>();
		action2.add(new Action(2, Action.ActionType.PowerBoost));
		cards.add(new Card(new Rectangle(1277, 27, 128, 166), Card.Type.Hero,
				Card.Faction.Enlightened, 2, action2, "Heavy Infantry", 1));

		// Cultist
		ArrayList<Action> action3 = new ArrayList<Action>();
		action3.add(new Action(1, Action.ActionType.HonorBoost));
		cards.add(new Card(new Rectangle(1426, 27, 128, 166),
				Card.Type.Monster, Card.Faction.Enlightened, 2, action3,
				"Cultist", 1));

		return cards;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ArrayList<Card> hand = new ArrayList<Card>();
		ArrayList<Card> discard = new ArrayList<Card>();

		ArrayList<Card> centerDeck = getCenterDeck("src/centerDeck.txt");

		Deck d = new Deck(centerDeck, hand, discard, getTopCards(), centerRow);
		d.shuffle();
		d.drawNCards(6);

		ArrayList<Player> plays = new ArrayList<Player>();

		Game g = new Game(100, plays, d);

		Scanner scan = new Scanner(System.in);
		System.out.println("What would you like the honor cap to be? ");
		int amountHonor = scan.nextInt();
		scan.close();
		JFrame frame = new JFrame();

		frame.setSize(1620, 940);
		frame.setVisible(true);

		g.gameHonor = amountHonor;
		
		g.country = "US";
		g.language = "en";

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
