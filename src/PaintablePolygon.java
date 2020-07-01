import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.awt.Color;

public class PaintablePolygon extends PaintableObject {
	private Polygon poly;		// The Polygon to be painted.
	private Color col;			// The color of the polygon.
	private boolean bIsWire;	// If the polygon is filled (false) or not (true).
	
	/**
	 * Constructs a polygon with the given points.
	 * 
	 * @param _xpoints_
	 * 				The x coordinates (world) of the polygon points.
	 * @param _ypoints_
	 * 				The y coordinates (world) of the polygon points.
	 * @param _npoints_
	 * 				The amount of points the polygon has.
	 */
	public PaintablePolygon(int[] _xpoints_, int[] _ypoints_, int _npoints_){
		this(_xpoints_, _ypoints_, _npoints_, Color.BLACK, false);
	}
	
	/**
	 * Constructs a polygon with the given points and color.
	 * 
	 * @param _xpoints_
	 * 				The x coordinates (world) of the polygon points.
	 * @param _ypoints_
	 * 				The y coordinates (world) of the polygon points.
	 * @param _npoints_
	 * 				The amount of points the polygon has.
	 * @param _col_
	 * 				The color of the polygon.
	 */
	public PaintablePolygon(int[] _xpoints_, int[] _ypoints_, int _npoints_, Color _col_){
		this(_xpoints_, _ypoints_, _npoints_, _col_, false);
	}
	
	/**
	 * Constructs a filled/wire polygon with the given points and color.
	 * 
	 * @param _xpoints_
	 * 				The x coordinates (world) of the polygon points.
	 * @param _ypoints_
	 * 				The y coordinates (world) of the polygon points.
	 * @param _npoints_
	 * 				The amount of points the polygon has.
	 * @param _col_
	 * 				The color of the polygon.
	 * @param _isWire_
	 * 				If the polygon will be wire frame or not.
	 */
	public PaintablePolygon(int[] _xpoints_, int[] _ypoints_, int _npoints_, Color _col_, boolean _isWire_){
		super();
		transform.setModelPoints(buildModelPoints(_xpoints_, _ypoints_, _npoints_));
		
		poly = new Polygon(_xpoints_, _ypoints_, _npoints_);
		col = _col_;
		bIsWire = _isWire_;
		calculateCenteredPosition();
	}
	
	/**
	 * Constructs a filled/wire polygon with the given points and color at a specified position.
	 * 
	 * @param _xpoints_
	 * 				The x coordinates (local) of the polygon points.
	 * @param _ypoints_
	 * 				The y coordinates (local) of the polygon points.
	 * @param _npoints_
	 * 				The amount of points the polygon has.
	 * @param _col_
	 * 				The color of the polygon.
	 * @param _isWire_
	 * 				If the polygon will be wire frame or not.
	 * @param _pos_
	 * 				The position of the polygon.
	 */
	public PaintablePolygon(int[] _xpoints_, int[] _ypoints_, int _npoints_, Color _col_,
		boolean _isWire_, Vector2Int _pos_) {
		super(_pos_);
		transform.setModelPoints(buildModelPoints(_xpoints_, _ypoints_, _npoints_));
		
		// Change all the x and y points to reflect the polygon's position
		int[] movedXPoints = _xpoints_.clone();
		int[] movedYPoints = _ypoints_.clone();
		for (int i = 0; i < _npoints_; ++i) {
			movedXPoints[i] += transform.getScreenPosition().getX();
			movedYPoints[i] += transform.getScreenPosition().getY();
		}
		
		poly = new Polygon(movedXPoints, movedYPoints, _npoints_);
		col = _col_;
		bIsWire = _isWire_;
	}
	
	/**
	 * Helper function for the constructor.
	 * Builds the model points and returns them.
	 * 
	 * @param _xpoints_
	 * 				X position of the points.
	 * @param _ypoints_
	 * 				Y position of the points.
	 * @param _npoints_
	 * 				Amount of points.
	 * 
	 * @return ArrayList<Vector4>
	 */
	private ArrayList<Vector4> buildModelPoints(int[] _xpoints_, int[] _ypoints_, int _npoints_) {
		ArrayList<Vector4> modelPoints = new ArrayList<Vector4>();
		for (int i = 0; i < _npoints_; ++i) {
			Vector4 p = new Vector4(_xpoints_[i], _ypoints_[i], 0);
			modelPoints.add(i, p);
		}
		return modelPoints;
	}
	
