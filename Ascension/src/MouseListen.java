import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseListen implements MouseListener{
	private Turn turn;
	
	MouseListen(Turn t) {
		this.turn = t;
	}
	
	public void setTurn(Turn t) {
		this.turn = t;
	}

	public void mouseClicked(MouseEvent e) {
		//		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		
//		long duration = System.currentTimeMillis() - time;
//		System.out.println(duration);
//		if(duration < 1000) {
//			System.out.println("Saved");
//			return;
//		} else {
//			time = System.currentTimeMillis();
//		}
//		
		
		Point loc = e.getLocationOnScreen();
		
		if(e.getButton() == MouseEvent.BUTTON1){
			turn.leftButtonClick(loc);
			
		}else if(e.getButton() == MouseEvent.BUTTON3){
			
			
		}else{
			//
			
		}
	}

}
