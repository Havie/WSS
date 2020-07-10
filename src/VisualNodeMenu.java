import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class VisualNodeMenu extends HitboxPolygon {
	private ArrayList<VisualNode> alVNodes;	// The node who owns this menu
	private Display display;	// The display the visual node menu is being displayed to
	private HitboxText[] htaOptionText;		// The options text
	private PaintableLine[] plaSeparators;	// The separators
	
	// Display specifications
	// Menu Background
	private final static Color BG_COLOR = new Color(0.93f, 0.93f, 0.93f);
	private final static int[] X_POLYPOINTS = new int[] {0, 0, 200, 200};
	private final static int[] Y_POLYPOINTS = new int[] {0, 200, 200, 0};
	private final static int N_POLYPOINTS = 4;
	// Menu Items
	public final static String HIGHLIGHT = "Highlight";
	public final static String PULL = "Pull";
	public final static String PUSH = "Push";
	public final static String COMMENT = "Comment";
	public final static String DESCRIPTION = "Description";
	public final static String MOVABLE = "Toggle Movable";
	public final static String DISABLE = "Toggle Connections";
	private final static String[] OPTION_NAMES = new String[] { HIGHLIGHT, PULL,
			PUSH, COMMENT, DESCRIPTION, MOVABLE, DISABLE};
	
	private final static Font OPTION_FONT = new Font("Serif", Font.PLAIN, 16);
	private final static Color OPTION_FONT_COL = Color.BLACK;
	private final static Color OPTION_BG_SEL_COL = new Color(1.0f, 1.0f, 1.0f, 1.0f);
	private final static Color OPTION_BG_COL = new Color(0f, 0f, 0f, 0f);
	private final static Vector2Int[] OPTION_POS = new Vector2Int[] {
			new Vector2Int(0, 0), new Vector2Int(0, 30), new Vector2Int(0, 50),
			new Vector2Int(0, 80), new Vector2Int(0, 100), new Vector2Int(0, 130),
			new Vector2Int(0, 160)
	};
	private final static Vector2Int OPTION_HITBOX_TOPLEFT = new Vector2Int(-90, -8);
	private final static Vector2Int OPTION_HITBOX_BOTRIGHT = new Vector2Int(90, 8);
	// Separator Lines
	private final static Color OPTION_LINE_COL = new Color(0.5f, 0.5f, 0.5f);
	private final static Vector2Int[] OPTION_LINE_POS = new Vector2Int[] {
		new Vector2Int(0, 15), new Vector2Int(0, 65), new Vector2Int(0, 115),
		new Vector2Int(0, 145), new Vector2Int(0, 175)
	};
	private final static int OPTION_LINE_LEN = 170;
	private final static int OPTION_LINE_THICK = 2;
	
	/**
	 * Constructs a VisualNodeMenu.
	 * 
	 * @param _vn_
	 * 				The visual node who owns this menu.
	 */
	public VisualNodeMenu(ArrayList<VisualNode> _vns_, Display _display_, Vector2Int _openPos_) {
		super(X_POLYPOINTS, Y_POLYPOINTS, N_POLYPOINTS,
				BG_COLOR, false, _openPos_);
		alVNodes = _vns_;
		
		display = _display_;
		display.addPaintableObj(ppDisplayBox);
		
		// Get the top center of the menu's rectangle
		Vector2Int originPoint = _openPos_.add(new Vector2Int(X_POLYPOINTS[2] / 2, OPTION_FONT.getSize()));
		
		// Create the lines
		Vector2Int lineChange = new Vector2Int(OPTION_LINE_LEN / 2, 0);
		Vector2Int lineLeftOrigin = originPoint.sub(lineChange);
		Vector2Int lineRightOrigin = originPoint.add(lineChange);
		plaSeparators = new PaintableLine[OPTION_LINE_POS.length];
		for (int i = 0; i < plaSeparators.length; ++i) {
			plaSeparators[i] = new PaintableLine(lineLeftOrigin.add(OPTION_LINE_POS[i]),
					lineRightOrigin.add(OPTION_LINE_POS[i]), OPTION_LINE_THICK, OPTION_LINE_COL);
			
			display.addPaintableObj(plaSeparators[i]);
		}
		
		// Create text
		htaOptionText = new HitboxText[OPTION_NAMES.length];
		for (int i = 0; i < htaOptionText.length; ++i) {
			Vector2Int textCenter = originPoint.add(OPTION_POS[i]);
			// Create the text
			htaOptionText[i] = new HitboxText(OPTION_NAMES[i],
					OPTION_FONT, OPTION_FONT_COL, OPTION_HITBOX_TOPLEFT, OPTION_HITBOX_BOTRIGHT,
					OPTION_BG_COL, false, textCenter);
			// Add it to the display
			display.addPaintableObj(htaOptionText[i].getPaintablePoly());
			display.addPaintableObj(htaOptionText[i].getPaintableText());
		}
	}
	
	/**
	 * Remove the polygon from the display.
	 */
	public void destroyMenu() {
		display.removePaintableObj(ppDisplayBox);
		
		for (int i = 0; i < plaSeparators.length; ++i) {
			display.removePaintableObj(plaSeparators[i]);
		}
		
		for (int i = 0; i < htaOptionText.length; ++i) {
			display.removePaintableObj(htaOptionText[i].getPaintablePoly());
			display.removePaintableObj(htaOptionText[i].getPaintableText());
		}
	}
	
	/**
	 * Returns the selected option that the intrusive position is in.
	 * Returns "" on failure.
	 * 
	 * @param _intrusivePos_
	 * 				The position inside one of the options.
	 * @return String
	 */
	public String getSelectedOption(Vector2Int _intrusivePos_) {
		for (int i = 0; i < htaOptionText.length; ++i) {
			if (htaOptionText[i].checkInBound(_intrusivePos_))
				return htaOptionText[i].getPaintableText().getContent();
		}
		
		return "";
	}
	
	/**
	 * Returns the index of the selected option that the intrusive position is in.
	 * Returns -1 on failure.
	 * 
	 * @param _intrusivePos_
	 * 				The position inside one of the options.
	 * @return integer
	 */
	public int getSelectedOptionIndex(Vector2Int _intrusivePos_) {
		for (int i = 0; i < htaOptionText.length; ++i) {
			if (htaOptionText[i].checkInBound(_intrusivePos_))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Highlights the option at the given index. Unhighlights all others.
	 * 
	 * @param _highIndex_
	 * 				Index of the option to be highlighted.
	 */
	public void highlightOption(int _highIndex_) {
		for (int i = 0; i < htaOptionText.length; ++i) {
			htaOptionText[i].getPaintablePoly().setColor(OPTION_BG_COL);
		}
		htaOptionText[_highIndex_].getPaintablePoly().setColor(OPTION_BG_SEL_COL);
	}
	/**
	 * Unhighlights all the options.
	 */
	public void unhighlightOptions() {
		for (int i = 0; i < htaOptionText.length; ++i) {
			htaOptionText[i].getPaintablePoly().setColor(OPTION_BG_COL);
		}
	}
	
	/**
	 * Return the visual nodes associated with the menu.
	 * 
	 * @return ArrayList<VisualNode>
	 */
	public ArrayList<VisualNode> getVisualNodes() { return alVNodes; }
}
