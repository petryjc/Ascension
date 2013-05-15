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
	Boolean VoidthirsterState;
	Boolean HedronLinkDeviceState;
	int RocketCourierState;

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
		this.VoidthirsterState = false;
		this.RocketCourierState = 0;
		this.HedronLinkDeviceState = false;
		optionPane = new DefaultOptionPane();
		
		if(this.player.seaTyrant){
			this.turnState = TurnState.SeaTyrantTurnBegin;
		} else if(this.player.corrosiveWidow){
			this.turnState = TurnState.CorrosiveWidowTurnBegin;
		}
		
		
		
		if(!this.player.seaTyrant && !this.player.corrosiveWidow){
			for (Card c : this.player.playerDeck.constructs) {
				executeCard(c);
			}	
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
			c = this.player.playerDeck.activateConstruct(loc);
			if (c != null) {
				executeCard(c);
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
			System.out.println(cardForRajTurnState);
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
				exitActiveWaitingState();
			}
			break;
		case SeaTyrantTurnBegin:
			
			if(this.player.playerDeck.constructs.size() <= 1){
				this.player.seaTyrant = false;
			}
			
			if(!this.player.seaTyrant){
				for(Card r:this.player.playerDeck.constructs){
					executeCard(r);
					this.turnState = Turn.TurnState.Default;
				}
				break;
			}
			
			this.optionPane.showMessageDialog(this.game, "Please choose a construct to save the rest will be placed in your discard", "", JOptionPane.PLAIN_MESSAGE);
			Card t = this.player.playerDeck.activateConstruct(loc);
			if (t != null) {
				for(Card x: this.player.playerDeck.constructs){
					if(!(x.equals(t))){
						this.player.playerDeck.discard.add(x);
						this.player.playerDeck.constructs.remove(x);
						this.player.playerDeck.resetHandLocation();
					}
				}
				this.turnState = Turn.TurnState.Default;
				this.turnStateMagnitude = 0;
			}
			
			break;
			
		case CorrosiveWidowTurnBegin:
			if(this.player.playerDeck.constructs.size() < 1){
				this.turnState = Turn.TurnState.Default;
				
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
			
			return true;
		case RajAction:
			optionPane.showMessageDialog(game, "Banish a hero in your hand. Then acquire a hero in the center with an honor value of up to two more than the banished hero.", "", JOptionPane.PLAIN_MESSAGE);
			this.turnState = TurnState.RajTurnState;
			this.turnStateMagnitude = 1;
			chill();

			return true;
		case SeaTyrantAction:
			this.player.incrementHonor(5);
			this.game.decrementHonor(5);
			for(Player p:this.game.players){
				if(!(p.equals(this.player))){
					p.flipTyrantConstructsBool();
				}
			}
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

		
		case TabletOfTimesDawn:
			int tabletOptionChoice = optionPane.showConfirmDialog(game, "Would you like to banish the Table of Times Dawn to receive an extra turn?", 
					"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if(tabletOptionChoice == JOptionPane.YES_OPTION) {
				this.game.extraTurn = true;
				for (int i = 0; i < this.player.playerDeck.constructs.size(); i++) {
					if (this.player.playerDeck.constructs.get(i).getName().equals("Tablet_of_Times_Dawn")) {
						this.player.playerDeck.constructs.remove(i);
					}
				}
				return true;
			}
			return false;
		
		case YggdrasilStaff:
			this.power += 1;
			if (this.rune >= 4) {
				int yggdrasilStaffChoice = optionPane.showConfirmDialog(game, "Would you like to exchange 4 Rune for 3 Honor?", 
						"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (yggdrasilStaffChoice == JOptionPane.YES_OPTION) {
					this.rune -= 4;
					this.player.incrementHonor(3);
					this.game.decrementHonor(3);
					return true;
				}
			}
			return true;
			
		case AvatarGolem:
			this.power += 2;
			boolean mechanaConstructHonor = false;
			boolean lifeboundConstructHonor = false;
			boolean voidConstructHonor = false;
			boolean enlightenedConstructHonor = false;
			for (Card c : this.player.playerDeck.constructs) {
				if (this.testForMechana(c) && !mechanaConstructHonor) {
					this.player.incrementHonor(1);
					this.game.decrementHonor(1);
					mechanaConstructHonor = true;
				} else if (c.getFaction() == Card.Faction.Lifebound && !lifeboundConstructHonor) {
					this.player.incrementHonor(1);
					this.game.decrementHonor(1);
					lifeboundConstructHonor = true;
				} else if (c.getFaction() == Card.Faction.Void && !voidConstructHonor) {
					this.player.incrementHonor(1);
					this.game.decrementHonor(1);
					voidConstructHonor = true;
				} else if (c.getFaction() == Card.Faction.Enlightened && !enlightenedConstructHonor) {
					this.player.incrementHonor(1);
					this.game.decrementHonor(1);
					enlightenedConstructHonor = true;
				}
			}
			return true;
			
		case KorAction:
			this.power += 2;
			if (this.player.playerDeck.constructs.size() >= 2) {
				player.playerDeck.drawNCards(1);
			}
			return true;
			
		case MechanaInitiate:
			Object mechanaInitiateOptions[] = {"1 Rune", "1 Power"};
			int mechanaInitiateChoice = optionPane.showOptionDialog(game, "Gain 1 Rune or 1 Power", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, mechanaInitiateOptions);
			if (mechanaInitiateChoice == JOptionPane.YES_OPTION) {
				this.rune += 1;
			} else {
				this.power += 1;
			}
			return true;
			
		case HedronCannon:
			for (Card c : this.player.playerDeck.constructs) {
				if (this.testForMechana(c)) {
					this.power += 1;
				}
			}
			return true;
			
		case Voidthirster:
			this.power += 1;
			this.VoidthirsterState = true;
			return true;
			
		case XeronAction:
			this.player.incrementHonor(3);
			this.game.decrementHonor(3);
			for (int i = 0; i < this.game.players.size(); i++) {
				if (!this.game.players.get(i).equals(this.player)) {
					Card xeronTemp = this.game.players.get(i).playerDeck.stealCard();
					if (xeronTemp != null) {
						this.player.playerDeck.hand.add(xeronTemp);
						this.player.playerDeck.resetHandLocation();
					}
				}
			}
			return true;
			
		case RocketCourier:
			this.RocketCourierState++;
			return true;
			
		case HedronLinkDevice:
			this.HedronLinkDeviceState = true;
			return true;
		}
		return false;
	}
	public boolean testForMechana(Card c) {
		return this.HedronLinkDeviceState || c.getFaction() == Card.Faction.Mechana;
	}
	
	public void handleRocketCourier(Card consToBuy) {
		int handleRocketCourierChoice = optionPane.showConfirmDialog(game, "Would you like to add that " + consToBuy.getName() + " to your hand?", 
				"", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (handleRocketCourierChoice == JOptionPane.YES_OPTION) {
			this.player.playerDeck.hand.add(new Card(consToBuy));
			this.RocketCourierState--;
		} else {
			this.player.playerDeck.addNewCardToDiscard(new Card(consToBuy));
		}
		this.player.playerDeck.resetHandLocation();
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
							if(this.VoidthirsterState) {
								this.player.incrementHonor(1);
								this.game.decrementHonor(1);
								this.VoidthirsterState = false;
							}
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
							if(this.VoidthirsterState) {
								this.player.incrementHonor(1);
								this.game.decrementHonor(1);
								this.VoidthirsterState = false;
							}
						}
						if(this.VoidMesmerState){
							this.turnState = TurnState.VoidMesmerState;
							this.turnStateMagnitude = c.getCost();
						}
					}
				} else {
					if(this.turnState == TurnState.FreeCard) {
						this.game.gameDeck.hand.remove(c);
						if (this.testForMechana(c) && c.getType() == Card.Type.Construct && this.RocketCourierState > 0) {
							this.handleRocketCourier(c);
						} else {
							this.player.playerDeck.addNewCardToDiscard(c);
						}
						this.game.gameDeck.drawCard();
					}
					else if (c.getType() == Card.Type.Construct
							&& this.testForMechana(c)
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
						if (this.RocketCourierState > 0) {
							this.handleRocketCourier(c);
						} else {
							this.player.playerDeck.addNewCardToDiscard(new Card(c));
						}
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
						if (this.testForMechana(c) && this.RocketCourierState > 0) {
							this.handleRocketCourier(c);
						} else {
							this.player.playerDeck.addNewCardToDiscard(new Card(c));
						}
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
						if (this.testForMechana(c) && c.getType() == Card.Type.Construct && this.RocketCourierState > 0) {
							this.handleRocketCourier(c);
						} else {
							this.player.playerDeck.addNewCardToDiscard(new Card(c));
						}
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

		Default, Discard, DeckBanish, CenterBanish, DefeatMonster,VoidMesmerState, FreeCard, HandBanish, AskaraCenterBanish, AskaraDiscard, RajTurnState, RajTurnState2,TwofoldAskara, FreeCardHero, SeaTyrantTurnBegin, CorrosiveWidowTurnBegin

	}

}
