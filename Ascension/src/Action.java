
public class Action {

	int magnitude;
	ActionType action;
	
	public enum ActionType {
		HonorBoost, RuneBoost, PowerBoost
	}
	
	public Action(int magnitude, ActionType at) {
		this.magnitude = magnitude;
		this.action = at;
	}
}
