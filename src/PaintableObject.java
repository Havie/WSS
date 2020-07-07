import java.awt.Graphics;

public abstract class PaintableObject {
	private Graphics graphicRecent;	// The last graphics this object was painted on
	protected Transform transform;	// Transform of the object
	private boolean bRendered;		// If the object is being rendered
	protected boolean bHidden;	// If the object is being hidden
	
	/**
	 * Constructs a PaintableObject.
	 * 
	 */
	public PaintableObject() {
		this(null);
	}
	
	/**
	 * Constructs a PaintableObject with a position and model points.
	 * 
	 * @param _modelPoints_
	 * 				The model points of the object.
	 */
	public PaintableObject(ArrayListVec4 _modelPoints_) {
		graphicRecent = null;
		transform = new Transform(_modelPoints_);
		transform.setPaintableComponent(this);
		bRendered = true;
		bHidden = false;
	}
	
	/**
	 * Paints the object to the provided graphics.
	 * 
	 * @param _graphics_
	 * 					The graphics the object will be drawn to.
	 */
	public boolean paint(Graphics _graphics_) {
		// Don't paint this if it is supposed to be hidden
		if (bHidden) {
			bRendered = false;
			return false;
		}
		
		Vector2Int screenPos = transform.getScreenPosition();
		if (screenPos.getX() > 2000 || screenPos.getX() < 0 ||
				screenPos.getY() > 1100 || screenPos.getY() < 0)
		{
			bRendered = false;
			return false;
		}
		
		graphicRecent = _graphics_;
		
		bRendered = true;
		return true;
	}
	
	/**
	 * Paints the object to the most recent graphics.
	 */
	public void repaint() {		
		if (graphicRecent != null)
			paint(graphicRecent);
	}
	
	/**
	 * Sets if the paint-able object is hidden.
	 * 
	 * @param _hidden_
	 * 				New value of hidden.
	 */
	public void setHidden(boolean _hidden_) { bHidden = _hidden_; }
	
	/**
	 * Returns the transform of the object.
	 * 
	 * @return Transform
	 */
	public Transform getTransform(){ return transform; }
	/**
	 * Returns if the object is rendered or not.
	 * 
	 * @return boolean
	 */
	protected boolean getIsRendered() { return bRendered; }
	
	/**
	 * Called when the transform is changed.
	 * Updates the visuals of the paintable object.
	 */
	public abstract void updateObjectVisuals();
}
