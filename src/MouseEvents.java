import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEvents implements MouseListener {
	private boolean bWasMouseClicked;
	private boolean bWasMouseEntered;
	private boolean bWasMouseExited;
	private boolean bWasMousePressed;
	private boolean bWasMouseReleased;
	private Vector2Int v2MousePosition;
	
	/**
	 * Constructs a MouseEvents object.
	 */
	public MouseEvents(){
		reset();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		bWasMouseClicked = true;
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		bWasMouseEntered = true;
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		bWasMouseExited = true;
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		bWasMousePressed = true;
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		bWasMouseReleased = true;
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
	}
	
	// Getters
	public boolean getWasMouseClicked() { return bWasMouseClicked; }
	public boolean getWasMouseEntered() { return bWasMouseEntered; }
	public boolean getWasMouseExited() { return bWasMouseExited; }
	public boolean getWasMousePressed() { return bWasMousePressed; }
	public boolean getWasMouseReleased() { return bWasMouseReleased; }
	public Vector2Int getMousePosition() { return v2MousePosition; }
	
	/**
	 * Resets all the booleans to false.
	 */
	public void reset(){
		bWasMouseClicked = false;
		bWasMouseEntered = false;
		bWasMouseExited = false;
		bWasMousePressed = false;
		bWasMouseReleased = false;
		v2MousePosition = new Vector2Int(-1, -1);
	}

}
