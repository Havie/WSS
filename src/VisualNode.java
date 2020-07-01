import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class VisualNode {
	private PaintablePolygon ppDisplayBox;	// Background of the node
	private PaintableText ptNameText;		// Text of the node
	private Vector2Int v2Pos;				// Position of the node
	private Vector4 v4Scale;				// Scale of the node
	private ArrayList<NodeConnection> alConnections;	// Connections this node has
	
	// Node Display specification
	private final Color NODE_BG_COLOR = Color.BLUE;
	private final int[] X_POLYPOINTS = new int[] {-81, -81, 81, 81};
	private final int[] Y_POLYPOINTS = new int[] {-50, 50, 50, -50};
	private final int N_POLYPOINTS = 4;
	private final Font NODE_FONT = new Font("Arial", Font.PLAIN, 24);
	private final Color NODE_TEXT_COLOR = Color.BLACK;
	
	/**
	 * Constructs a visual node with the specified position.
	 * 
	 * @param _pos_
	 * 				The position of the visual node.
	 */
	public VisualNode(Vector2Int _pos_){	
		this(_pos_, "Node");
	}
	
	/**
	 * Constructs a visual node with the specified position and name.
	 * 
	 * @param _pos_
	 * 				The position of the visual node.
	 * @param _name_
	 * 				The string to be displayed on top of the visual node.
	 */
	public VisualNode(Vector2Int _pos_, String _name_) {
		v2Pos = _pos_;
		v4Scale = new Vector4((X_POLYPOINTS[2] - X_POLYPOINTS[0]) / 2,
				(Y_POLYPOINTS[2] - Y_POLYPOINTS[0]) / 2, 1);
		//System.out.println("Right X Pos: " + X_POLYPOINTS[2]);
		//System.out.println("Left X Pos: " + X_POLYPOINTS[0]);
		//System.out.println("Bot X Pos: " + Y_POLYPOINTS[2]);
		//System.out.println("Top X Pos: " + Y_POLYPOINTS[0]);
		//System.out.println("Right - Left: " + (X_POLYPOINTS[2] - X_POLYPOINTS[0]));
		//System.out.println("Bot - Top: " + (Y_POLYPOINTS[2] - Y_POLYPOINTS[0]));
		//System.out.println("RL/2: " + (X_POLYPOINTS[2] - X_POLYPOINTS[0]) / 2);
		//System.out.println("BT/2: " + (Y_POLYPOINTS[2] - Y_POLYPOINTS[0]) / 2);
		
		// Create node background
		ppDisplayBox = new PaintablePolygon(X_POLYPOINTS, Y_POLYPOINTS, N_POLYPOINTS,
				NODE_BG_COLOR, false, v2Pos);
		// Create node text
		ptNameText = new PaintableText(_name_, NODE_TEXT_COLOR, NODE_FONT, v2Pos);
		
		alConnections = new ArrayList<NodeConnection>();
	}
	
	/**
	 * Adds the node to the passed in display.
	 * 
	 * @param _display_
	 * 				The display the node will be set to be painted on.
	 */
	public void addNodeToDisplay(Display _display_) {
		_display_.addPaintableObj(ppDisplayBox);
		_display_.addPaintableObj(ptNameText);
	}
	
	/**
	 * Sets the position of the visual node and its children.
	 * 
	 * @param _pos_
	 * 				The new position of the visual node.
	 */
	public void setPosition(Vector2Int _pos_) {
		v2Pos = _pos_;
		ppDisplayBox.setPosition(v2Pos);
		ptNameText.setPosition(v2Pos);
		for (NodeConnection con : alConnections) {
			con.updatePosition();
		}
	}
	/**
	 * Returns the position of the visual node and its children.
	 * 
	 * @return Vector2Int
	 */
	public Vector2Int getPosition() { return v2Pos; }
	
	/**
	 * Moves the position of the visual node and its children.
	 * 
	 * @param _moveVec_
	 * 				The vector to update the position by.
	 */
	public void move(Vector2Int _moveVec_) {
		v2Pos = v2Pos.add(_moveVec_);
		ppDisplayBox.move(_moveVec_);
		ptNameText.move(_moveVec_);
		for (NodeConnection con : alConnections) {
			con.updatePosition();
		}
	}
	
	/**
	 * Scales the visual node components.
	 * 
	 * @param _scalar_
	 * 				The amount to scale by.
	 * @param _pos_
	 * 				The position to scale about.
	 */
	public void scale(float _scalar_, Vector2Int _pos_) {
		//System.out.println("Scaling the node by " + _scalar_);
		ppDisplayBox.scale(new Vector4(_scalar_, _scalar_, _scalar_), new Vector4(_pos_));
		ptNameText.scale(new Vector4(_scalar_, _scalar_, _scalar_), new Vector4(_pos_));
		
		v2Pos = ppDisplayBox.transform.getScreenPosition();
		v4Scale = ppDisplayBox.transform.getScreenScale();
		for (NodeConnection con : alConnections) {
			con.updatePosition();
		}
	}
	
	/**
	 * Checks if the passed in position is inside the visual node. Returns the result.
	 * 
	 * @param _intrusivePos_
	 * 				Position that is being tested if it is inside the node.	
	 * @return boolean whether the point is inside the visual nodes bounds.
	 */
	public boolean checkInBound(Vector2Int _intrusivePos_) {
		//System.out.println("-------------");
		//System.out.println("Pos:" + v2Pos.toString());
		//System.out.println("Scale:" + v2Scale.toString());
		
		// Calculate the bounds of the visual node
		Vector2Int topLeftPoint = new Vector2Int((int)(v2Pos.getX() - v4Scale.getX()),
				(int)(v2Pos.getY() - v4Scale.getY()));
		Vector2Int botRightPoint = new Vector2Int((int)(v2Pos.getX() + v4Scale.getX()),
				(int)(v2Pos.getY() + v4Scale.getY()));
		
		//System.out.println("TopLeftPoint: (" + topLeftPoint.toString());
		//System.out.println("BotRightPoint: (" + botRightPoint.toString());
		//System.out.println("IntrusivePos: (" + _intrusivePos_.toString());
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
	
	/**
	 * Adds a connection to the connections list.
	 * 
	 * @param _conn_
	 * 				Connection to add.
	 */
	public void addConnection(NodeConnection _conn_) {
		alConnections.add(_conn_);
	}
}
