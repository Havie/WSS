import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PaintableConnection extends PaintableObject {
	private Transform tAnchor1;	// Start anchor of the line.
	private Transform tAnchor2;	// End anchor of the line.
	private Color col;			// Color of the line.
	private float fBaseThick;	// Starting thicknes of the line.
	private float fThickness;	// Thickness of the line.
	
	/**
	 * Constructs a paintable line with an end point and start point. Colored black.
	 * 
	 * @param _anchor1_
	 * 				Start anchor of the line.
	 * @param _anchor2_
	 * 				End anchor of the line.
	 */
	public PaintableConnection(Transform _anchor1_, Transform _anchor2_){
		this(_anchor1_, _anchor2_, Color.BLACK);
	}
	
	/**
	 * Constructs a paintable line with an end point, start point, and color.
	 * 
	 * @param _anchor1_
	 * 				Start anchor of the line.
	 * @param _anchor2_
	 * 				End anchor of the line.
	 * @param _col_
	 * 				Color of the line.
	 */
	public PaintableConnection(Transform _anchor1_, Transform _anchor2_, Color _col_) {
		this(_anchor1_, _anchor2_, _col_, 1);
	}
	
	/**
	 * Constructs a paintable line with an end point, start point, color, and thickness.
	 * 
	 * @param _anchor1_
	 * 				Start anchor of the line.
	 * @param _anchor2_
	 * 				End anchor of the line.
	 * @param _col_
	 * 				Color of the line.
	 * @param _thickness_
	 * 				Thickness of the line.
	 */
	public PaintableConnection(Transform _anchor1_, Transform _anchor2_, Color _col_, int _thickness_) {
		super();
		tAnchor1 = _anchor1_;
		tAnchor2 = _anchor2_;
		col = _col_;
		fBaseThick = _thickness_;
		fThickness = _thickness_;
	}
	
	/**
	 * Override of paint function. Paints a line from pos1 to pos2.
	 * 
	 * @param _graphics_
	 * 				Graphics to draw the line to.
	 */
	@Override
	public boolean paint(Graphics _graphics_){
		if (!(super.paint(_graphics_)))
			return false;
		
		Graphics2D g2 = (Graphics2D) _graphics_;
		g2.setColor(col);
		g2.setStroke(new BasicStroke(fThickness));
		
		Vector2Int pos1 = tAnchor1.getScreenPosition();
		Vector2Int pos2 = tAnchor2.getScreenPosition();
		g2.drawLine(pos1.getX(), pos1.getY(), pos2.getX(), pos2.getY());
		
		return true;
	}
	
	/**
	 * Called when the transform is changed.
	 * Updates the visuals of the paintable object.
	 */
	@Override
	public void updateObjectVisuals() {
		float potSize = transform.getScreenScale().getX();
		if (potSize > 0)
			fThickness = potSize * fBaseThick;
	}

}
