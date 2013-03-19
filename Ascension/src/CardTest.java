import static org.junit.Assert.*;

import org.junit.Test;


public class CardTest {

	@Test
	public void testInitialization() {
		Card c= new Card();
		assertNotNull(c);
	}

}
