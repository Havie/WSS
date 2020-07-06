import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuActionEventHandler implements ActionListener {
	private boolean bWasActionPerformed;	// If an action was just performed.
	private ActionEvent aeInfo;	// The info of the ActionEvent

	/**
	 * Constructs a MenuActionListener.
	 */
	public MenuActionEventHandler() {
		reset();
	}
	
	/**
	 * Override of actionPerformed.
	 * Called when the user clicks a menu with this class as a listener.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		bWasActionPerformed = true;
		aeInfo = arg0;
	}
	
	/**
	 * Resets the variables of the MenuActionListener
	 */
	public void reset() {
		bWasActionPerformed = false;
		aeInfo = null;
	}

	/**
	 * Returns the information of the most recent action.
	 * 
	 * @return ActionEvent
	 */
	public ActionEvent getActionEventInfo() { return aeInfo; }
	/**
	 * Returns if an action was performed since the last reset or not.
	 * 
	 * @return boolean
	 */
	public boolean getWasActionPerformed() { return bWasActionPerformed; }
}
