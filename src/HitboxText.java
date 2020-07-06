import java.awt.Color;
import java.awt.Font;

public class HitboxText extends HitboxPolygon {
	private PaintableText ptText;	// Paint-able text on top of the hitbox

	/**
	 * Constructs HitboxText.
	 * @param _content_
	 * 				What the text will say.
	 * @param _font_
	 * 				Font of the text.
	 * @param _textCol_
	 * 				Color of the text.
	 * @param _topLeft_
	 * 				The top left corner coordinate of the hitbox.
	 * @param _botRight_
	 * 				The bot right corner coordinate of the hitbox.
	 * @param _color_
	 * 				Color of the polygon.
	 * @param _isWire_
	 * 				If the polygon is wireframe.
	 * @param _pos_
	 * 				Position of the polygon.
	 */
	public HitboxText(String _content_, Font _font_, Color _textCol_,
			Vector2Int _topLeft_, Vector2Int _botRight_, 
			Color _color_, boolean _isWire_, Vector2Int _pos_) {	
		super(new int[] {
				_topLeft_.getX(), _topLeft_.getX(), _botRight_.getX(), _botRight_.getX()
		}, new int[] {
				_topLeft_.getY(), _botRight_.getY(), _botRight_.getY(), _topLeft_.getY()
		}, 4, _color_, _isWire_, _pos_);

		ptText = new PaintableText(_content_, _textCol_, _font_, _pos_);
	}
	
	/**
	 * Returns the PaintableText.
	 * 
	 * @return PaintableText
	 */
	public PaintableText getPaintableText() { return ptText; }
	/**
	 * Returns the PaintablePolygon.
	 * 
	 * @return PaintablePolygon
	 */
	public PaintablePolygon getPaintablePoly() { return ppDisplayBox; }
	/* Uncomment for testing
	public Vector4 getTopLeft() { return ppDisplayBox.getTransform().getTransformedModelPoints().get(0); }
	public Vector4 getBotRight() { return ppDisplayBox.getTransform().getTransformedModelPoints().get(2); }
	public Matrix4x4 getMatrix() { return ppDisplayBox.getTransform().getCompleteTransMatrix(); }
	public Matrix4x4 getLocalMatrix() { return ppDisplayBox.getTransform().getTransformationMatrix(); }
	*/
}
