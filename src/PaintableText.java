import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class PaintableText extends PaintableObject {
	private String sContent;	// What the text will say.
	private Color col;			// Color of text.
	private Font font;			// Font of text.
	private Vector2Int drawPos;	// Draw position of the text (drawn from top-left to bottom-right).
	
	// Future implementation, shrink the font size if it exceeds these
	//private int maxCharPerLine;	// The maximum amount of characters allowed to be on a single line.
	//private int maxLines;		// The maximum amount of lines there are allowed to be.
	
	/**
	 * Constructs a PaintableText.
	 * 
	 * @param _content_
	 * 				What the text will say.
	 * @param _col_
	 * 				The color of the text.
	 * @param _font_
	 * 				The font of the text.
	 * @param _pos_
	 * 				The position of the text.
	 */
	public PaintableText(String _content_, Color _col_, Font _font_, Vector2Int _pos_) {
		sContent = _content_;
		col = _col_;
		font = _font_;
		v2Pos = _pos_;
		drawPos = createDrawPos();
	}
	
	/**
	 * Paints the object to the provided graphics.
	 * 
	 * @param _graphics_
	 * 				Graphics to draw on.
	 */
	@Override
	public void paint(Graphics _graphics_) {
		super.paint(_graphics_);
		_graphics_.setColor(col);
		_graphics_.setFont(font);
		_graphics_.drawString(sContent, drawPos.getX(), drawPos.getY());
	}
	
	/**
	 * Helper function to determine where we should draw the text 
	 * from to make the center of the text be at v2Pos.
	 * 
	 * @return Vector2Int position the text should be drawn at.
	 */
	private Vector2Int createDrawPos() {
		int textSize = font.getSize();
		// Calculate the top left corner of where the text should be
		return v2Pos.sub(new Vector2Int(textSize / 4 * (sContent.length() + 1), -textSize / 4));
	}

	/**
	 * Sets the object to be at the new position.
	 * 
	 * @param _newPos_
	 * 				The new position of the object.
	 */
	@Override
	public void setPosition(Vector2Int _newPos_) {
		v2Pos = _newPos_;
		drawPos = createDrawPos();
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
		v2Pos = v2Pos.add(_moveVec_);
		drawPos = drawPos.add(_moveVec_);
		this.repaint();
	}

}
