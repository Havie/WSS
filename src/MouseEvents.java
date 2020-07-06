import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseMotionListener;

public class MouseEvents implements MouseListener, MouseWheelListener, MouseMotionListener{
	private boolean bWasMouseClicked;
	private boolean bWasMouseEntered;
	private boolean bWasMouseExited;
	private boolean bWasMousePressed;
	private boolean bWasMouseReleased;
	private boolean bMouseIsDown;
	private boolean bWasMouseMoved;
	private Vector2Int v2MousePosition;
	
	private boolean bWasMouseScrolled;
	private int iMouseScrollAmount;
	private int iMouseButton;
	private int iMouseButtonClicked;
	
	private final long DOUBLE_CLICK_TIMER = 300000000;
	private boolean bWasDoubleClicked;
	private long loLastClickTime;
	private int iLastMouseButton;
	
	/**
	 * Constructs a MouseEvents object.
	 */
	public MouseEvents(){
		reset();
		bMouseIsDown = false;
		v2MousePosition = new Vector2Int(-1, -1);
		loLastClickTime = System.nanoTime();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		bWasMouseClicked = true;
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
		iMouseButtonClicked = arg0.getButton();
		
		if (System.nanoTime() - loLastClickTime < DOUBLE_CLICK_TIMER
				&& iLastMouseButton == iMouseButtonClicked) {
			bWasDoubleClicked = true;
		}
		loLastClickTime = System.nanoTime();
		iLastMouseButton = iMouseButtonClicked;
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
		bMouseIsDown = true;
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
		iMouseButton = arg0.getButton();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		bWasMouseReleased = true;
		bMouseIsDown = false;
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
		iMouseButton = arg0.getButton();
	}
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		bWasMouseScrolled = true;
		iMouseScrollAmount = arg0.getWheelRotation();
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		v2MousePosition = new Vector2Int(arg0.getX(), arg0.getY());
		bWasMouseMoved = true;
	}
	
	// Getters
	public boolean getWasMouseClicked() { return bWasMouseClicked; }
	public boolean getWasMouseEntered() { return bWasMouseEntered; }
	public boolean getWasMouseExited() { return bWasMouseExited; }
	public boolean getWasMousePressed() { return bWasMousePressed; }
	public boolean getWasMouseReleased() { return bWasMouseReleased; }
	public boolean getMouseIsDown() { return bMouseIsDown; }
	public Vector2Int getMousePosition() { return v2MousePosition; }
	public boolean getWasMouseScrolled() { return bWasMouseScrolled; }
	public int getMouseScrollAmount() { return iMouseScrollAmount; }
	public int getMouseButton() { return iMouseButton; }
	public int getMouseButtonClicked() { return iMouseButtonClicked; }
	public boolean getWasMouseDoubleClicked() { return bWasDoubleClicked; }
	public boolean getWasMouseMoved() { return bWasMouseMoved; }
	
	/**
	 * Resets all the booleans to false.
	 */
	public void reset(){
		bWasMouseClicked = false;
		bWasMouseEntered = false;
		bWasMouseExited = false;
		bWasMousePressed = false;
		bWasMouseReleased = false;
		bWasMouseScrolled = false;
		iMouseScrollAmount = 0;
		iMouseButtonClicked = -1;
		bWasMouseMoved = false;
		bWasDoubleClicked = false;
	}

}
