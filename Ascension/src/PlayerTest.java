import static org.junit.Assert.*;

import org.junit.Test;


public class PlayerTest {

	@Test
	public void testInitialization() {
		Player p = new Player();
		assertNotNull(p);
	}

}
