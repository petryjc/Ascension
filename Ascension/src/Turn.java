import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Turn {

	Player player;
	int rune;
	int power;
	int constructRune;
	int mechanaConstructRune;
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

	public Turn(Player player, Game g) {
		this.player = player;
		this.rune = 0;
		this.power = 0;
		this.game = g;
		this.turnState = TurnState.Default;
		actions = new ArrayList<Action>();
		this.uniteOccurred = false;
		this.united = false;
		this.AiyanaState = false;
		for (Card c : this.player.playerDeck.constructs) {
			executeCardAction(c);
		}

	}

	public void leftButtonClick(Point loc) {
		if (actions.size() <= 0) {
			this.turnState = TurnState.Default;
		}
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
				while (!this.player.playerDeck.hand.isEmpty()) {
					Card c = this.player.playerDeck.hand.get(0);
					this.executeCardAction(c);
					this.player.playerDeck.playCard(c);
				}

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
		case OptionalDeckBanish:
			Card banishedcard = this.player.playerDeck.attemptDeckBanish(loc);
			if (banishedcard != null) {
				this.game.gameDeck.discard.add(banishedcard);
			} else {
				this.turnStateActionCompleted = false;
			}
			this.turnStateMagnitude--;
			if (this.turnStateMagnitude < 1) {
				this.turnState = TurnState.Default;
			}
			this.completedActions.add(this.turnStateActionCompleted);
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
			this.turnState = TurnState.OptionalDeckBanish;
			this.turnStateMagnitude = a.magnitude;
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
					this.turnState = TurnState.DeckBanish;
					this.turnStateMagnitude = a.magnitude;
					this.completedActions.add(true);
					break;
				case CenterBanish:
					this.turnState = TurnState.CenterBanish;
					this.turnStateMagnitude = a.magnitude;
					break;
				case OptionalDeckBanish:
					this.turnState = TurnState.OptionalDeckBanish;
					this.turnStateMagnitude = a.magnitude;
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
				}
			}
		}
	}

	public boolean staticCardList(ArrayList<Card> s, Point p) {
		for (Card c : s) {
			if (c.getLocation().contains(p)) {
				if (c.getType() == Card.Type.Monster) {
					if (c.getCost() <= this.power) {
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
		Default, Discard, DeckBanish, CenterBanish, OptionalDeckBanish
	}
}
