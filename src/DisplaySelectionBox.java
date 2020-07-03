import java.awt.Color;

public class DisplaySelectionBox {
	private PaintablePolygon paintPoly;	// The selection box
	private int[] iaXPoints;	// Points of the polygon
	private int[] iaYPoints;	// Points of the polygon
	private Vector2Int topLeft;	// Top left point of the polygon
	private Vector2Int botRight;// Bot right point of the polygon
	
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
		
		topLeft = _point1_;
		botRight = _point2_;
	}
	
	/**
	 * Sets the first point to the given point.
	 * 
	 * @param _point1_
	 * 				The new position for the first point.
	 */
	public void setPoint1(Vector2Int _point1_) {
		createPolyPoints(_point1_, botRight);
	}
	/**
	 * Sets the second point to the given point.
	 * 
	 * @param _point2_
	 * 				The new position for the second point.
	 */
	public void setPoint2(Vector2Int _point2_) {
		createPolyPoints(topLeft, _point2_);
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
		if (_pos_.getX() > topLeft.getX()) {
			System.out.println("Is to the right of the left side");
			if (_pos_.getX() < botRight.getX()) {
				System.out.println("Is to the left of the right side");
				if (_pos_.getY() > topLeft.getY()) {
					System.out.println("Is below the top side");
					if (_pos_.getY() < botRight.getY()) {
						System.out.println("Is above the bottom side");
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
	 * @param _topLeft_
	 * 				One corner of the polygon.
	 * @param _botRight_
	 * 				Corner diagonal of the other corner of the polygon.
	 */
	private void createPolyPoints(Vector2Int _topLeft_, Vector2Int _botRight_) {
		iaXPoints = new int[] { _topLeft_.getX(), _botRight_.getX(), _botRight_.getX(), _topLeft_.getX() };
		iaYPoints = new int[] { _topLeft_.getY(), _topLeft_.getY(), _botRight_.getY(), _botRight_.getY() };
		
		topLeft = _topLeft_;
		botRight = _botRight_;
		
		paintPoly.setPolyPoints(iaXPoints, iaYPoints, 4);
		paintPoly.setBorderColor(borderCol);
	}
}
