import java.awt.Component;

import javax.swing.JOptionPane;

public class DefaultOptionPane implements IOptionPane{

	@Override
	public int showConfirmDialog(Component parentComponent, Object message,
			String title, int optionType, int messageType) {
		return JOptionPane.showConfirmDialog(parentComponent, message, 
				title, optionType,messageType);
	}

	@Override
	public void showMessageDialog(Component parentComponent, Object message,
			String title, int messageType) {
		JOptionPane.showMessageDialog(parentComponent,message,title,
				messageType); 
	}
	
	@Override
	public int showOptionDialog(Component parentComponent, Object message,
			String title, int optionType, int messageType, Object[] options) {
		return JOptionPane.showOptionDialog(parentComponent,
				message,
				title,
				optionType,
				messageType,
				null,
				options,
				options[1]);
	}
	
}
