import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Turn{

	Player player;
	int rune;
	int power;
	int constructRune;
	int mechanaConstructRune;
	int heroRune;
	int monsterPower;
	Game game;
	TurnState turnState;
	Boolean turnStateActionCompleted;
	int turnStateMagnitude;
	ArrayList<Action> actions;
	Boolean united;
	Boolean uniteOccurred;
	Action actionOnUnite;
	Boolean AiyanaState;
	IOptionPane optionPane;

	public Turn(Player player, Game g) {
		this.player = player;
		this.rune = 0;
		this.power = 0;
		this.game = g;
		this.heroRune = 0;
		this.monsterPower = 0;
		this.constructRune = 0;
		this.mechanaConstructRune = 0;
		this.turnState = TurnState.Default;
		actions = new ArrayList<Action>();
		this.uniteOccurred = false;
		this.united = false;
		this.AiyanaState = false;
		optionPane = new DefaultOptionPane();
		for (Card c : this.player.playerDeck.constructs) {
			executeCard(c);
		}

	}
	
	public void playAll() {
		//Continue playing cards until you a) run out b) discard c) banish
		while (!this.player.playerDeck.hand.isEmpty()) {
			Card c = this.player.playerDeck.hand.get(0);
			this.executeCard(c);
			this.player.playerDeck.playCard(c);
		}
	}

	public synchronized void exitActiveWaitingState() {
		this.turnState = TurnState.Default;
		notify();
	}
	
	public synchronized void decrementTurnStateMagnitude() {
		this.turnStateMagnitude--;
		if (this.turnStateMagnitude < 1) {
			exitActiveWaitingState();
		}
	}
	
	public synchronized void leftButtonClick(Point loc) {
		switch (turnState) {
		case Default:
			Rectangle end = new Rectangle(1460, 492, 91, 91);
			Rectangle playAllCardsInHand = new Rectangle(47, 493, 83, 100);
			if (end.contains(loc)) {
				this.player.playerDeck.endTurn();
				this.game.nextTurn();
				return;
			}
			if (playAllCardsInHand.contains(loc)) {
				playAll();
			}
			Card c = this.player.playerDeck.handleClick(loc);
			this.game.repaint();
			if (c != null) {
				executeCard(c);
				return;
			}
			if (attemptCardPurchaseWithinCardList(this.game.gameDeck.constructs, loc)) {
				return;
			}
			if (attemptCardPurchaseWithinCardList(this.game.gameDeck.hand, loc)) {
				return;
			}
			break;
		case Discard:
			if (this.player.playerDeck.attemptDiscard(loc)) {
				decrementTurnStateMagnitude();
			}
			break;
		case DeckBanish:
			Card banished = this.player.playerDeck.attemptDeckBanish(loc);
			if (banished != null) {
				decrementTurnStateMagnitude();
				this.game.gameDeck.discard.add(banished);
			}
			break;
		case CenterBanish:
			if(this.game.gameDeck.attemptCenterBanish(loc)) {
				decrementTurnStateMagnitude();
			}
			break;
		case DefeatMonster:
			Card defeatedMonster = this.game.gameDeck.attemptDefeatMonster(loc, this.turnStateMagnitude);
			if (defeatedMonster != null) {
				executeCard(defeatedMonster);
				exitActiveWaitingState();
			}
			break;
		default:
			break;
		}

	}
	
	public void executeCard(Card c) {
		if (this.united && c.getFaction() == Card.Faction.Lifebound
				&& !this.uniteOccurred) {
			executeAction(this.actionOnUnite);
			this.uniteOccurred = true;
		}
		for (Action a : c.getActions()) {
			this.turnStateActionCompleted = true;
			if (a.dependency < 0 ) { //Only execute actions that aren't dependent on others
				if(executeAction(a)) { //Execute all actions that depend upon this one
					for(Action a2 : c.getActions()) {
						if(a2.dependency == c.getActions().indexOf(a))
							executeAction(a2);
					}
				}
			}
		}
	}
	
	public synchronized boolean executeAction(Action a) {
		if (a.onUnite && !this.united) {
			this.actionOnUnite = a;
			this.united = true;
		}
		switch (a.action) {
		case HonorBoost:
			this.player.incrementHonor(a.magnitude);
			this.game.decrementHonor(a.magnitude);
			return true;
		case PowerBoost:
			this.power += a.magnitude;
			return true;
		case RuneBoost:
			this.rune += a.magnitude;
			return true;
		case DrawCard:
			player.playerDeck.drawNCards(a.magnitude);
			return true;
		case Discard:
			this.turnState = TurnState.Discard;
			this.turnStateMagnitude = a.magnitude;
			return true;
		case ForcedDeckBanish:
			optionPane.showMessageDialog(game,"Select " + a.magnitude + " card(s) from your deck to banish them","",
					JOptionPane.PLAIN_MESSAGE); 
			this.turnState = TurnState.DeckBanish;
			this.turnStateMagnitude = a.magnitude;
			return true;
		case CenterBanish:
			this.turnState = TurnState.CenterBanish;
			this.turnStateMagnitude = a.magnitude;
			return true;
		case OptionalDeckBanish:
			int n = optionPane.showConfirmDialog(game, "Would you like to banish " + a.magnitude + " card(s) from the deck?", 
					"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(n == JOptionPane.YES_OPTION) {
				this.turnState = TurnState.DeckBanish;
				this.turnStateMagnitude = a.magnitude;
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return true;
			}
			return false;
		case HonorAndRuneBoost:
			this.rune += a.magnitude;
			this.player.incrementHonor(a.magnitude);
			this.game.decrementHonor(a.magnitude);
			return true;
		case ConstructRuneBoost:
			this.constructRune += a.magnitude;
			return true;
		case MechanaConstructRuneBoost:
			this.mechanaConstructRune += a.magnitude;
			return true;
		case EnterAiyanaState:
			this.AiyanaState = true;
			return true;
		case HeroRuneBoost:
			this.heroRune += a.magnitude;
			return true;
		case MonsterPowerBoost:
			this.monsterPower += a.magnitude;
			return true;
		case DefeatMonster:
			if (!this.game.gameDeck.canAMonsterBeDefeated(a.magnitude)) {
				this.player.incrementHonor(1);
				this.game.decrementHonor(1);
			} else {
				this.turnState = TurnState.DefeatMonster;
				this.turnStateMagnitude = a.magnitude;
			}
			return true;
		}
		return false;
	}
	
	public boolean attemptCardPurchaseWithinCardList(ArrayList<Card> s, Point p) {
		for (Card c : s) {
			if (c.getLocation().contains(p)) {
				if (c.getType() == Card.Type.Monster) {
					if (this.monsterPower > 0 && c.getCost() < this.power + this.monsterPower) {
						for (int i = 0; i < c.getCost(); i++) {
							if (this.monsterPower > 0) {
								this.monsterPower--;
							} else {
								this.power--;
							}
						}
					} else if (c.getCost() <= this.power) {
						this.power -= c.getCost();
						executeCard(c);
						this.game.gameDeck.hand.remove(c);
						if (!c.getName().equals("Cultist")) {
							this.game.gameDeck.discard.add(c);
							this.game.gameDeck.drawCard();
						}
					}
				} else {
					if (c.getType() == Card.Type.Construct
							&& c.getFaction() == Card.Faction.Mechana
							&& c.getCost() <= (this.rune + this.constructRune + this.mechanaConstructRune)) {
						for (int i = 0; i < c.getCost(); i++) {
							if (this.mechanaConstructRune > 0) {
								this.mechanaConstructRune--;
							} else if (this.constructRune > 0) {
								this.constructRune--;
							} else {
								this.rune--;
							}
						}
						this.player.playerDeck.addNewCardToDiscard(new Card(c));
						if (this.game.gameDeck.hand.contains(c)) {
							this.game.gameDeck.hand.remove(c);
							this.game.gameDeck.drawCard();
						}
					} else if (c.getType() == Card.Type.Construct
							&& c.getCost() <= (this.rune + this.constructRune)) {
						for (int i = 0; i < c.getCost(); i++) {
							if (this.constructRune > 0) {
								this.constructRune--;
							} else {
								this.rune--;
							}
						}
						this.player.playerDeck.addNewCardToDiscard(new Card(c));
						if (this.game.gameDeck.hand.contains(c)) {
							this.game.gameDeck.hand.remove(c);
							this.game.gameDeck.drawCard();
						}
					} else if (this.AiyanaState && c.getType() == Card.Type.Hero && c.getHonorWorth() <= this.rune) {
						this.rune -= c.getHonorWorth();
						this.player.playerDeck.addNewCardToDiscard(new Card(c));
						if (this.game.gameDeck.hand.contains(c)) {
							this.game.gameDeck.hand.remove(c);
							this.game.gameDeck.drawCard();
						}
					} else if (this.heroRune > 0 && c.getType() == Card.Type.Hero && c.getCost() < this.rune + this.heroRune) {
						for (int i = 0; i < c.getCost(); i++) {
							if (this.heroRune > 0) {
								this.heroRune--;
							} else {
								this.rune--;
							}
						}
						this.player.playerDeck.addNewCardToDiscard(new Card(c));
						if (this.game.gameDeck.hand.contains(c)) {
							this.game.gameDeck.hand.remove(c);
							this.game.gameDeck.drawCard();
						}
					} else if (c.getCost() <= this.rune) {
						this.rune -= c.getCost();
						this.player.playerDeck.addNewCardToDiscard(new Card(c));
						if (this.game.gameDeck.hand.contains(c)) {
							this.game.gameDeck.hand.remove(c);
							this.game.gameDeck.drawCard();
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	public enum TurnState {
		Default, Discard, DeckBanish, CenterBanish, DefeatMonster
	}

}
