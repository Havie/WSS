import java.awt.Component;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.glass.events.KeyEvent;

public class GameController {	
	private HashMap<String, VisualNode> hmNodes;	// Hash map of the visual nodes
	private HashMap<String, NodeConnection> alConnections;	// Hash map of the visual node connections
	private Display displayMain;	// The display
	private DisplayTextField searchBox;	// The search box of the display
	private Transform transWorldAnchor;	// The world's transform
	private NodeLoader nLoad;	// The node loader that populates the display with nodes
	
	private ArrayList<VisualNode> alSelNodes;	// List of nodes the user has selected
	
	// Constants
	private final int TARGET_FPS = 60;
	private final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
	private final float ZOOM_POWER = 2.0f;
	
	
	/**
	 * Constructs a GameController.
	 * 
	 * @throws IOException 
	 */
	public GameController() throws IOException{
		hmNodes = new HashMap<String, VisualNode>();
		alConnections = new HashMap<String, NodeConnection>();
		
		displayMain = new Display();
		new DisplayMenuBar(displayMain);
		searchBox = new DisplayTextField(displayMain);
		searchBox.setCapitalsOnly();
		
		transWorldAnchor = new Transform();
		
		nLoad = new NodeLoader(this);
		nLoad.load();
		
		displayMain.prepareGUI();
		
		searchBox.setVisible(false);
		
		alSelNodes = new ArrayList<VisualNode>();
	}
	
	/**
	 * Adds a node to the nodes list.
	 * 
	 * @param _nodeToAdd_
	 * 				The node that will be added to the list.
	 */
	public void addNode(VisualNode _nodeToAdd_){
		hmNodes.put(_nodeToAdd_.getName(), _nodeToAdd_);
		_nodeToAdd_.addNodeToDisplay(displayMain);
		transWorldAnchor.addChild(_nodeToAdd_.getPolyTrans());
		transWorldAnchor.addChild(_nodeToAdd_.getTextTrans());
	}
	
	/**
	 * Adds a connection to the display.
	 * 
	 * @param _connToAdd_
	 * 				The connection that will be added.
	 */
	public void addConnection(NodeConnection _connToAdd_) {		
		// Make sure the connection does not already exist
		if (alConnections.containsKey(_connToAdd_.getKey()))
			return;
		
		alConnections.put(_connToAdd_.getKey(), _connToAdd_);
		_connToAdd_.addConnectionToDisplay(displayMain);
		transWorldAnchor.addChild(_connToAdd_.getLineTrans());
	}
	
