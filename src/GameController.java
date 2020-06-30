import java.util.ArrayList;
import java.awt.MouseInfo;
import java.awt.Point;

public class GameController {	
	private ArrayList<VisualNode> alNodes;
	private Display mainDisplay;
	
	// Constants
	private final int TARGET_FPS = 60;
	private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	
	
	/**
	 * Constructs a GameController.
	 */
	public GameController(){
		alNodes = new ArrayList<VisualNode>();
		mainDisplay = new Display();
	}
	
	/**
	 * Adds a node to the nodes list.
	 * 
	 * @param _nodeToAdd_
	 * 				The node that will be added to the list.
	 */
	private void addNode(VisualNode _nodeToAdd_){
		alNodes.add(_nodeToAdd_);
		_nodeToAdd_.addNodeToDisplay(mainDisplay);
	}
	
	/**
	 * Adds a connection to the display.
	 * 
	 * @param _connToAdd_
	 * 				The connection that will be added.
	 */
	private void addConnection(NodeConnection _connToAdd_) {
		_connToAdd_.addConnectionToDisplay(mainDisplay);
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
		// Changed during game variables
		VisualNode selNode = null;		// Selected node
		Vector2Int selOffset = null;	// Offset of where the user selected the node
		
		// Mouse event handler
		MouseEvents mouseEventHandler = mainDisplay.getMouseEvents();
		
		// Start the game loop
		while(true) {
			// Update time
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			double delta = updateLength / ((double)OPTIMAL_TIME);
			
			lastFpsTime += updateLength;
			if (lastFpsTime >= 1000000000)
				lastFpsTime = 0;

			// Get input
			// See if the user is trying to select a node
			if (mouseEventHandler.getWasMousePressed()) {
				for (VisualNode singleNode : alNodes) {
					if (singleNode.checkInBound(mouseEventHandler.getMousePosition())){
						selNode = singleNode;
						Point mousePoint = MouseInfo.getPointerInfo().getLocation();
						Vector2Int mousePos = new Vector2Int(mousePoint.x, mousePoint.y);
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
			// Reset input
			mouseEventHandler.reset();
			
			// If the user has a node selected, move it
			if (selNode != null) {
				Point mousePoint = MouseInfo.getPointerInfo().getLocation();
				Vector2Int mousePos = new Vector2Int(mousePoint.x, mousePoint.y);
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
