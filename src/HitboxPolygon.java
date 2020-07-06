import java.awt.Color;

public class HitboxPolygon {
	protected PaintablePolygon ppDisplayBox;
	
	/**
	 * Constructs a HitboxPolygon.
	 * 
	 * @param _xPoints_
	 * 				x values of the points of the polygon.
	 * @param _yPoints_
	 * 				y values of the points of the polygon.
	 * @param _nPoints_
	 * 				The number of points of the polygon.
	 * @param _color_
	 * 				Color of the polygon.
	 * @param _isWire_
	 * 				If the polygon is wire.
	 * @param _pos_
	 * 				Position of the polygon.
	 */
	public HitboxPolygon(int[] _xPoints_, int[] _yPoints_, int _nPoints_,
			Color _color_, boolean _isWire_, Vector2Int _pos_) {
		ppDisplayBox = new PaintablePolygon(_xPoints_, _yPoints_, _nPoints_,
				_color_, _isWire_, _pos_);
	}
	
	/**
	 * Checks if the passed in position is inside the visual node. Returns the result.
	 * 
	 * @param _intrusivePos_
	 * 				Position that is being tested if it is inside the node.	
	 * @return boolean whether the point is inside the visual nodes bounds.
	 */
	public boolean checkInBound(Vector2Int _intrusivePos_) {
		ArrayListVec4 modelPoints = ppDisplayBox.getTransform().getTransformedModelPoints();
		
		
		// Calculate the bounds of the visual node
		Vector4 topLeftPoint = modelPoints.get(0);
		Vector4 botRightPoint = modelPoints.get(2);
		
		// Check
		if (_intrusivePos_.getX() > topLeftPoint.getX()) {
			//System.out.println("Is to the right of the left side");
			if (_intrusivePos_.getX() < botRightPoint.getX()) {
				//System.out.println("Is to the left of the right side");
				if (_intrusivePos_.getY() > topLeftPoint.getY()) {
					//System.out.println("Is below the top side");
					if (_intrusivePos_.getY() < botRightPoint.getY()) {
						//System.out.println("Is above the bottom side");
						return true;
					}
				}
			}
		}
		return false;
	}
}
