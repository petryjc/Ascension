import java.awt.Component;


public class TestOptionPane implements IOptionPane{
	int returnResult;
	
	public TestOptionPane(int returnResult) {
		this.returnResult = returnResult;
	}
	
	@Override
	public int showConfirmDialog(Component parentComponent, Object message,
			String title, int optionType, int messageType) {
		return returnResult;
	}

	@Override
	public void showMessageDialog(Component parentComponent, Object message,
			String title, int messageType) {
		// TODO Auto-generated method stub
		
	}
	
	public int showOptionDialog(Component parentComponent, Object message,
			String title, int optionType, int messageType, Object[] options) {
		return returnResult;
	}

}
