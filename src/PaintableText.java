import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class PaintableText extends PaintableObject {
	private String sContent;	// What the text will say.
	private Color col;			// Color of text.
	private Font font;			// Font of text.
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
		
		FontMetrics fm = g2.getFontMetrics();
		Vector2Int screenPos = transform.getScreenPosition();
		int x = -fm.stringWidth(sContent) / 2 + screenPos.getX();
		int y = -fm.getHeight() / 2 + fm.getAscent() + screenPos.getY();
		
		g2.drawString(sContent, x, y);
		
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
	}
	
	/**
	 * Returns the display string.
	 * 
	 * @return String
	 */
	public String getContent() { return sContent; }
	/**
	 * Sets the content of the text.
	 * 
	 * @param _content_
	 * 				New display content.
	 */
	public void setContent(String _content_) { sContent = _content_; }
	/**
	 * Sets the color of the paint-able text.
	 * 
	 * @param _col_
	 * 				New color.
	 */
	public void setColor(Color _col_) { col = _col_; }
}
