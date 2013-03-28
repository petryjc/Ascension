import static org.junit.Assert.*;

import org.junit.Test;


public class ActionTest {

	@Test
	public void testActionIntialize() {
		Action a = new Action(10, Action.ActionType.HonorBoost);
		Action b = new Action(10, Action.ActionType.PowerBoost);
		Action c = new Action(10, Action.ActionType.RuneBoost);
		Action d = new Action(5, Action.ActionType.DrawCard);
		
		assertEquals(a.magnitude, 10);
		assertEquals(a.action, Action.ActionType.HonorBoost);
		assertEquals(b.magnitude, 10);
		assertEquals(b.action, Action.ActionType.PowerBoost);
		assertEquals(c.magnitude, 10);
		assertEquals(c.action, Action.ActionType.RuneBoost);
		assertEquals(d.magnitude, 5);
		assertEquals(d.action,Action.ActionType.DrawCard);
	}

}
