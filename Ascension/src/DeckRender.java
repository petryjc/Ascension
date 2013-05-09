import java.awt.Rectangle;
import java.util.ArrayList;


public class DeckRender {
	
	public static final double cardDimensionRatio = 1.5; //height divided by width
	static final int yBorder = 2;  // Space between cards and top/bottom
	static final int xBorder = 2;  // Space between cards and edges
		
	private Deck deck;
	
	public DeckRender(Deck deck){
		this.deck = deck;
	}
	
	public void resetHandLocation() {
		nullOutCardLocation(this.deck.notPlayed);
		nullOutCardLocation(this.deck.discard);
		setCardListWithinLocation(this.deck.hand, this.deck.handLocation);
	}
	
	public static void setCardListWithinLocation(ArrayList<Card> cardList, Rectangle location) {
		if(location == null)
			return;
		
		int size = cardList.size();
		int sf1 = (int) Math.round((location.height-2 * yBorder)/cardDimensionRatio);
		int sf2 = (int) Math.round((location.width - (size + 1) * xBorder)/(size + 0.0));
		int sf = Math.min(sf1, sf2);
		int x = xBorder + location.x;
		if(sf2 > sf1) {
			x += (location.width - ((size + 1) * xBorder + size * sf))/2;
		}
		for(Card c : cardList) {
			c.setLocation(new Rectangle(x, yBorder + location.y, sf, (int) (sf * cardDimensionRatio)));
			x += sf + xBorder;
		}
	}
	
	public static void nullOutCardLocation(ArrayList<Card> cardList) {
		for(Card c : cardList) {
			c.setLocation(null);
		}
	}
	
}