	/**
	 * Helper function.
	 * Calculates the x and y position of the polygon based on the specified coordinates.
	 * Meant to be used when the points of the polygon are specified in world coordinates.
	 */
	private void calculateCenteredPosition() {
		Vector2Int pos = new Vector2Int();
		for (int i = 0; i < poly.npoints; ++i) {
			pos = pos.add(new Vector2Int(poly.xpoints[i], poly.ypoints[i]));
		}
		transform.setPosition(pos.scale(1.0 / poly.npoints));
	}
	
	
	/**
	 * Paints the polygon.
	 * 
	 * @param _graphics_
	 * 				The graphics to be painted to.
	 */
	@Override
	public void paint(Graphics _graphics_) {
		super.paint(_graphics_);
		_graphics_.setColor(col);
		if (bIsWire)
			_graphics_.drawPolygon(poly);
		else
			_graphics_.fillPolygon(poly);
	}

	/**
	 * Sets the object to be at the new position.
	 * 
	 * @param _newPos_
	 * 				The new position of the object.
	 */
	@Override
	public void setPosition(Vector2Int _newPos_) {
		// Change all the x and y points to reflect the polygon's new position
		int[] updatedXPoints = poly.xpoints.clone();
		int[] updatedYPoints = poly.ypoints.clone();
		for (int i = 0; i < poly.npoints; ++i) {
			updatedXPoints[i] = updatedXPoints[i] - transform.getScreenPosition().getX() + _newPos_.getX();
			updatedYPoints[i] = updatedYPoints[i] - transform.getScreenPosition().getY() + _newPos_.getY();
		}
		poly.xpoints = updatedXPoints;
		poly.ypoints = updatedYPoints;
		
		transform.setPosition(_newPos_);
		
		this.repaint();
	}

	/**
	 * Changes the object's position by the passed in vector.
	 * 
	 * @param _moveVec_
	 * 				The amount to move the object.
	 */
	@Override
	public void move(Vector2Int _moveVec_) {
		// Move all the points by the move vector
		for (int i = 0; i < poly.npoints; ++i) {
			poly.xpoints[i] += _moveVec_.getX();
			poly.ypoints[i] += _moveVec_.getY();
		}
		transform.translate(_moveVec_);
		
		this.repaint();
	}

	/**
	 * Sets the object to be a different size.
	 * 
	 * @param _newSize_
	 * 				The new size of the object.
	 */
	@Override
	public void setSize(Vector2Int _newSize_) {
		setSize(new Vector4(_newSize_.getX(), _newSize_.getY(), 1));
	}
	/**
	 * Sets the object to be a different size.
	 * 
	 * @param _newSize_
	 * 				The new size of the object.
	 */
	public void setSize(Vector4 _newSize_) {
		transform.setSize(_newSize_);
		
		updatePolygon();
	}

	/**
	 * Changes the object's scale by the passed in vector.
	 * 
	 * @param _scaleVec_
	 * 				The amount to scale the object.
	 */
	@Override
	public void scale(Vector2Int _scaleVec_) {
		scale(new Vector4(_scaleVec_.getX(), _scaleVec_.getY(), 1), new Vector4());
	}
	/**
	 * Changes the object's scale by the passed in vector.
	 * 
	 * @param _scaleVec_
	 * 				The amount to scale the object.
	 */
	public void scale(Vector4 _scaleVec_, Vector4 _pos_) {
		transform.scale(_scaleVec_, _pos_);
		
		updatePolygon();
	}
	
	/**
	 * Helper function.
	 * Updates the polygon's points to reflect the model points.
	 */
	private void updatePolygon() {
		ArrayList<Vector4> modelPoints = transform.getTransformedModelPoints();
		
		//System.out.println("---------------------------------");
		//System.out.println("This Polygon now has the points: ");
		
		// Update the polygon's points
		int[] xpoints = new int[modelPoints.size()];
		int[] ypoints = new int[modelPoints.size()];
		for (int i = 0; i < modelPoints.size(); ++i) {
			xpoints[i] = (int)modelPoints.get(i).getX();
			ypoints[i] = (int)modelPoints.get(i).getY();
			//modelPoints.get(i).print();
		}
		// Update the polygon
		poly.xpoints = xpoints;
		poly.ypoints = ypoints;
		
		this.repaint();
	}
}
