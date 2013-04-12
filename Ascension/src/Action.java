
public class Action {

	int magnitude;
	ActionType action;
	
	public enum ActionType {
		HonorBoost, RuneBoost, PowerBoost, DrawCard, Discard, ForcedDeckBanish, OptionalDeckBanish, CenterBanish
	}
	
	public Action(int magnitude, ActionType at) {
		this.magnitude = magnitude;
		this.action = at;
	}
}
