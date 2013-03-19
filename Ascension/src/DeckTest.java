import static org.junit.Assert.*;

import org.junit.Test;


public class DeckTest {

	@Test
	public void testInitialization() {
		Deck d = new Deck();
		assertNotNull(d);
	}

}
