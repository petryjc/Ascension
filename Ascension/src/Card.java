import java.awt.Point;
import java.awt.Rectangle;


public class Card {
	
	public enum Faction {
		Mechana, Lifebound, Void, Enlightened
	}
	
	public enum Type {
		Hero, Construct, Monster
	}
	
	private Rectangle location;
	private Type type;
	private Faction faction;
	
	public Rectangle getLocation() {
		return this.location;
	}
	
	public void setLocation(Rectangle r) {
		this.location = r;
	}
	
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Faction getFaction() {
		return faction;
	}

	public void setFaction(Faction faction) {
		this.faction = faction;
	}

	public boolean onCard(Point p) {
		return location.contains(p);
	}
	
}