	/**
	 * Starts running the game loop.
	 * 
	 * @throws IOException 
	 */
	public void run() throws IOException {		
		displayMain.repaint();
		transWorldAnchor.setSize(new Vector4(0.03125f, 0.03125f, 0.03125f));
		transWorldAnchor.setPosition(new Vector2Int(700, 480));
		transWorldAnchor.translate(new Vector2Int());
		
		// Time variables
		long lastLoopTime = System.nanoTime();
		long lastFpsTime = 0;
		long gameTime = 0;

		// Changed during game variables
		Vector2Int selOffset = null;	// Offset of where the user selected the nodes
		Vector2Int lastMousePos = null;	// The last position of the mouse
		Vector2Int mousePos = null;		// The position of the mouse
		ArrayList<VisualNode> highNodes = new ArrayList<VisualNode>();	// The nodes that are currently highlighted
		boolean multiHighlight = false;	// If the user can highlight multiple nodes at once
		DisplaySelectionBox selectBox = null;	// The selection box
		
		
		// Mouse event handler
		MouseEvents mouseEventHandler = displayMain.getMouseEvents();
		// Menu event handler
		MenuActionEventHandler menuEventHandler = displayMain.getMenuEvents();
		// Keyboard event handler
		KeyEventHandler keyEventHandler = displayMain.getKeyEvents();
		// Keyboard event handler for the search box
		KeyEventHandler searchBoxKeyEventHandler = searchBox.getKeyEvents();
		
		// Start the game loop
		while(true) {
			if (displayMain.getShouldCloseWindow()){
				transWorldAnchor.setLocalPosition(new Vector4(0, 0, 0, 1));
				transWorldAnchor.setSize(new Vector4(1, 1, 1, 1));
				nLoad.save(hmNodes);
				System.exit(0);
			}
			
			
			// Update time
			long now = System.nanoTime();
			long updateLength = now - lastLoopTime;
			lastLoopTime = now;
			//double delta = updateLength / ((double)OPTIMAL_TIME);
			
			lastFpsTime += updateLength;
			if (lastFpsTime >= 1000000000)
				lastFpsTime = 0;

			// Get input
			// Start Mouse Input
			// Mouse position
			if (mousePos != null)
				lastMousePos = mousePos.clone();
			mousePos = mouseEventHandler.getMousePosition();
			// If the mouse was pressed
			if (mouseEventHandler.getWasMousePressed()) {
				// If the user pressed left mouse
				if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON1) {
					if (alSelNodes.size() <= 1)
						this.deselectNodes();
					
					// See if there was a node where they clicked (for dragging)
					VisualNode selNode = this.getVisualNodeByPos(mousePos);
					if (selNode != null) {		
						this.selectNode(selNode);
						selOffset = selNode.getPosition().sub(mousePos);
					}
				}
				// If the user pressed right mouse
				else if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON3) {
					// See if there was a node where they pressed (for highlighting)
					VisualNode pressedNode = this.getVisualNodeByPos(mousePos);
					// If multiple highlight is not enabled, turn off highlight and clear the list
					if (!multiHighlight) {
						for (VisualNode vn : highNodes)
							// If its the pressed one, don't toggle it off, that will happen below
							if (vn != pressedNode && vn.getIsHighlighted())
								vn.highlightNode();
						highNodes.clear();
					}
					// If there was a selected node, highlight toggle it
					if (pressedNode != null){					
						pressedNode.highlightNode();
						// If the node was already highlighted, remove it
						if (highNodes.contains(pressedNode))
							highNodes.remove(pressedNode);
						// Otherwise, add it
						else
							highNodes.add(pressedNode);
					}
					else {
						// There was no selected node, so lets make a selection box
						selectBox = new DisplaySelectionBox(mousePos);
						selectBox.addToDisplay(displayMain);
					}
				}
			}
			// If the mouse was released
			if (mouseEventHandler.getWasMouseReleased()) {
				// If they released the left mouse button
				if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON1) {
					// See if the user has released the node they selected
					if (alSelNodes.size() <= 1) {
						this.deselectNodes();
						selOffset = null;
					}
				}
				// If they released the right mouse button
				else if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON3) {
					// See if the user had a selection box
					if (selectBox != null) {
						for (String name : hmNodes.keySet()) {
							VisualNode vn = hmNodes.get(name);
							// Check if the current visual node is in bounds of the select box
							if (selectBox.testInBound(vn.getPolyTrans().getScreenPosition()));
								this.selectNode(vn);
						}
						
						selectBox.removeFromDisplay(displayMain);
						selectBox = null;
					}
				}
			}
			// If the mouse is down
			if (mouseEventHandler.getMouseIsDown()) {
				// If the left mouse button is being held down
				if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON1) {
					// If the user has a node selected, move that node
					if (alSelNodes.size() > 0)
						for (VisualNode selNode : alSelNodes)
							selNode.setPosition(mousePos.add(selOffset));
					// If they don't we want to move the world in the opposite direction
					else
						transWorldAnchor.translate(lastMousePos.sub(mousePos).flip());
				}
				// If the right mouse button is being held down
				if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON3) {
					// If the user has a selection box
					if (selectBox != null) {
						selectBox.setPoint2(mousePos);
					}
				}
			}
			// If the mouse has been scrolled
			if (mouseEventHandler.getWasMouseScrolled()) {
				int scrollNotches = mouseEventHandler.getMouseScrollAmount();
				float scrollAmount = 0;
				if (scrollNotches < 0)
					scrollAmount = -1.0f / (ZOOM_POWER * scrollNotches);
				else
					scrollAmount = ZOOM_POWER * scrollNotches;
				
				// Scale the world
				transWorldAnchor.scale(new Vector4(scrollAmount, scrollAmount, 1), new Vector4(mousePos));
			}
			// If the mouse was double clicked
			if (mouseEventHandler.getWasMouseDoubleClicked()) {
				// If it was a left double click
				if (mouseEventHandler.getMouseButtonClicked() == MouseEvent.BUTTON1) {
					// See if there was a node where the user pressed (for setting root)
					VisualNode pressedNode = getVisualNodeByPos(mousePos);					
					if (pressedNode != null) {
						pressedNode.toggleRootStatus();
					}
				}
			}
			// End Mouse Input
			// Start Menu Action Events
			if (menuEventHandler.getWasActionPerformed()) {
				// Search menu
				if (menuEventHandler.getActionEventInfo().getActionCommand().equals("Search")) {	
					openSearch();
				}
			}
			// End Menu Action Events
			// Start Keyboard Events
			// If keys are being held down
			if (keyEventHandler.getIsKeyHeld()) {
				ArrayList<Integer> heldKeys = keyEventHandler.getKeyCodesHeld();
				
				// If shift is being held down
				if (heldKeys.contains((Integer)KeyEvent.VK_SHIFT))
					multiHighlight = true;
				else
					multiHighlight = false;
			}
			else
				multiHighlight = false;
			// End Keyboard Events
			// Start Search Box Keyboard Events
			// If a key was pressed
			if (searchBoxKeyEventHandler.getWasKeyPressed()) {
				// If enter was pressed
				if (searchBoxKeyEventHandler.getKeyCode() == KeyEvent.VK_ENTER) {
					// If we find the specified program, clear the text box and hide the search menu
					if (searchNode(searchBox.getText())) {
						searchBox.setText("");
						searchBox.setVisible(false);
					}
					// If we don't find the specified program, display an error message
					else {
						showErrorMessage("No program with the name " +
								searchBox.getText() + " was found.");
					}
				}
			}
			// End Search Box Keyboard Events
			// Reset input
			mouseEventHandler.reset();
			menuEventHandler.reset();
			searchBoxKeyEventHandler.reset();
			
			// Clear the graphic to draw again
			displayMain.repaint();
			
			
			// Sleep for a little bit
			try {
				gameTime = (lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000;
				//System.out.println(gameTime);
				Thread.sleep(gameTime);
			} catch (Exception e){
				
			}
		}
	}
	
	/**
	 * Opens the search box.
	 */
	private void openSearch() {
		searchBox.setVisible(!searchBox.isVisible());
	}
	
	/**
	 * Centers the world on the specified node.
	 * Returns false if there was no node with the specified name.
	 * 
	 * @param _nodeName_
	 * 				Node to search for.
	 */
	private boolean searchNode(String _nodeName_) {		
		Vector2Int screenCenter = displayMain.getDisplayCenter();
		
		VisualNode vn = hmNodes.get(_nodeName_);
		if (vn == null) {
			return false;
		}
		else{
			Vector2Int worldTransPos = vn.getPosition().flip().add(screenCenter);
			transWorldAnchor.translate(worldTransPos);
			return true;
		}
	}
	
	/**
	 * Creates a DisplayPopup to display an error message.
	 * 
	 * @param _message_
	 * 				The message the pop-up will display.
	 * @return DisplayPopup
	 */
	private DisplayPopup showErrorMessage(String _message_) {
		// Create the label with the text on it.
		Label l = new Label(_message_);
		Vector2Int popUpDim = new Vector2Int(500, 100);
		// The window position will be such that the center of the pop up is at
		// the center of the current display.
		Vector2Int windowPos = new Vector2Int(displayMain.getJFrame().getLocation().x,
											  displayMain.getJFrame().getLocation().y);
		windowPos = windowPos.add(displayMain.getDisplayCenter()).sub(popUpDim.scale(0.5));
		Component[] popChildren = new Component[] { l };
		
		// Create the menu and return it.
		return new DisplayPopup("Error Message", popUpDim, windowPos, popChildren);
	}
	
	/**
	 * Returns the VisualNode that has bounds containing the given position.
	 * 
	 * @param _pos_
	 * 				The position of a potential node we want.
	 * @return VisualNode
	 */
	private VisualNode getVisualNodeByPos(Vector2Int _pos_) {
		for (String key : hmNodes.keySet()) {
			VisualNode singleNode = hmNodes.get(key);
			if (singleNode.checkInBound(_pos_)){
				return singleNode;
			}
		}
		return null;
	}
	
	/**
	 * Deselects the selected nodes and clears the list.
	 */
	private void deselectNodes() {
		for (VisualNode vn : alSelNodes) {
			vn.toggleSelectedStatus();
		}
		alSelNodes.clear();
	}
	
	/**
	 * Selects the given node and adds it to the list.
	 * 
	 * @param _nodeToSel_
	 * 				Node to select.
	 */
	private void selectNode(VisualNode _nodeToSel_) {
		_nodeToSel_.toggleSelectedStatus();
		alSelNodes.add(_nodeToSel_);
	}
	
	/**
	 * Returns the visual node hash map.
	 * 
	 * @return HashMap<String, VisualNode>
	 */
	public HashMap<String, VisualNode> getVisualNodeMap() { return hmNodes; }
}
