import java.awt.Component;


public interface IOptionPane {
	int showConfirmDialog(Component parentComponent,
	          Object message, String title, int optionType, int messageType);
	
	void showMessageDialog(Component parentComponent,
	          Object message, String title, int messageType);
}
