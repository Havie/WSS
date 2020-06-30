import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PaintableLine extends PaintableObject {
	private Vector2Int v2Pos1;	// Start position of the line.
	private Vector2Int v2Pos2;	// End position of the line.
	private Color col;			// Color of the line.
	private float fBaseThick;	// Starting thicknes of the line.
	private float fThickness;	// Thickness of the line.
	
	/**
	 * Constructs a paintable line. End point and start point are 0, 0 and color is black.
	 */
	public PaintableLine() {
		this(new Vector2Int(),new Vector2Int(),Color.BLACK);
	}
	
	/**
	 * Constructs a paintable line with an end point and start point. Colored black.
	 * 
	 * @param _pos1_
	 * 				Start position of the line.
	 * @param _pos2_
	 * 				End position of the line.
	 */
	public PaintableLine(Vector2Int _pos1_, Vector2Int _pos2_){
		this(_pos1_, _pos2_, Color.BLACK);
	}
	
	/**
	 * Constructs a paintable line with an end point, start point, and color.
	 * 
	 * @param _pos1_
	 * 				Start position of the line.
	 * @param _pos2_
	 * 				End position of the line.
	 * @param _col_
	 * 				Color of the line.
	 */
	public PaintableLine(Vector2Int _pos1_, Vector2Int _pos2_, Color _col_) {
		this(_pos1_, _pos2_, _col_, 1);
	}
	
	/**
	 * Constructs a paintable line with an end point, start point, color, and thickness.
	 * 
	 * @param _pos1_
	 * 				Start position of the line.
	 * @param _pos2_
	 * 				End position of the line.
	 * @param _col_
	 * 				Color of the line.
	 * @param _thickness_
	 * 				Thickness of the line.
	 */
	public PaintableLine(Vector2Int _pos1_, Vector2Int _pos2_, Color _col_, int _thickness_) {
		super();
		v2Pos1 = _pos1_.clone();
		v2Pos2 = _pos2_.clone();
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
	public void paint(Graphics _graphics_){
		super.paint(_graphics_);
		Graphics2D g2 = (Graphics2D) _graphics_;
		g2.setColor(col);
		g2.setStroke(new BasicStroke(fThickness));
		g2.drawLine(v2Pos1.getX(), v2Pos1.getY(), v2Pos2.getX(), v2Pos2.getY());
	}

	/**
	 * Sets the start position of the line to be at the new position.
	 * 
	 * @param _newPos_
	 * 				The new start position of the line.
	 */
	@Override
	public void setPosition(Vector2Int _newPos_) {
		v2Pos1 = _newPos_;
		
		this.repaint();
	}
	
	/**
	 * Sets the start and end position of the line to be at the new positions.
	 * 
	 * @param _newPos1_
	 * 				The new start position of the line.
	 * @param _newPos2_
	 * 				The new end position of the line.
	 */
	public void setPosition(Vector2Int _newPos1_, Vector2Int _newPos2_) {
		v2Pos1 = _newPos1_;
		v2Pos2 = _newPos2_;
		
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
		v2Pos1 = v2Pos1.add(_moveVec_);
		v2Pos2 = v2Pos2.add(_moveVec_);
		
		this.repaint();
	}

	@Override
	public void setSize(Vector2Int _newSize_) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Changes the object's scale by the passed in vector.
	 * 
	 * @param _scaleVec_
	 * 				The amount to scale the object.
	 */
	@Override
	public void scale(Vector2Int _scaleVec_) {
		scale(new Vector4(_scaleVec_));
	}
	
	/**
	 * Changes the object's scale by the passed in vector.
	 * 
	 * @param _scalar_
	 * 				The amount to scale the object.
	 */
	public void scale(float _scalar_) {
		scale(new Vector4(_scalar_, _scalar_, 1, 1));
	}
	
	/**
	 * Changes the object's scale by the passed in vector.
	 * 
	 * @param _scaleVec_
	 * 				The amount to scale the object.
	 */
	public void scale(Vector4 _scaleVec_) {
		transform.scale(_scaleVec_, new Vector4());
		
		updateLine();
	}
	
	/**
	 * Helper function.
	 * Updates the line's size.
	 */
	private void updateLine() {
		float potSize = transform.getScreenScale().getX();
		if (potSize > 0)
			fThickness = potSize * fBaseThick;
		System.out.println(fThickness);
	}

}
