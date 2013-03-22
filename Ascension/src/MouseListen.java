import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Date;


public class MouseListen implements MouseListener{
	private Turn turn;
	private Game game;
	
	MouseListen(Turn t, Game g) {
		this.turn = t;
		this.game = g;
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
		Rectangle end = new Rectangle(1460,492, 91, 91);
		
		if(e.getButton() == MouseEvent.BUTTON1){
			System.out.println("Hello");
			if(end.contains(loc)){
				this.turn.player.playerDeck.endTurn();
				game.nextTurn();
			}
			else{
				if(turn == null) 
					return;
				turn.player.playerDeck.handleClick(loc);
			}
			
		}else if(e.getButton() == MouseEvent.BUTTON3){
			
			System.out.println("hello friend");
			
			
		}else{
			//
			
		}
	}

}
