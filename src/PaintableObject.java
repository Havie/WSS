import java.awt.Graphics;

public abstract class PaintableObject {
	private Graphics graphicRecent;	// The last graphics this object was painted on
	protected Transform transform;	// Transform of the object
	
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
	}
	
	/**
	 * Paints the object to the provided graphics.
	 * 
	 * @param _graphics_
	 * 					The graphics the object will be drawn to.
	 */
	public boolean paint(Graphics _graphics_) {
		Vector2Int screenPos = transform.getScreenPosition();
		if (screenPos.getX() > 2000 || screenPos.getX() < 0 ||
				screenPos.getY() > 1100 || screenPos.getY() < 0)
			return false;
		
		graphicRecent = _graphics_;
		
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
	 * Returns the transform of the object.
	 * 
	 * @return Transform
	 */
	public Transform getTransform(){
		return transform;
	}
	
	/**
	 * Called when the transform is changed.
	 * Updates the visuals of the paintable object.
	 */
	public abstract void updateObjectVisuals();
}
