
public class Action {

	int magnitude;
	int dependency;
	ActionType action;
	boolean happened;
	boolean onUnite;
	
	public enum ActionType {
		HonorBoost, RuneBoost, HonorAndRuneBoost, PowerBoost, DrawCard, Discard, 
		ForcedDeckBanish, OptionalDeckBanish, CenterBanish, ConstructRuneBoost, 
		MechanaConstructRuneBoost, EnterAiyanaState, HeroRuneBoost, MonsterPowerBoost, 
		DefeatMonster, EnterVoidMesmer, FreeCard
	}
	
	public Action(int magnitude, ActionType at) {
		this.magnitude = magnitude;
		this.action = at;
		this.dependency = -1;
		this.onUnite = false;
	}
	
	public Action(int magnitude, ActionType at, int dependency) {
		this.magnitude = magnitude;
		this.action = at;
		this.dependency = dependency;
		this.onUnite = false;
	}
	
	public Action(int magnitude, ActionType at, int dependency, boolean onUnite) {
		this.magnitude = magnitude;
		this.action = at;
		this.dependency = dependency;
		this.onUnite = onUnite;
	}

	public boolean hasHappened() {
		return happened;
	}

	public void setHappened(boolean happened) {
		this.happened = happened;
	}
	
	
}
