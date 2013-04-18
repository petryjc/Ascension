
import junit.framework.Assert;

import org.junit.Test;


public class PlayerTest {

	@Test
	public void testInitialization() {
		Player p = new Player();
		Player p1 = new Player(new PlayerDeck(PlayerDeckTest.randomCardList(),null,null,null), "Jack");
		Assert.assertNotNull(p);
		Assert.assertNotNull(p1);
	}
	
	@Test
	public void testStartingHand() {
		Player p1 = new Player(new PlayerDeck(PlayerDeckTest.randomCardList(),null,null,null), "Jack");
		Assert.assertEquals(0,p1.playerDeck.hand.size());
		p1.startingHand();
		Assert.assertEquals(5,p1.playerDeck.hand.size());
	}

}
