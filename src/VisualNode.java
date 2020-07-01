import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class VisualNode {
	private PaintablePolygon ppDisplayBox;	// Background of the node
	private PaintableText ptNameText;		// Text of the node
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
	 * Constructs a visual node with the specified position and the name of the passed in node.
	 * 
	 * @param _pos_
	 * 				The position of the visual node.
	 */
	public VisualNode(Node n, Vector2Int _pos_){	
		String name = "Node";
		if (n != null) {
			String potName = n.getName();
			if (potName != null) {
				name = potName;
			}
		}
		
		// Create node background
		ppDisplayBox = new PaintablePolygon(X_POLYPOINTS, Y_POLYPOINTS, N_POLYPOINTS,
				NODE_BG_COLOR, false, _pos_);
		// Create node text
		ptNameText = new PaintableText(name, NODE_TEXT_COLOR, NODE_FONT, _pos_);
		
		alConnections = new ArrayList<NodeConnection>();
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
		// Create node background
		ppDisplayBox = new PaintablePolygon(X_POLYPOINTS, Y_POLYPOINTS, N_POLYPOINTS,
				NODE_BG_COLOR, false, _pos_);
		// Create node text
		ptNameText = new PaintableText(_name_, NODE_TEXT_COLOR, NODE_FONT, _pos_);
		
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
		ppDisplayBox.getTransform().setPosition(_pos_);
		ptNameText.getTransform().setPosition(_pos_);
	}
	/**
	 * Returns the position of the visual node and its children.
	 * 
	 * @return Vector2Int
	 */
	public Vector2Int getPosition() { return ppDisplayBox.getTransform().getScreenPosition(); }
	
	/**
	 * Moves the position of the visual node and its children.
	 * 
	 * @param _moveVec_
	 * 				The vector to update the position by.
	 */
	public void move(Vector2Int _moveVec_) {
		ppDisplayBox.getTransform().translate(_moveVec_);
		ptNameText.getTransform().translate(_moveVec_);
		updateConnections();
	}
	
	/**
	 * Updates the positions of the connections.
	 */
	public void updateConnections() {
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
		ppDisplayBox.getTransform().scale(new Vector4(_scalar_, _scalar_, _scalar_), new Vector4(_pos_));
		ptNameText.getTransform().scale(new Vector4(_scalar_, _scalar_, _scalar_), new Vector4(_pos_));
		
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
	
	/**
	 * Adds a connection to the connections list.
	 * 
	 * @param _conn_
	 * 				Connection to add.
	 */
	public void addConnection(NodeConnection _conn_) {
		alConnections.add(_conn_);
	}
	
	/**
	 * Returns the transform of the background box.
	 * 
	 * @return Transform.
	 */
	public Transform getPolyTrans() {
		return ppDisplayBox.getTransform();
	}
	/**
	 * Returns the transform of the text.
	 * 
	 * @return Transform.
	 */
	public Transform getTextTrans() {
		return ptNameText.getTransform();
	}
}
