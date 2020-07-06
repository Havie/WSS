import java.awt.Color;

public class DisplaySelectionBox {
	private PaintablePolygon paintPoly;	// The selection box
	private int[] iaXPoints;	// Points of the polygon
	private int[] iaYPoints;	// Points of the polygon
	private Vector2Int v2FirstPoint;	// First point of the polygon
	private Vector2Int v2SecondPoint;	// Second point of the polygon
	
	// Constants
	private final Color borderCol = new Color(0.0f, 0.5f, 0.85f);
	private final Color col = new Color(0.0f, 0.5f, 0.85f, 0.4f);
	
	/**
	 * Constructs a DisplaySelectionBox.
	 * Default.
	 */
	DisplaySelectionBox() {
		this(new Vector2Int(), new Vector2Int());
	}
	/**
	 * Constructs a DisplaySelectionBox with both points initialized as the given point.
	 * 
	 * @param _point_
	 * 				The initial point for both points of the box.
	 */
	DisplaySelectionBox(Vector2Int _point_) {		
		this(_point_, new Vector2Int(_point_.getX(), _point_.getY()));
	}
	/**
	 * Constructs a DisplaySelection Box with the given points
	 * 
	 * @param _point1_
	 * 				First point of the box.
	 * @param _point2_
	 * 				Second point of the box.
	 */
	DisplaySelectionBox(Vector2Int _point1_, Vector2Int _point2_) {
		paintPoly = new PaintablePolygon(new int[]{}, new int[]{}, 0);
		
		createPolyPoints(_point1_, _point2_);
		
		paintPoly.setBorderColor(borderCol);
		paintPoly.setColor(col);
		
		v2FirstPoint = _point1_;
		v2SecondPoint = _point2_;
	}
	
	/**
	 * Sets the first point to the given point.
	 * 
	 * @param _point1_
	 * 				The new position for the first point.
	 */
	public void setPoint1(Vector2Int _point1_) {
		createPolyPoints(_point1_, v2SecondPoint);
	}
	/**
	 * Sets the second point to the given point.
	 * 
	 * @param _point2_
	 * 				The new position for the second point.
	 */
	public void setPoint2(Vector2Int _point2_) {
		createPolyPoints(v2FirstPoint, _point2_);
	}
	
	/**
	 * Adds the paint-able polygon to the display.
	 * 
	 * @param _display_
	 * 				The display the polygon will be painted to.
	 */
	public void addToDisplay(Display _display_) {
		_display_.addPaintableObj(paintPoly);
	}
	/**
	 * Removes the paint-able polygon from the display.
	 * 
	 * @param _display_
	 * 				The display the polygon will be removed from.
	 */
	public void removeFromDisplay(Display _display_) {
		_display_.removePaintableObj(paintPoly);
	}
	
	/**
	 * Tests if the given point is in the bounds of the selection box.
	 * 
	 * @param _pos_
	 * 				The position to test.
	 * @return boolean
	 */
	public boolean testInBound(Vector2Int _pos_) {
		int xMin = v2FirstPoint.getX();
		int xMax = v2SecondPoint.getX();
		
		if (v2FirstPoint.getX() > v2SecondPoint.getX()) {
			xMin = v2SecondPoint.getX();
			xMax = v2FirstPoint.getX();
		}
		
		int yMin = v2FirstPoint.getY();
		int yMax = v2SecondPoint.getY();
		
		if (v2FirstPoint.getY() > v2SecondPoint.getY()) {
			yMin = v2SecondPoint.getY();
			yMax = v2FirstPoint.getY();
		}
		
		Vector2Int topLeft = new Vector2Int(xMin, yMin);
		Vector2Int botRight = new Vector2Int(xMax, yMax);
		

		
		if (_pos_.getX() > topLeft.getX()) {
			//System.out.println("FurtherRight");
			if (_pos_.getX() < botRight.getX()) {
				//System.out.println("FurtherLeft");
				if (_pos_.getY() > topLeft.getY()) {
					//System.out.println("FurtherDown");
					if (_pos_.getY() < botRight.getY()) {
						//System.out.println("FurtherUp");
						//Driver.print("Comparing: " + _pos_.toString());
						//Driver.print("TopLeft: " + topLeft.toString());
						//Driver.print("BotRight: " + botRight.toString());
						return true;
					}
				}
			}
		}
		return false;
		
	}
	
	/**
	 * Helper function to create the points of the polygon from two Vector2Ints.
	 * 
	 * @param _firstPoint_
	 * 				One corner of the polygon.
	 * @param _secondPoint_
	 * 				Corner diagonal of the other corner of the polygon.
	 */
	private void createPolyPoints(Vector2Int _firstPoint_, Vector2Int _secondPoint_) {
		iaXPoints = new int[] { _firstPoint_.getX(), _secondPoint_.getX(), _secondPoint_.getX(), _firstPoint_.getX() };
		iaYPoints = new int[] { _firstPoint_.getY(), _firstPoint_.getY(), _secondPoint_.getY(), _secondPoint_.getY() };
		
		v2FirstPoint = _firstPoint_;
		v2SecondPoint = _secondPoint_;
		
		paintPoly.setPolyPoints(iaXPoints, iaYPoints, 4);
		paintPoly.setBorderColor(borderCol);
	}
}
