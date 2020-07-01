import java.util.ArrayList;

public class GameController {	
	private ArrayList<VisualNode> alNodes;
	private ArrayList<NodeConnection> alConnections;
	private Display mainDisplay;
	private Transform worldAnchor;
	
	// Constants
	private final int TARGET_FPS = 60;
	private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	private final float ZOOM_POWER = 2.0f;
	
	
	/**
	 * Constructs a GameController.
	 */
	public GameController(){
		alNodes = new ArrayList<VisualNode>();
		alConnections = new ArrayList<NodeConnection>();
		mainDisplay = new Display();
		worldAnchor = new Transform();
	}
	
	/**
	 * Adds a node to the nodes list.
	 * 
	 * @param _nodeToAdd_
	 * 				The node that will be added to the list.
	 */
	public void addNode(VisualNode _nodeToAdd_){
		alNodes.add(_nodeToAdd_);
		_nodeToAdd_.addNodeToDisplay(mainDisplay);
		worldAnchor.addChild(_nodeToAdd_.getPolyTrans());
		worldAnchor.addChild(_nodeToAdd_.getTextTrans());
	}
	
	/**
	 * Adds a connection to the display.
	 * 
	 * @param _connToAdd_
	 * 				The connection that will be added.
	 */
	public void addConnection(NodeConnection _connToAdd_) {
		alConnections.add(_connToAdd_);
		_connToAdd_.addConnectionToDisplay(mainDisplay);
		worldAnchor.addChild(_connToAdd_.getLineTrans());
	}
	
	/**
	 * Starts running the game loop.
	 */
	public void run() {
		// Time variables
		long lastLoopTime = System.nanoTime();
		long lastFpsTime = 0;
		long gameTime = 0;
		
		// Pre-game initialization
		// Testing
		/*
		VisualNode node1 = new VisualNode(new Vector2Int(100, 100)); // Test node 1
		VisualNode node2 = new VisualNode(new Vector2Int(300, 100)); // Test node 2
		VisualNode node3 = new VisualNode(new Vector2Int(100, 300)); // Test node 3
		VisualNode node4 = new VisualNode(new Vector2Int(300, 300)); // Test node 4
		
		NodeConnection conn1 = new NodeConnection(node1, node2); // Test connection
		NodeConnection conn2 = new NodeConnection(node1, node3); // Test connection
		NodeConnection conn3 = new NodeConnection(node2, node3); // Test connection
		NodeConnection conn4 = new NodeConnection(node1, node4); // Test connection\
		
		addConnection(conn1);
		addConnection(conn2);
		addConnection(conn3);
		addConnection(conn4);
		addNode(node1);
		addNode(node2);
		addNode(node3);
		addNode(node4);
		*/
		// Changed during game variables
		VisualNode selNode = null;		// Selected node
		Vector2Int selOffset = null;	// Offset of where the user selected the node
		Vector2Int lastMousePos = null;	// The last position of the mouse
		Vector2Int mousePos = null;		// The position of the mouse
		
		
		// Mouse event handler
		MouseEvents mouseEventHandler = mainDisplay.getMouseEvents();
		
		// Start the game loop
		while(true) {
			// Update time
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			//double delta = updateLength / ((double)OPTIMAL_TIME);
			
			lastFpsTime += updateLength;
			if (lastFpsTime >= 1000000000)
				lastFpsTime = 0;

			// Get input
			// Mouse position
			if (mousePos != null)
				lastMousePos = mousePos.clone();
			mousePos = mouseEventHandler.getMousePosition();
			// If the mouse was pressed
			if (mouseEventHandler.getWasMousePressed()) {
				// See if the user tried to select a node
				for (VisualNode singleNode : alNodes) {
					if (singleNode.checkInBound(mousePos)){
						selNode = singleNode;
						selOffset = selNode.getPosition().sub(mousePos);
						break;
					}
				}
			}
			// See if the user has released the node they selected
			if (selNode != null && mouseEventHandler.getWasMouseReleased()) {
				selNode = null;
				selOffset = null;
			}
			// See if the user does not have a node selected, but is holding down
			// If that is the case, we want to move the world in the opposite direction.
			if (selNode == null && mouseEventHandler.getMouseIsDown()) {
				worldAnchor.translate(lastMousePos.sub(mousePos).flip());
			}
			// See if the user has scrolled the mouse wheel
			if (mouseEventHandler.getWasMouseScrolled()) {
				int scrollNotches = mouseEventHandler.getMouseScrollAmount();
				float scrollAmount = 0;
				if (scrollNotches < 0) {
					scrollAmount = -1.0f / (ZOOM_POWER * scrollNotches);
				}
				else
					scrollAmount = ZOOM_POWER * scrollNotches;
				
				// Scale the world
				worldAnchor.scale(new Vector4(scrollAmount, scrollAmount, 1), new Vector4(mousePos));
			}
			// Reset input
			mouseEventHandler.reset();
			
			// If the user has a node selected, move it
			if (selNode != null) {
				selNode.setPosition(mousePos.add(selOffset));
			}
			
			// Clear the graphic to draw again
			mainDisplay.repaint();
			
			
			// Sleep for a little bit
			try {
				gameTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
				//System.out.println(gameTime);
				Thread.sleep(gameTime);
			} catch (Exception e){
				
			}
		}
	}
}
