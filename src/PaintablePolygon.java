import java.awt.Graphics;
import java.awt.Polygon;
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
		v2Pos = _pos_;
		
		// Change all the x and y points to reflect the polygon's position
		int[] movedXPoints = _xpoints_.clone();
		int[] movedYPoints = _ypoints_.clone();
		for (int i = 0; i < _npoints_; ++i) {
			movedXPoints[i] += v2Pos.getX();
			movedYPoints[i] += v2Pos.getY();
		}
		
		poly = new Polygon(movedXPoints, movedYPoints, _npoints_);
		col = _col_;
		bIsWire = _isWire_;
	}
	
	/**
	 * Calculates the x and y position of the polygon based on the specified coordinates.
	 * Meant to be used when the points of the polygon are specified in world coordinates.
	 */
	private void calculateCenteredPosition() {
		v2Pos = new Vector2Int();
		for (int i = 0; i < poly.npoints; ++i) {
			v2Pos = v2Pos.add(new Vector2Int(poly.xpoints[i], poly.ypoints[i]));
		}
		v2Pos = v2Pos.scale(1.0 / poly.npoints);
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
			updatedXPoints[i] = updatedXPoints[i] - v2Pos.getX() + _newPos_.getX();
			updatedYPoints[i] = updatedYPoints[i] - v2Pos.getY() + _newPos_.getY();
		}
		poly.xpoints = updatedXPoints;
		poly.ypoints = updatedYPoints;
		
		v2Pos = _newPos_;
		
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
		v2Pos = v2Pos.add(_moveVec_);
		
		this.repaint();
	}
}
