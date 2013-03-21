import java.awt.Point;
import java.awt.Rectangle;


public class Card {
	
	private Rectangle location;
	
	public boolean onCard(Point p) {
		return location.contains(p);
	}
	
}
