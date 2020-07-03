import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PaintableText extends PaintableObject {
	private String sContent;	// What the text will say.
	private Color col;			// Color of text.
	private Font font;			// Font of text.
	private Vector2Int drawPos;	// Draw position of the text (drawn from top-left to bottom-right).
	private float fSize;		// Size of the font
	private float fOriginalSize;// Original size of the font
	
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
		super();
		
		sContent = _content_;
		col = _col_;
		font = _font_;
		fSize = font.getSize();
		fOriginalSize = font.getSize();
		
		transform.setLocalPosition(_pos_);
		
		drawPos = createDrawPos();
	}
	
	/**
	 * Helper function to determine where we should draw the text 
	 * from to make the center of the text be at v2Pos.
	 * 
	 * @return Vector2Int position the text should be drawn at.
	 */
	private Vector2Int createDrawPos() {
		int textSize = font.getSize();
		Vector2Int tempDrawPos = transform.getScreenPosition().sub(
				new Vector2Int((int)(textSize / 4.0f * (sContent.length() + 2)), (int)(-textSize / 4.0f)));
		// Calculate the top left corner of where the text should be
		return tempDrawPos;
	}
	
	/**
	 * Paints the object to the provided graphics.
	 * 
	 * @param _graphics_
	 * 				Graphics to draw on.
	 */
	@Override
	public boolean paint(Graphics _graphics_) {
		if (!(super.paint(_graphics_)))
			return false;
		
		// Don't draw the text if its too small
		if (font.getSize() <= 1)
			return false;
		
		Graphics2D g2 = (Graphics2D) _graphics_;
		g2.setColor(col);
		g2.setFont(font);
		g2.drawString(sContent, drawPos.getX(), drawPos.getY());
		
		return true;
	}
	
	/**
	 * Called when the transform is changed.
	 * Updates the visuals of the paintable object.
	 */
	@Override
	public void updateObjectVisuals() {
		fSize = fOriginalSize * transform.getScreenScale().getX();
		
		int potSize = (int) fSize;
		potSize = potSize > 0 ? potSize : 1;
		font = new Font(font.getFontName(), font.getStyle(), potSize);
		
		drawPos = createDrawPos();
	}
	
	/**
	 * Returns the display string.
	 * 
	 * @return String
	 */
	public String getContent() { return sContent; }
	/**
	 * Sets the color of the paint-able text.
	 * 
	 * @param _col_
	 * 				New color.
	 */
	public void setColor(Color _col_) { col = _col_; }
}
