
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
	@Test
	public void testIncrementHonor(){
		Player p1 = new Player(new PlayerDeck(PlayerDeckTest.randomCardList(),null,null,null), "Jack");
		p1.incrementHonor(25);
		Assert.assertEquals(25, p1.honorTotal);
	}
	@Test
	public void testGetNewPlayer(){
		Player p1 =  new Player(new PlayerDeck(PlayerDeckTest.randomCardList(),null,null,null), "Jack");
		Player p2 = Player.getNewPlayer("Gabe");
		
		Assert.assertEquals("Jack", p1.name);
		Assert.assertEquals("Gabe", p2.name);
		Assert.assertNotNull(p2);
		
	}
}
