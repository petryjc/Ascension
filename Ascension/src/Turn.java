import java.awt.Frame;
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
	Boolean VoidMesmerState;
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
		this.VoidMesmerState = false;
		optionPane = new DefaultOptionPane();
		for (Card c : this.player.playerDeck.constructs) {
			executeCard(c);
		}
	}
	
	public void playAll() {
		//Continue playing cards until you run out (aynchronous nature made this easier)
		while (!this.player.playerDeck.hand.isEmpty()) {
			Card c = this.player.playerDeck.hand.get(0);
			this.player.playerDeck.playCard(c);
			this.executeCard(c);
		}
	}

	public synchronized void exitActiveWaitingState() {
		this.turnState = TurnState.Default;
		this.turnStateMagnitude = 0;
		notify();
	}
	
	public synchronized void decrementTurnStateMagnitude() {
		this.turnStateMagnitude--;
		if (this.turnStateMagnitude < 1) {
			exitActiveWaitingState();
		}
	}
	
	public void handleDiscard(Point loc) {
		switch (turnState) {
		case DeckBanish:
			Card banished = this.player.playerDeck.attemptDeckDiscardBanish(loc);
			if (banished != null) {
				decrementTurnStateMagnitude();
				this.game.gameDeck.discard.add(banished);
			}
			break;
		default:
			break;
		}
	}
	
	public synchronized void leftButtonClick(Point loc) {
		if(this.game.discardFrame.isVisible()) {
			handleDiscard(loc);
			return;
		}
		Rectangle end = new Rectangle(1460, 492, 91, 91);
		if (end.contains(loc)) {
			this.player.playerDeck.endTurn();
			this.game.nextTurn();
			return;
		}
		Rectangle discard = new Rectangle(1431, 692, 142, 108);
		if(discard.contains(loc)) {
			this.game.discardFrame.setVisible(true);
			return;
		}
		switch (turnState) {
		case Default:
			Rectangle playAllCardsInHand = new Rectangle(47, 493, 83, 100);
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
			if (this.player.playerDeck.attemptDiscard(loc) != null) {
				decrementTurnStateMagnitude();
			}
			break;
		case AskaraDiscard:
			Card askaraDiscardCard = this.player.playerDeck.attemptDiscard(loc);
			if (askaraDiscardCard != null) {
				decrementTurnStateMagnitude();
				if (askaraDiscardCard.getFaction() == Card.Faction.Enlightened && this.turnStateMagnitude > 0) {
					decrementTurnStateMagnitude();
				}
			}
			break;
		case DeckBanish:
			Card banished = this.player.playerDeck.attemptDeckHandBanish(loc);
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
		case AskaraCenterBanish:
			Card c1 = this.game.gameDeck.attemptAskaraCenterBanish(loc);
			if(c1 != null) {
				if (c1.getType() == Card.Type.Monster) {
					this.player.incrementHonor(3);
					this.game.decrementHonor(3);
				}
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
		case VoidMesmerState:
			Card aquHero = this.game.gameDeck.attemptGetHero(loc, this.turnStateMagnitude);
			if (aquHero != null) {
				executeCard(aquHero);
				exitActiveWaitingState();
			}
			break;
			
		case FreeCard:
			if (attemptCardPurchaseWithinCardList(this.game.gameDeck.hand, loc)) {
				decrementTurnStateMagnitude();
			}
			break;
		case FreeCardHero:
			Card e  = this.game.gameDeck.getHeroFromCenter(loc);
			
			if(e != null){
				this.game.gameDeck.hand.remove(e);
				this.game.gameDeck.drawCard();
				this.player.playerDeck.addNewCardToDiscard(e);
				this.turnState = Turn.TurnState.Default;
			}
			break;
		case HandBanish:
			Card b = this.player.playerDeck.attemptDeckHandBanish(loc);
			if (b != null) {
				decrementTurnStateMagnitude();
				this.game.gameDeck.discard.add(b);
			}
			break;

		case TwofoldAskara:
			Card d = this.player.playerDeck.getCardFromPlayed(loc);
			if(d != null){	
				this.executeCard(d);
				this.turnState = Turn.TurnState.Default;
			}
			break;
			

		case RajTurnState:
			Card cardForRajTurnState = this.player.playerDeck.attemptDeckHandBanish(loc);
			if (cardForRajTurnState != null) {
				this.turnStateMagnitude = cardForRajTurnState.getHonorWorth() + 2;
				this.turnState = Turn.TurnState.RajTurnState2;		
			}
			break;
		case RajTurnState2:
			Card cardForRajTurnState2ToAcquire = this.game.gameDeck.attemptRajEffect(loc, this.turnStateMagnitude);
			if (cardForRajTurnState2ToAcquire != null) {
				this.player.playerDeck.hand.add(cardForRajTurnState2ToAcquire);
				this.player.playerDeck.resetHandLocation();
				this.turnStateMagnitude = 0;
				this.turnState = Turn.TurnState.Default;
			}
			break;
		case SeaTyrantState:
			this.player.incrementHonor(5);
			this.game.decrementHonor(5);
			for(Player p:this.game.players){
				if(!(p.equals(this.player))){
					p.flipTyrantConstructsBool();
				}
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
	
	public void chill() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
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
			optionPane.showMessageDialog(game,"Select " + a.magnitude + " card(s) from your deck to discard","",
					JOptionPane.PLAIN_MESSAGE);
			this.turnState = TurnState.Discard;
			this.turnStateMagnitude = a.magnitude;
			chill();
			return true;
		case ForcedDeckBanish:
			optionPane.showMessageDialog(game,"Select " + a.magnitude + " card(s) from your deck to banish them","",
					JOptionPane.PLAIN_MESSAGE); 
			this.turnState = TurnState.DeckBanish;
			this.turnStateMagnitude = a.magnitude;
			chill();
			return true;
		case CenterBanish:
			int m = optionPane.showConfirmDialog(game, "Would you like to banish " + a.magnitude + " card(s) from the center deck?", 
					"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(m == JOptionPane.YES_OPTION) {
				this.turnState = TurnState.CenterBanish;
				this.turnStateMagnitude = a.magnitude;
				chill();
				return true;
			}
			return false;
		case HandBanish:
			int z = optionPane.showConfirmDialog(game, "Would you like to banish " + a.magnitude + " card(s) from your hand?", 
					"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(z == JOptionPane.YES_OPTION) {
				this.turnState = TurnState.HandBanish;
				this.turnStateMagnitude = a.magnitude;
				chill();
				return true;
			}
			return false;
		case OptionalDeckBanish:
			if(player.playerDeck.hand.size() == 0)
				return false;
			int n = optionPane.showConfirmDialog(game, "Would you like to banish " + a.magnitude + " card(s) from your deck?", 
					"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(n == JOptionPane.YES_OPTION) {
				this.turnState = TurnState.DeckBanish;
				this.turnStateMagnitude = a.magnitude;
				chill();
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
		case EnterVoidMesmer:
			this.VoidMesmerState = true;
			return true;
		case FreeCard:
			optionPane.showMessageDialog(game,"Select a free center card","",
					JOptionPane.PLAIN_MESSAGE);
			this.turnState = TurnState.FreeCard;
			this.turnStateMagnitude = a.magnitude;
		case HeavyOrMystic:
			Object objects[] = {"Mystic", "Heavy Infantry"};
			int heavyOrMysticChoice = optionPane.showOptionDialog(game, "Aquire a Mystic or Heavy Infantry?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, objects);
			if(heavyOrMysticChoice == JOptionPane.YES_OPTION) {
				this.player.playerDeck.hand.add(new Card(Main.getMystic()));
				this.player.playerDeck.deckRend.resetHandLocation();
			} else {
				this.player.playerDeck.hand.add(new Card(Main.getHeavyInfantry()));
				this.player.playerDeck.deckRend.resetHandLocation();
			}
			return true;
		case LunarStag:
			Object objects2[] = {"2 Rune", "2 Honor"};
			int lunarStagChoice = optionPane.showOptionDialog(game, "Gain 2 Rune or 2 Honor", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, objects2);
			if (lunarStagChoice == JOptionPane.YES_OPTION) {
				this.rune += 2;
			} else {
				this.player.incrementHonor(2);
				this.game.decrementHonor(2);
			}
			return true;
		case AskaraOfFate:
			this.player.playerDeck.drawCard();
			for (Player p : this.game.players) {
				p.playerDeck.drawCard();
			}
			return true;
		case AskaraCenterBanish:
			int num1 = optionPane.showConfirmDialog(game, "Would you like to banish a card from the center deck?", 
					"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(num1 == JOptionPane.YES_OPTION) {
				this.turnState = TurnState.AskaraCenterBanish;
				this.turnStateMagnitude = 1;
				chill();
				return true;
			}
			return false;
		case NookHound:
			this.player.playerDeck.drawCard();
			Card nookHoundCard = this.player.playerDeck.hand.get(this.player.playerDeck.hand.size() - 1);
			int nookHoundNumber = optionPane.showConfirmDialog(game, "Would you like to discard the " + nookHoundCard.getName() +" that you just drew?", 
					"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (nookHoundNumber == JOptionPane.YES_OPTION) {
				this.player.playerDeck.hand.remove(this.player.playerDeck.hand.size() - 1);
				this.player.playerDeck.discard.add(nookHoundCard);
				this.player.playerDeck.drawCard();
			}
			return true;
		case AskaraDiscard:
			optionPane.showMessageDialog(game,"Discard 2 card or 1 Enlightened card.","",
					JOptionPane.PLAIN_MESSAGE);
			this.turnState = TurnState.AskaraDiscard;
			this.turnStateMagnitude = 2;
			chill();
			return true;

		case TwofoldAskaraPlayed:
			if(this.player.playerDeck.checkForHeroInPlayedforTwoFold()){
				optionPane.showMessageDialog(game,"Pick a Hero to copy from the previously played cards","",
					JOptionPane.PLAIN_MESSAGE);
				this.turnState = TurnState.TwofoldAskara;
				chill();
				
				
			}else{
				optionPane.showMessageDialog(game,"No Hero available to copy","",
						JOptionPane.PLAIN_MESSAGE);
			}
			

		case RajAction:
			optionPane.showMessageDialog(game, "Banish a hero in your hand. Then acquire a hero in the center with an honor value of up to two more than the banished hero.", "", JOptionPane.PLAIN_MESSAGE);
			this.turnState = TurnState.RajTurnState;
			this.turnStateMagnitude = 1;
			chill();

			return true;
			
		case CetraAction:
			if(this.game.gameDeck.checkForHeroInCenter()){
				optionPane.showMessageDialog(game,"Pick a Hero from the Center Row","",
						JOptionPane.PLAIN_MESSAGE);
				this.turnState = TurnState.FreeCardHero;
				chill();
			}else{
				optionPane.showMessageDialog(game,"No Hero available to purchase","",
						JOptionPane.PLAIN_MESSAGE);
			}
			return true;
		}
		return false;
	}
	
	public boolean attemptCardPurchaseWithinCardList(ArrayList<Card> s, Point p) {
		for (Card c : s) {
			if (c.getLocation().contains(p)) {
				
				if (c.getType() == Card.Type.Monster) {
					if(this.turnState == TurnState.FreeCard) {
						executeCard(c);
						this.game.gameDeck.hand.remove(c);
						if (!c.getName().equals("Cultist")) {
							this.game.gameDeck.discard.add(c);
							this.game.gameDeck.drawCard();
						}
					}
					else if (this.monsterPower > 0 && c.getCost() < this.power + this.monsterPower) {
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
						if(this.VoidMesmerState){
							this.turnState = TurnState.VoidMesmerState;
							this.turnStateMagnitude = c.getCost();
						}
					}
				} else {
					if(this.turnState == TurnState.FreeCard) {
						this.game.gameDeck.hand.remove(c);
						this.player.playerDeck.addNewCardToDiscard(c);
						this.game.gameDeck.drawCard();
					}
					else if (c.getType() == Card.Type.Construct
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

		Default, Discard, DeckBanish, CenterBanish, DefeatMonster,VoidMesmerState, FreeCard, HandBanish, AskaraCenterBanish, AskaraDiscard, RajTurnState, RajTurnState2,TwofoldAskara, FreeCardHero, SeaTyrantState

	}

}
