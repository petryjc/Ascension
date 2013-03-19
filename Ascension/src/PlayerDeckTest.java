import static org.junit.Assert.*;

import org.junit.Test;


public class PlayerDeckTest {

	@Test
	public void testInitialization() {
		PlayerDeck pd = new PlayerDeck();
		assertNotNull(pd);
	}

}
