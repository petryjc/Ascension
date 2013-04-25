import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JOptionPane;


public class Turn {

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
	ArrayList<Boolean> completedActions;
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
			executeCardAction(c);
		}

	}
	
	public void playAll() {
		//Continue playing cards until you a) run out b) discard c) banish
		while (!this.player.playerDeck.hand.isEmpty()) {
			Card c = this.player.playerDeck.hand.get(0);
			this.executeCardAction(c);
			this.player.playerDeck.playCard(c);
			if(this.turnState == TurnState.Discard || 
					this.turnState == TurnState.DeckBanish)
				return;	
		}
	}

	public void leftButtonClick(Point loc) {
		switch (turnState) {
		case Default:
			completedActions = new ArrayList<Boolean>();
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
				executeCardAction(c);
				return;
			}
			if (staticCardList(this.game.gameDeck.constructs, loc)) {
				return;
			}
			if (staticCardList(this.game.gameDeck.hand, loc)) {
				return;
			}
			break;
		case Discard:
			if (this.player.playerDeck.attemptDiscard(loc)) {
				this.turnStateMagnitude--;
				if (this.turnStateMagnitude < 1) {
					this.turnState = TurnState.Default;
				}
			}
			break;
		case DeckBanish:
			Card banished = this.player.playerDeck.attemptDeckBanish(loc);
			if (banished != null) {
				this.turnStateMagnitude--;
				if (this.turnStateMagnitude < 1) {
					this.turnState = TurnState.Default;
				}
				this.game.gameDeck.discard.add(banished);
			}
			break;
		case CenterBanish:
			if (!this.game.gameDeck.attemptCenterBanish(loc)) {
				this.turnStateActionCompleted = false;
			}
			this.turnStateMagnitude--;
			if (this.turnStateMagnitude < 1) {
				this.turnState = TurnState.Default;
			}
			this.completedActions.add(this.turnStateActionCompleted);
			break;
		case DefeatMonster:
			Card defeatedMonster = this.game.gameDeck.attemptDefeatMonster(loc, this.turnStateMagnitude);
			if (defeatedMonster != null) {
				executeCardAction(defeatedMonster);
				this.turnState = TurnState.Default;
				this.turnStateMagnitude = 0;
			}
			break;
		default:
			break;
		}

	}
	
	public void executeUnitedAction(Action a) {
		switch (a.action) {
		case HonorBoost:
			this.player.incrementHonor(a.magnitude);
			this.game.decrementHonor(a.magnitude);
			break;
		case PowerBoost:
			this.power += a.magnitude;
			break;
		case RuneBoost:
			this.rune += a.magnitude;
			break;
		case DrawCard:
			player.playerDeck.drawNCards(a.magnitude);
			break;
		case Discard:
			this.turnState = TurnState.Discard;
			this.turnStateMagnitude = a.magnitude;
			break;
		case ForcedDeckBanish:
			this.turnState = TurnState.DeckBanish;
			this.turnStateMagnitude = a.magnitude;
			break;
		case CenterBanish:
			this.turnState = TurnState.CenterBanish;
			this.turnStateMagnitude = a.magnitude;
			break;
		case OptionalDeckBanish:
			int n = optionPane.showConfirmDialog(game, "Would you like to banish " + a.magnitude + " card(s) from the deck?", 
					"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(n == JOptionPane.YES_OPTION) {
				this.turnState = TurnState.DeckBanish;
				this.turnStateMagnitude = a.magnitude;
			}
			break;
		case HonorAndRuneBoost:
			this.rune += a.magnitude;
			this.player.incrementHonor(a.magnitude);
			this.game.decrementHonor(a.magnitude);
			break;
		case ConstructRuneBoost:
			this.constructRune += a.magnitude;
			break;
		case MechanaConstructRuneBoost:
			this.mechanaConstructRune += a.magnitude;
			break;
		case EnterAiyanaState:
			this.AiyanaState = true;
			break;
		case DefeatMonster:
			break;
		case HeroRuneBoost:
			break;
		case MonsterPowerBoost:
			break;
		default:
			break;
		}
	}


	public void executeCardAction(Card c) {
		if (this.united && c.getFaction() == Card.Faction.Lifebound
				&& !this.uniteOccurred) {
			executeUnitedAction(this.actionOnUnite);
			this.uniteOccurred = true;
		}
		for (Action a : c.getActions()) {
			this.completedActions = new ArrayList<Boolean>();
			this.turnStateActionCompleted = true;
			if (a.dependency < 0 || this.completedActions.get(a.dependency)) {
				if (a.onUnite && !this.united) {
					this.actionOnUnite = a;
					this.united = true;
					break;
				}
				switch (a.action) {
				case HonorBoost:
					this.player.incrementHonor(a.magnitude);
					this.game.decrementHonor(a.magnitude);
					this.completedActions.add(true);
					break;
				case PowerBoost:
					this.power += a.magnitude;
					this.completedActions.add(true);
					break;
				case RuneBoost:
					this.rune += a.magnitude;
					this.completedActions.add(true);
					break;
				case DrawCard:
					player.playerDeck.drawNCards(a.magnitude);
					this.completedActions.add(true);
					break;
				case Discard:
					this.turnState = TurnState.Discard;
					this.turnStateMagnitude = a.magnitude;
					this.completedActions.add(true);
					break;
				case ForcedDeckBanish:
					optionPane.showMessageDialog(game,"Select " + a.magnitude + " card(s) from the deck to banish them","",
							JOptionPane.PLAIN_MESSAGE); 
					this.turnState = TurnState.DeckBanish;
					this.turnStateMagnitude = a.magnitude;
					this.completedActions.add(true);
					break;
				case CenterBanish:
					this.turnState = TurnState.CenterBanish;
					this.turnStateMagnitude = a.magnitude;
					break;
				case OptionalDeckBanish:
					int n = optionPane.showConfirmDialog(game, "Would you like to banish " + a.magnitude + " card(s) from the deck?", 
							"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(n == JOptionPane.YES_OPTION) {
						this.turnState = TurnState.DeckBanish;
						this.turnStateMagnitude = a.magnitude;
					}
					break;
				case HonorAndRuneBoost:
					this.rune += a.magnitude;
					this.player.incrementHonor(a.magnitude);
					this.game.decrementHonor(a.magnitude);
					this.completedActions.add(true);
					break;
				case ConstructRuneBoost:
					this.constructRune += a.magnitude;
					this.completedActions.add(true);
					break;
				case MechanaConstructRuneBoost:
					this.mechanaConstructRune += a.magnitude;
					this.completedActions.add(true);
					break;
				case EnterAiyanaState:
					this.AiyanaState = true;
					break;
				case HeroRuneBoost:
					this.heroRune += a.magnitude;
					break;
				case MonsterPowerBoost:
					this.monsterPower += a.magnitude;
					break;
				case DefeatMonster:
					if (!this.game.gameDeck.canAMonsterBeDefeated(a.magnitude)) {
					this.player.incrementHonor(1);
					this.game.decrementHonor(1);
					} else {
					this.turnState = TurnState.DefeatMonster;
					this.turnStateMagnitude = a.magnitude;
					}
					break;
				}
			}
		}
	}

	
	public boolean staticCardList(ArrayList<Card> s, Point p) {
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
						executeCardAction(c);
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
