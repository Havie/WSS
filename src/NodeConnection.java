import java.awt.Color;

public class NodeConnection {
	private PaintableConnection plConnector;	// The line between the two nodes
	private VisualNode vnNode1;			// Node 1 (from node) to connect
	private VisualNode vnNode2;			// Node 2 (to node) to connect
	
	// Connector line display specifications
	private final Color LINE_COLOR = new Color(0.2f, 0.2f, 0.2f); // color of the line
	private final int LINE_THICKNESS = 5;
	private final Color LINE_COLOR_HIGHLIGHT = new Color(0.8f, 0.5f, 0.1f);
	
	/**
	 * Constructs a NodeConnection between the provided nodes.
	 * 
	 * @param _node1_
	 * 				Node 1 to connect (from node).
	 * @param _node2_
	 * 				Node 2 to connect (to node).
	 */
	public NodeConnection(VisualNode _node1_, VisualNode _node2_) {
		vnNode1 = _node1_;
		vnNode2 = _node2_;
		
		vnNode1.addConnection(this);
		vnNode2.addConnection(this);
		
		plConnector = new PaintableConnection(vnNode1.getRightAnchor(), vnNode2.getLeftAnchor(),
				LINE_COLOR, LINE_THICKNESS);
		updatePosition();
	}
	
	/**
	 * Adds the node connection to passed in display.
	 *  
	 * @param _display_
	 * 				The display the connection will be painted on.
	 */
	public void addConnectionToDisplay(Display _display_) {
		_display_.addPaintableObj(plConnector);
	}
	
	/**
	 * Removes the node connection form the passed in display.
	 * 
	 * @param _display_
	 * 				The display the connection will be removed from.
	 */
	public void removeConnectionFromDisplay(Display _display_) {
		_display_.removePaintableObj(plConnector);
	}
	
	/**
	 * Updates the start and end points of the line.
	 */
	public void updatePosition() {
		//plConnector.getTransform().setLocalPosition(vnNode1.getScreenPosition().add(vnNode2.getScreenPosition()));
		highlight();
	}
	
	/**
	 * Scales the node connection.
	 * 
	 * @param _scalar_ float
	 */
	public void scale(float _scalar_) {
		plConnector.getTransform().scale(new Vector4(_scalar_, _scalar_, _scalar_), new Vector4());
	}
	
	/**
	 * Returns the unique key for this node connection. 
	 * 
	 * It concatenates the names of the two
	 * nodes such that the first name is the name of the first node (from node)
	 * and the second name is the name of the second node (to node)
	 * 
	 * [Depricated] It concatenates the names of the two
	 * nodes such that the first name is lexicographically greater than the second.
	 * 
	 * @return String
	 */
	public String getKey() {
		String firstName = vnNode1.getName();
		String secondName = vnNode2.getName();
		/*
		// If the first name is lexographically less than the second name, swap them
		if (firstName.compareTo(secondName) < 0) {
			String temp = firstName;
			firstName = secondName;
			secondName = temp;
		}
		*/
		
		return firstName + " " + secondName;
	}
	
	/**
	 * Highlights the node connection if at least one of its nodes are highlighted.
	 */
	public void highlight() {
		if (vnNode1.getIsHighlighted() || vnNode2.getIsHighlighted()) {
			plConnector.setColor(LINE_COLOR_HIGHLIGHT);
		}
		else
		{
			plConnector.setColor(LINE_COLOR);
		}
	}
	
	/**
	 * Determines if the connection should be hidden based on the two visual nodes' connection visible status
	 */
	public void updateHiddenStatus() {
		if (vnNode1.getConnectionsVisible() && vnNode2.getConnectionsVisible())
			plConnector.setHidden(false);
		else
			plConnector.setHidden(true);
	}
	
	/**
	 * Returns the transform of the line.
	 * 
	 * @return Transform
	 */
	public Transform getLineTrans() { return plConnector.getTransform(); }
	/**
	 * Returns the first node when given 0, and the second node when given anything else.
	 * 
	 * @param _index_
	 * 				Which node to get
	 * @return VisualNode
	 */
	public VisualNode getNode(int _index_) { if (_index_ == 0) return vnNode1; else return vnNode2;}
}
