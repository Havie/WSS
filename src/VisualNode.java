import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

public class VisualNode extends HitboxPolygon {
	private PaintableText ptNameText;		// Text of the node
	private ArrayList<NodeConnection> alConnections;	// Connections this node has
	private boolean bHighlighted;	// If the node is highlighted
	private boolean bIsRoot;		// If the node is a root node
	private boolean bIsSelected;	// If the node is selected
	
	private String sComment;		// A comment the user can write
	private String sDescription;	// A description the user can write
	
	private boolean bIsMovable;		// If this node is movable
	private boolean bConnectionsVisible;	// If the connections for this node are supposed to be visible
	
	private PaintablePolygon ppLeftAnchor;	// Left (to) anchor
	private PaintablePolygon ppRightAnchor; // Right (from) anchor
	
	// Node Display specification
	private final static Color NODE_BG_COLOR = new Color(0.1f, 0.4f, 0.9f);
	private final static int[] X_POLYPOINTS = new int[] {-81, -81, 81, 81};
	private final static int[] Y_POLYPOINTS = new int[] {-50, 50, 50, -50};
	private final static int N_POLYPOINTS = 4;
	private final Font NODE_FONT = new Font("Arial", Font.PLAIN, 24);
	private final Color NODE_TEXT_COLOR = Color.WHITE;
	
	private final Color NODE_HIGHLIGHT_COLOR = new Color(0.8f, 0.8f, 0.2f);
	private final Color NODE_ROOT_COLOR = new Color(0.6f, 0.1f, 0.7f);
	private final Color NODE_SEL_COLOR = new Color(0.1f, 0.8f, 0.2f);
	private final Color NODE_IMMOVE_COLOR = new Color(0.8f, 0.15f, 0.15f);
	
	private final Vector2Int LEFT_OFFSET = new Vector2Int(-81, 0);
	private final Vector2Int RIGHT_OFFSET = new Vector2Int(81, 0);
	
	/**
	 * Constructs a visual node with the specified position.
	 * 
	 * @param _pos_
	 * 				The position of the visual node.
	 */
	public VisualNode(Vector2Int _pos_){		
		// Create node background
		super(X_POLYPOINTS, Y_POLYPOINTS, N_POLYPOINTS,
				NODE_BG_COLOR, false, _pos_);
		// Create node text
		ptNameText = new PaintableText("Node", NODE_TEXT_COLOR, NODE_FONT, _pos_);
		
		alConnections = new ArrayList<NodeConnection>();
		
		bHighlighted = false;
		bIsRoot = false;
		
		sComment = "";
		sDescription = "";
		
		bIsMovable = true;
		bConnectionsVisible = true;
		
		// Create the left and right anchors
		ppLeftAnchor = new PaintablePolygon(new int[]{}, new int[]{}, 0);
		ppLeftAnchor.getTransform().setPosition(_pos_.add(LEFT_OFFSET));
		
		ppRightAnchor = new PaintablePolygon(new int[]{}, new int[]{}, 0);
		ppRightAnchor.getTransform().setPosition(_pos_.add(RIGHT_OFFSET));
	}
	
	/**
	 * Constructs a visual node with the specified position and the name of the passed in node.
	 * 
	 * @param _n_
	 * 				The node whose data to base the new node off of.
	 * @param _pos_
	 * 				The position of the visual node.
	 */
	public VisualNode(Node _n_, Vector2Int _pos_){	
		this(_pos_);
		
		// Set the name of the node
		String name = "Node";
		if (_n_ != null) {
			String potName = _n_.getName();
			if (potName != null) {
				name = potName;
			}
		}
		ptNameText.setContent(name);
		
		// If the node is a root, toggle it
		if (_n_.getIsRoot())
			toggleRootStatus();
		// If the node is movable, toggle it
		this.setIsMovable(_n_.getIsMoveable());
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
		this(_pos_);
		// Set the content of the text
		ptNameText.setContent(_name_);
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
		_display_.addPaintableObj(ppLeftAnchor);
		_display_.addPaintableObj(ppRightAnchor);
	}
	
	/**
	 * Removes the node from the passed in display.
	 * 
	 * @param _display_
	 * 				The display the node will be removed from.
	 */
	public void removeNodeFromDisplay(Display _display_) {
		_display_.removePaintableObj(ppDisplayBox);
		_display_.removePaintableObj(ptNameText);
		_display_.removePaintableObj(ppLeftAnchor);
		_display_.removePaintableObj(ppRightAnchor);
	}
	
	/**
	 * Sets the position of the visual node and its children.
	 * 
	 * @param _pos_
	 * 				The new position of the visual node.
	 */
	public void setPosition(Vector2Int _pos_) {
		if (bIsMovable) {
			ppDisplayBox.getTransform().setPosition(_pos_);
			ptNameText.getTransform().setPosition(_pos_);
			updateConnections();
		}
	}
	
	/**
	 * Returns the position of the visual node and its children.
	 * 
	 * @return Vector2Int
	 */
	public Vector2Int getScreenPosition() { return ppDisplayBox.getTransform().getScreenPosition(); }
	/**
	 * Returns the world position of the visual node.
	 * 
	 * @return Vector4
	 */
	public Vector4 getWorldPosition() { return ppDisplayBox.getTransform().getTransformationMatrix().extractPosition(); }
	
