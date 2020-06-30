import java.awt.Graphics;

public abstract class PaintableObject {
	protected Vector2Int v2Pos;		// Position of the object on the screen.
	private Graphics graphicRecent;	// The last graphics this object was painted on
	
	/**
	 * Constructs a PaintableObject.
	 */
	public PaintableObject() {
		v2Pos = new Vector2Int();
		graphicRecent = null;
	}
	
	/**
	 * Paints the object to the provided graphics.
	 * 
	 * @param _graphics_
	 * 					The graphics the object will be drawn to.
	 */
	public void paint(Graphics _graphics_) {
		graphicRecent = _graphics_;
	}
	
	/**
	 * Paints the object to the most recent graphics.
	 */
	public void repaint() {
		if (graphicRecent != null)
			paint(graphicRecent);
	}
	
	/**
	 * Sets the object to be at the new position.
	 * 
	 * @param _newPos_
	 * 				The new position of the object.
	 */
	public abstract void setPosition(Vector2Int _newPos_);
	/**
	 * Changes the object's position by the passed in vector.
	 * 
	 * @param _moveVec_
	 */
	public abstract void move(Vector2Int _moveVec_);
}
