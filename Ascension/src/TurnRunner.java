import java.awt.Point;


public class TurnRunner implements Runnable{
	Turn t;
	Point p;
	
	public TurnRunner(Turn t, Point p) {
		this.t = t;
		this.p = p;
	}
	
	@Override
	public void run() {
		t.leftButtonClick(p);
	}
}
