import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class CapitalsOnlyKeyEventHandler implements KeyListener {
	private JTextField textField;
	
	/**
	 * Constructs a CapitalsOnlyKeyListener.
	 * 
	 * @param _textField_
	 * 				The text field which will be limited to only capitals.
	 */
	public CapitalsOnlyKeyEventHandler(JTextField _textField_) {
		textField = _textField_;
	}
	
	@Override
	public void keyPressed(KeyEvent arg0) {
	}
	/**
	 * When a key is released, set the text to be capital only.
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		if (arg0.getKeyCode() >= KeyEvent.VK_A && arg0.getKeyCode() <= KeyEvent.VK_Z) {
			textField.setText(textField.getText().toUpperCase());
		}
	}
	@Override
	public void keyTyped(KeyEvent arg0) {}

}
