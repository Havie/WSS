import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DisplayTextField extends JTextField {
	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;
	
	private KeyEventHandler keyEventHandler;	// So an outside source can tell what keys
												// the user is pressing when typing in the box.
	

	/**
	 * Constructs a DisplayTextField
	 * 
	 * @param _display_
	 * 				The display this text field will be shown on.
	 */
	public DisplayTextField(Display _display_) {
		super(20);
		JTextArea ta = new JTextArea(5, 20);
		ta.setEditable(false);
		
		keyEventHandler = new KeyEventHandler();
		this.addKeyListener(keyEventHandler);
		
		_display_.add(this);
	}
	
	/**
	 * Makes the display text field capitals only.
	 */
	public void setCapitalsOnly() {
		this.addKeyListener(new CapitalsOnlyKeyEventHandler(this));
	}
	
	/**
	 * Returns the key event listener.
	 * 
	 * @return KeyEventHandler
	 */
	public KeyEventHandler getKeyEvents() { return keyEventHandler; }
}
