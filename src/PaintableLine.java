import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PaintableLine extends PaintableObject {
	private Vector2Int v2Point1;	// Point 1 of the line
	private Vector2Int v2Point2;	// Point 2 of the line
	private float fThickness;		// Thickness of the line
	private Color col;				// Color of the line
	
	/**
	 * Constructs a PaintalbeLine.
	 * Default.
	 */
	public PaintableLine() {
		this(new Vector2Int(), new Vector2Int());
	}
	
	/**
	 * Constructs a PaintableLine.
	 * 
	 * @param _pos1_
	 * 				Point 1 of the line.
	 * @param _pos2_
	 * 				Point 2 of the line.
	 */
	public PaintableLine(Vector2Int _pos1_, Vector2Int _pos2_) {
		this(_pos1_, _pos2_, 1, Color.BLACK);
	}
	
	/**
	 * Constructs a PaintableLine.
	 * Full.
	 * 
	 * @param _pos1_
	 * 				Point 1 of the line.
	 * @param _pos2_
	 * 				Point 2 of the line.
	 * @param _thick_
	 * 				Thickness of the line.
	 * @param _col_
	 * 				Color of the line.
	 */
	public PaintableLine(Vector2Int _pos1_, Vector2Int _pos2_, float _thick_, Color _col_) {
		v2Point1 = _pos1_.clone();
		v2Point2 = _pos2_.clone();
		fThickness = _thick_;
		col = _col_;
	}
	
	
	@Override
	public void updateObjectVisuals() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Override of paint function. Paints a line from pos1 to pos2.
	 * 
	 * @param _graphics_
	 * 				Graphics to draw the line to.
	 */
	@Override
	public boolean paint(Graphics _graphics_){
		super.paint(_graphics_);
		
		Graphics2D g2 = (Graphics2D) _graphics_;
		g2.setColor(col);
		g2.setStroke(new BasicStroke(fThickness));
		
		g2.drawLine(v2Point1.getX(), v2Point1.getY(), v2Point2.getX(), v2Point2.getY());
		
		return true;
	}

}
