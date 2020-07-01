import java.awt.Color;

public class NodeConnection {
	private PaintableConnection plConnector;	// The line between the two nodes
	private VisualNode vnNode1;			// Node 1 to connect
	private VisualNode vnNode2;			// Node 2 to connect
	
	// Connector line display specifications
	private final Color LINE_COLOR = Color.WHITE;
	private final int LINE_THICKNESS = 5;
	
	/**
	 * Constructs a NodeConnection between the provided nodes.
	 * 
	 * @param _node1_
	 * 				Node 1 to connect.
	 * @param _node2_
	 * 				Node 2 to connect.
	 */
	public NodeConnection(VisualNode _node1_, VisualNode _node2_) {
		vnNode1 = _node1_;
		vnNode2 = _node2_;
		
		vnNode1.addConnection(this);
		vnNode2.addConnection(this);
		
		plConnector = new PaintableConnection(vnNode1.getPolyTrans(), vnNode2.getPolyTrans(),
				LINE_COLOR, LINE_THICKNESS);
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
	 * Updates the start and end points of the line.
	 */
	public void updatePosition() {
		plConnector.getTransform().setLocalPosition(vnNode1.getPosition().add(vnNode2.getPosition()));
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
	 * Returns the transform of the line.
	 * 
	 * @return Transform
	 */
	public Transform getLineTrans() { return plConnector.getTransform(); }
}
