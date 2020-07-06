import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class KeyEventHandler implements KeyListener {
	private boolean bWasKeyPressed;		// If a key was pressed
	private boolean bWasKeyReleased;	// If a key was released
	private boolean bWasKeyTyped;		// If a key was typed
	private boolean bIsKeyHeld;			// If a key is being held down
	private int iKeyCode;				// The integer value of the key pressed/released
	private int iKeyCodeTyped;			// The integer value of the key typed
	private ArrayList<Integer> alKeyCodesHeld;	// The integer values of the keys being held down
	
	/**
	 * Constructs a KeyEventHandler.
	 * Default.
	 */
	public KeyEventHandler() {
		reset();
		
		alKeyCodesHeld = new ArrayList<Integer>();
	}

	/**
	 * Called when a key is pressed.
	 * 
	 * @param arg0
	 * 				KeyEvent information.
	 */
	@Override
	public void keyPressed(KeyEvent arg0) {
		bWasKeyPressed = true;
		iKeyCode = arg0.getKeyCode();
		
		if (!alKeyCodesHeld.contains((Integer) arg0.getKeyCode())) {
			alKeyCodesHeld.add((Integer) arg0.getKeyCode());
			bIsKeyHeld = true;
		}
	}

	/**
	 * Called when a key is released.
	 * 
	 * @param arg0
	 * 				KeyEvent information.
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {
		bWasKeyReleased = true;
		iKeyCode = arg0.getKeyCode();
		
		alKeyCodesHeld.remove((Integer) arg0.getKeyCode());
		bIsKeyHeld = !alKeyCodesHeld.isEmpty();
	}

	/**
	 * Called when a key is typed.
	 * 
	 * @param arg0
	 * 				KeyEvent information.
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {
		bWasKeyTyped = true;
		iKeyCodeTyped = arg0.getKeyCode();
	}
	
	/**
	 * Resets the stored input information.
	 */
	public void reset() {
		bWasKeyPressed = false;
		bWasKeyReleased = false;
		bWasKeyTyped = false;
		iKeyCode = -1;
		iKeyCodeTyped = -1;
	}
	
	/**
	 * Returns if a key was pressed since the last refresh.
	 * 
	 * @return boolean
	 */
	public boolean getWasKeyPressed() { return bWasKeyPressed; }
	/**
	 * Returns if a key was released since the last refresh.
	 * 
	 * @return boolean
	 */
	public boolean getWasKeyReleased() { return bWasKeyReleased; }
	/**
	 * Returns if a key was typed since the last refresh.
	 * 
	 * @return boolean
	 */
	public boolean getWasKeyTyped() { return bWasKeyTyped; }
	/**
	 * Returns true if at least one key is being held down.
	 * 
	 * @return boolean
	 */
	public boolean getIsKeyHeld() { return bIsKeyHeld; }
	/**
	 * Returns the last key pressed/released since the last refresh.
	 * 
	 * @return integer
	 */
	public int getKeyCode() { return iKeyCode; }
	/**
	 * Returns the last key typed since the last refresh.
	 * 
	 * @return integer
	 */
	public int getKeyCodeTyped() { return iKeyCodeTyped; }
	/**
	 * Returns a copy of the list of key codes being held down.
	 * 
	 * @return ArrayList<Integer>
	 */
	public ArrayList<Integer> getKeyCodesHeld() { return alKeyCodesHeld; }

}
