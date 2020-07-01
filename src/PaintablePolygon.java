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
		super();
		poly = new Polygon(_xpoints_, _ypoints_, _npoints_);
		transform.setModelPoints(buildModelPoints(_xpoints_, _ypoints_, _npoints_));
		
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
		super();
		poly = new Polygon();
		transform.setModelPoints(buildModelPoints(_xpoints_, _ypoints_, _npoints_));
		transform.setLocalPosition(_pos_);
		
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
	 * @return ArrayListVec4
	 */
	private ArrayListVec4 buildModelPoints(int[] _xpoints_, int[] _ypoints_, int _npoints_) {
		ArrayListVec4 modelPoints = new ArrayListVec4();
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
		transform.setLocalPosition(pos.scale(1.0 / poly.npoints));
	}
	
	
	/**
	 * Paints the polygon.
	 * 
	 * @param _graphics_
	 * 				The graphics to be painted to.
	 */
	@Override
	public boolean paint(Graphics _graphics_) {
		if (!(super.paint(_graphics_)))
			return false;
		
		_graphics_.setColor(col);
		if (bIsWire)
			_graphics_.drawPolygon(poly);
		else
			_graphics_.fillPolygon(poly);
		return true;
	}
	
	/**
	 * Called when the transform is changed.
	 * Updates the visuals of the paintable object.
	 */
	@Override
	public void updateObjectVisuals() {
		ArrayListVec4 modelPoints = transform.getTransformedModelPoints();
		
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
	}
}
