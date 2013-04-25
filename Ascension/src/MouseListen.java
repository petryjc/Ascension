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

	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		
		if(e.getButton() == MouseEvent.BUTTON1){
			turn.leftButtonClick(e.getPoint());
		}else if(e.getButton() == MouseEvent.BUTTON3){
			
		}
	}

}
