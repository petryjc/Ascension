import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class MouseListen implements MouseListener{
	private Turn turn;
	
	MouseListen(Turn t) {
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
		
		Point loc = e.getLocationOnScreen();
		Rectangle end = new Rectangle(1465,495, 1550,587);
		
		if(e.getButton() == MouseEvent.BUTTON1){
			System.out.println("hello");	
			
			if(end.contains(loc)){
				this.turn.player.playerDeck.endTurn();
				this.turn.player.yourTurn = false;
			}
			else{
				turn.player.playerDeck.handleClick(loc);
			}
			
		}else if(e.getButton() == MouseEvent.BUTTON3){
			
			System.out.println("hello friend");
			
			
		}else{
			//
			
		}
	}

}