	/**
	 * Moves the position of the visual node and its children.
	 * 
	 * @param _moveVec_
	 * 				The vector to update the position by.
	 */
	public void move(Vector2Int _moveVec_) {
		if (bIsMovable) {
			ppDisplayBox.getTransform().translate(_moveVec_);
			ptNameText.getTransform().translate(_moveVec_);
			updateConnections();
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
		if (bIsMovable) {
			//System.out.println("Scaling the node by " + _scalar_);
			ppDisplayBox.getTransform().scale(new Vector4(_scalar_, _scalar_, _scalar_), new Vector4(_pos_));
			ptNameText.getTransform().scale(new Vector4(_scalar_, _scalar_, _scalar_), new Vector4(_pos_));
			updateConnections();
		}
	}
	
	/**
	 * Updates the positions of the connections.
	 */
	public void updateConnections() {	
		Vector2Int pos = new Vector2Int(ppDisplayBox.getTransform().getLocalPosition());
		ppLeftAnchor.getTransform().setLocalPosition(pos.add(LEFT_OFFSET));
		ppRightAnchor.getTransform().setLocalPosition(pos.add(RIGHT_OFFSET));
		
		for (NodeConnection con : alConnections) {
			con.updatePosition();
		}
	}
	
	/**
	 * Toggles if the node is highlighted.
	 * Changes the color of the node outline and its connections.
	 */
	public void highlightNode() {
		bHighlighted = !bHighlighted;
		if (bHighlighted) {
			ppDisplayBox.setBorderColor(NODE_HIGHLIGHT_COLOR);
		}
		else {
			ppDisplayBox.setBorderColor(NODE_BG_COLOR);
		}
		for (NodeConnection nc : alConnections)
			nc.highlight();
	}
	
	/**
	 * Toggles if the node is a root.
	 * Changes the color of the node.
	 */
	public void toggleRootStatus() {
		bIsRoot = !bIsRoot;
		if (bIsRoot)
			ppDisplayBox.setColor(NODE_ROOT_COLOR);
		else
			ppDisplayBox.setColor(NODE_BG_COLOR);
	}
	
	/**
	 * Toggles if the ndoe is selected.
	 * Changes the color of the text.
	 */
	public void toggleSelectedStatus() {
		bIsSelected = !bIsSelected;
		// Don't update color if it is immovable
		if (bIsMovable) {
			if (bIsSelected)
				ptNameText.setColor(NODE_SEL_COLOR);
			else
				ptNameText.setColor(NODE_TEXT_COLOR);
		}
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
	 * Sets the comment.
	 * 
	 * @param _comment_
	 * 				New comment.
	 */
	public void setComment(String _comment_) { sComment = _comment_; }
	/**
	 * Sets the description.
	 * 
	 * @param _description_
	 * 				New description.
	 */
	public void setDescription(String _description_) { sDescription = _description_; }
	/**
	 * Sets movable.
	 * 
	 * @param _isMovable_
	 * 				New value of movable.
	 */
	public void setIsMovable(boolean _isMovable_) {
		bIsMovable = _isMovable_;
		if (bIsMovable)
			ptNameText.setColor(NODE_TEXT_COLOR);
		else
			ptNameText.setColor(NODE_IMMOVE_COLOR);
	}
	
	/**
	 * Sets the connections visible to the passed in value.
	 * 
	 * @param _connVis_
	 * 				New connections visible status.
	 */
	public void setConnectionsVisible(boolean _connVis_) {
		bConnectionsVisible = _connVis_;
		
		for (NodeConnection nc : alConnections) {
			nc.updateHiddenStatus();
		}
	}
	
	/**
	 * Returns the transform of the background box.
	 * 
	 * @return Transform.
	 */
	public Transform getPolyTrans() { return ppDisplayBox.getTransform(); }
	/**
	 * Returns the transform of the text.
	 * 
	 * @return Transform.
	 */
	public Transform getTextTrans() { return ptNameText.getTransform(); }
	/**
	 * Returns the name of the node (text being displayed).
	 * 
	 * @return String
	 */
	public String getName() { return ptNameText.getContent(); }
	/**
	 * Returns if the node is highlighted.
	 * 
	 * @return boolean
	 */
	public boolean getIsHighlighted() { return bHighlighted; }
	/**
	 * Returns if the node is selected.
	 * 
	 * @return boolean
	 */
	public boolean getIsSelected() { return bIsSelected; }
	/**
	 * Returns if the node is a root node.
	 * 
	 * @return boolean
	 */
	public boolean getIsRoot() { return bIsRoot; }
	/**
	 * Returns the connections.
	 * 
	 * @return ArrayList<NodeConnection>
	 */
	public ArrayList<NodeConnection> getConnections() { return alConnections; }
	/**
	 * Returns the comment.
	 * 
	 * @return String
	 */
	public String getComment() { return sComment; }
	/**
	 * Returns the description.
	 * 
	 * @return String
	 */
	public String getDescription() { return sDescription; }
	/**
	 * Returns if the visual node is movable.
	 * 
	 * @return boolean
	 */
	public boolean getIsMovable() { return bIsMovable; }
	/**
	 * Returns if the connections are visible.
	 * 
	 * @return boolean
	 */
	public boolean getConnectionsVisible() { return bConnectionsVisible; }
	/**
	 * Returns the left (to) anchor.
	 * 
	 * @return Transform
	 */
	public Transform getLeftAnchor() { return ppLeftAnchor.getTransform(); }
	/**
	 * Returns the right (from) anchor.
	 * 
	 * @return Transform
	 */
	public Transform getRightAnchor() { return ppRightAnchor.getTransform(); }
}
