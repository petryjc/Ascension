import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;


public class Card {
	
	private String name;
	private Rectangle location;
	private Type type;
	private Faction faction;
	private int cost;
	private ArrayList<Action> actions;
	private Image i;
	private int honorWorth;
	
	public Card() {
		this(null,null,null, 0, new ArrayList<Action>(),"");
	}
	
	public Card(Card c) {
		this(c.location, c.type, c.faction, c.cost, c.actions, c.name, c.honorWorth);
	}
	
	public Card(Type type, Faction faction) {
		this(null, type, faction, 0, new ArrayList<Action>(),"");
	}
	
	public Card(Type type, Faction faction, int cost, ArrayList<Action> actions, String name) {
		this(null, type, faction, cost, actions,name);
	}
	
	public Card(Type type, Faction faction, int cost, ArrayList<Action> actions, String name, int honorWorth) {
		this(null, type, faction, cost, actions,name);
		this.honorWorth = honorWorth;
	}
	
	public Card(Rectangle location, Type type, Faction faction, int cost, ArrayList<Action> actions, String name, int honorWorth) {
		this(location, type, faction, cost, actions, name);
		this.honorWorth = honorWorth;
	}
	
	public Card(Rectangle location, Type type, Faction faction, int cost, ArrayList<Action> actions, String name) {
		this.location = location;
		this.type = type;
		this.faction = faction;
		this.cost = cost;
		this.actions = actions;
		this.name = name;
		URL url = this.getClass().getResource(this.name + ".jpg");
		if(url != null) {
			i = new ImageIcon(url).getImage();
		} else {
			i = new ImageIcon(this.getClass().getResource("Basic.jpg")).getImage();
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static enum Faction {
		Mechana, Lifebound, Void, Enlightened, Common
	}
	
	public enum Type {
		Hero, Construct, Monster
	}
	
	public Rectangle getLocation() {
		return this.location;
	}
	
	public void setLocation(Rectangle r) {
		this.location = r;
	}
	
	public Type getType() {
		return type;
	}
	public int getHonorWorth(){
		return this.honorWorth;
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
	
	public int getCost() {
		return this.cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}


	public boolean onCard(Point p) {
		if(location == null)
			return false;
		return location.contains(p);
	}
	
	public ArrayList<Action> getActions() {
		return this.actions;
	}
	
	public Image getImage() {
		return this.i;
	}
	
}
