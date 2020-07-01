import java.awt.Graphics;
import java.util.ArrayList;

public abstract class PaintableObject {
	private Graphics graphicRecent;	// The last graphics this object was painted on
	protected Transform transform;	// Transform of the object
	
	/**
	 * Constructs a PaintableObject.
	 */
	public PaintableObject() {
		graphicRecent = null;
		transform = new Transform();
	}
	
	/**
	 * Constructs a PaintableObject with a position.
	 * 
	 * @param _pos_
	 * 				The position of the transform.
	 */
	public PaintableObject(Vector2Int _pos_) {
		graphicRecent = null;
		transform = new Transform();
		transform.setPosition(_pos_);
	}
	
	/**
	 * Constructs a PaintableObject with a position and model points.
	 * 
	 * @param _pos_
	 * 				The position of the transform.
	 * @param _modelPoints_
	 * 				The model points of the object.
	 */
	public PaintableObject(Vector2Int _pos_, ArrayList<Vector4> _modelPoints_) {
		graphicRecent = null;
		transform = new Transform(_modelPoints_);
		transform.setPosition(_pos_);
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
	 * Returns the transform of the object.
	 * 
	 * @return Transform
	 */
	public Transform getTransform(){
		return transform;
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
	 * 				The amount to move the current vector by.
	 */
	public abstract void move(Vector2Int _moveVec_);
	/**
	 * Sets the object to be a different size.
	 * 
	 * @param _newSize_
	 * 				The new size of the object.
	 */
	public abstract void setSize(Vector2Int _newSize_);
	/**
	 * Changes the object's scale by the passed in vector.
	 * 
	 * @param _scaleVec_
	 * 				The amount to scale the object.
	 */
	public abstract void scale(Vector2Int _scaleVec_);
}
