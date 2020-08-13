import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Label;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameController {	
	private HashMap<String, VisualNode> hmNodes;	// Hash map of the visual nodes
	private HashMap<String, NodeConnection> hmConnections;	// Hash map of the visual node connections
	private Display displayMain;	// The display
	private DisplayTextField searchBox;	// The search box of the display
	private Transform transWorldAnchor;	// The world's transform
	private NodeLoader nLoad;	// The node loader that populates the display with nodes
	private VisualNodeMenu vnmMenu;	// The visual node menu
	
	private ArrayList<VisualNode> alSelNodes;	// List of nodes the user has selected
	
	private boolean bMultiHigh;	// If the user has turned on multi highlight
	private ArrayList<VisualNode> alHighNodes;	// The highlighted nodes
	
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
		hmConnections = new HashMap<String, NodeConnection>();
		
		displayMain = new Display("Intern WebSpeed Search Assistant");
		new DisplayMenuBar(displayMain);
		searchBox = new DisplayTextField(displayMain);
		searchBox.setCapitalsOnly();
		
		transWorldAnchor = new Transform();
		
		nLoad = new NodeLoader(this);
		nLoad.load();
		
		displayMain.prepareGUI();
		
		searchBox.setVisible(false);
		
		alSelNodes = new ArrayList<VisualNode>();
		
		bMultiHigh = false;
		alHighNodes = new ArrayList<VisualNode>();
		
		vnmMenu = null;
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
		transWorldAnchor.addChild(_nodeToAdd_.getLeftAnchor());
		transWorldAnchor.addChild(_nodeToAdd_.getRightAnchor());
	}
	
	/**
	 * Removes a node from the nodes list.
	 * 
	 * @param _nodeToRemove_
	 * 				The node that will be removed from the list.
	 */
	private void removeNode(VisualNode _nodeToRemove_) {
		hmNodes.remove(_nodeToRemove_.getName());
		_nodeToRemove_.removeNodeFromDisplay(displayMain);
		transWorldAnchor.removeChild(_nodeToRemove_.getPolyTrans());
		transWorldAnchor.removeChild(_nodeToRemove_.getTextTrans());
		transWorldAnchor.removeChild(_nodeToRemove_.getLeftAnchor());
		transWorldAnchor.removeChild(_nodeToRemove_.getRightAnchor());
	}
	
	/**
	 * Adds a connection to the display.
	 * 
	 * @param _connToAdd_
	 * 				The connection that will be added.
	 */
	public void addConnection(NodeConnection _connToAdd_) {		
		// Make sure the connection does not already exist
		if (hmConnections.containsKey(_connToAdd_.getKey()))
			return;
		
		hmConnections.put(_connToAdd_.getKey(), _connToAdd_);
		_connToAdd_.addConnectionToDisplay(displayMain);
		transWorldAnchor.addChild(_connToAdd_.getLineTrans());
	}
	
	/**
	 * Removes a connection from the display.
	 * 
	 * @param _connToRemove_
	 * 				The connection that will be removed.
	 */
	private void removeConneciton(NodeConnection _connToRemove_) {
		hmConnections.remove(_connToRemove_.getKey());
		_connToRemove_.removeConnectionFromDisplay(displayMain);
		transWorldAnchor.addChild(_connToRemove_.getLineTrans());
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
		VisualNode moveAnchorNode = null;	// The node that is the anchor of the dragging movement
		Vector2Int lastMousePos = null;	// The last position of the mouse
		Vector2Int mousePos = null;		// The position of the mouse

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
					// See if the node menu was clicked
					if (vnmMenu != null && vnmMenu.checkInBound(mousePos)) {
						String menuSel = vnmMenu.getSelectedOption(mousePos);
						this.doVisualNodeOption(menuSel);
					}
					// Otherwise, try to select a node
					else {
						// Close any open visual node menu
						this.closeVisualNodeMenu();
					
						// See if there was a node where they clicked (for dragging)
						VisualNode selNode = this.getVisualNodeByPos(mousePos);
						moveAnchorNode = selNode;
						
						// If the user clicked on a node
						if (selNode != null) {
							// If the user does not have multiple nodes selected, 
							// deselect the currently selected node and selected the node they clicked on
							if (alSelNodes.size() <= 1) {
								this.deselectNodes();
								this.selectNode(selNode);
							}
							// If the user had multiple nodes selected
							else {
								// Check if the node clicked on is already selected
								boolean isAlreadySelected = false;
								for (VisualNode vn : alSelNodes) {
									if (vn == selNode) {
										isAlreadySelected = true;
										break;
									}
								}
								
								// If the selected node is a currently selected node, do nothing extra
								if (isAlreadySelected) {
								}
								// If the selected node is not a currently selected node, deselect the
								// current nodes and select that node
								else {
									this.deselectNodes();
									this.selectNode(selNode);
								}
							}
							// Get the selection offset
							selOffset = selNode.getScreenPosition().sub(mousePos);
						}
						// If the user did not click on a node
						else {
							// Make a selection box
							this.closeVisualNodeMenu();
							this.deselectNodes();
							selectBox = new DisplaySelectionBox(mousePos);
							selectBox.addToDisplay(displayMain);
						}
					}
				}
				// If the user pressed right mouse
				else if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON3) {	
					// Check if we have multiple nodes selected
					if (alSelNodes.size() > 1) {
						// Open a node menu that references all the nodes
						this.openVisualNodeMenu(alSelNodes, mousePos);
					}
					// If we don't have multiple nodes selected
					else {
						// See if there was a node where they pressed
						VisualNode pressedNode = this.getVisualNodeByPos(mousePos);
						
						// If the user did click on a node open the visual menu for that node
						if (pressedNode != null) {
							this.openVisualNodeMenu(pressedNode, mousePos);
						}
						else {
							this.closeVisualNodeMenu();
						}
					}
				}
			}
			// If the mouse was released
			if (mouseEventHandler.getWasMouseReleased()) {
				// If they released the left mouse button
				if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON1) {
					// See if the user has released the node they selected
					if (alSelNodes.size() == 1) {
						this.deselectNodes();
						selOffset = null;
					}
					
					// See if the user had a selection box
					if (selectBox != null) {
						for (String name : hmNodes.keySet()) {
							VisualNode vn = hmNodes.get(name);
							// Check if the current visual node is in bounds of the select box
							if (selectBox.testInBound(vn.getScreenPosition()))
								this.absoluteSelectNode(vn);
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
						// If there is no menu open
						if (vnmMenu == null) {
							// If the user has nodes selected, move those nodes
							if (alSelNodes.size() > 0 && moveAnchorNode != null) {
								for (VisualNode selNode : alSelNodes) {
									// If the nodes are not the anchor node move them based on the anchor node
									if (selNode != moveAnchorNode) {
										Vector2Int newPos = selNode.getScreenPosition().sub(
												moveAnchorNode.getScreenPosition()).add(
												mousePos.add(selOffset));
										selNode.setPosition(newPos);
									}
								}
								// Finally move the anchor node last
								moveAnchorNode.setPosition(mousePos.add(selOffset));
							}
							// If the user does not have any nodes selected
							else {
								// If the user has a selection box
								if (selectBox != null) {
									selectBox.setPoint2(mousePos);
								}
							}
						}
					}
					// If the middle mouse button is being held down
					if (mouseEventHandler.getMouseButton() == MouseEvent.BUTTON2) {
						// If there is not a menu open, move the world
						if (vnmMenu == null) {
							transWorldAnchor.translate(lastMousePos.sub(mousePos).flip());
						}
					}				
			}
			// If the mouse has been scrolled
			if (mouseEventHandler.getWasMouseScrolled()) {
				// Close the right click menu if there is one
				this.closeVisualNodeMenu();
				
				int scrollNotches = mouseEventHandler.getMouseScrollAmount();
				float scrollAmount = 0;
				if (scrollNotches < 0)
					scrollAmount = -ZOOM_POWER * scrollNotches;
				else
					scrollAmount = 1.0f / (ZOOM_POWER * scrollNotches);
				
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
				// If it was a right double click
				if (mouseEventHandler.getMouseButtonClicked() == MouseEvent.BUTTON3) {
					// See if there was a node where the user pressed
					VisualNode pressedNode = getVisualNodeByPos(mousePos);
					if (pressedNode != null) {
						this.pullReferencesCloser(pressedNode);
					}
				}
			}
			// If the mouse was moved
			if (mouseEventHandler.getWasMouseMoved()) {
				// If there is node menu open
				if (vnmMenu != null) {
					int highIndex = vnmMenu.getSelectedOptionIndex(mousePos);
					if (highIndex != -1) {
						// See if we are hovering over any options
						vnmMenu.highlightOption(highIndex);
					}
				}
			}
			// End Mouse Input
			// Start Menu Action Events
			if (menuEventHandler.getWasActionPerformed()) {
				// Search menu
				String actionCmd = menuEventHandler.getActionEventInfo().getActionCommand();
				switch (actionCmd) {
				case ("Search"):
					openSearch();
					break;
				case ("New"):
					openProgramFileBuilderMenu();
					break;
				default:
					System.out.println("Invalid Menu button: " + actionCmd);
					break;
				}
			}
			// End Menu Action Events
			// Start Keyboard Events
			// If a key was pressed
			if (keyEventHandler.getWasKeyPressed()) {
				// If m was pressed
				if (keyEventHandler.getKeyCode() == KeyEvent.VK_M) {
					VisualNode pressedNode = getVisualNodeByPos(mousePos);
					if (pressedNode != null) {
						this.pullChildCloserToHighlighted(pressedNode);
					}
				}
			}
			// If keys are being held down
			if (keyEventHandler.getIsKeyHeld()) {
				ArrayList<Integer> heldKeys = keyEventHandler.getKeyCodesHeld();
				
				// If shift is being held down
				if (heldKeys.contains((Integer)KeyEvent.VK_SHIFT))
					bMultiHigh = true;
				else
					bMultiHigh = false;
			}
			else
				bMultiHigh = false;
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
			keyEventHandler.reset();
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
			Vector2Int worldTransPos = vn.getScreenPosition().flip().add(screenCenter);
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
		Vector2Int windowPos = displayMain.getNewWindowCenterPos(popUpDim);
		
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
	 * Will not select if it is immovable.
	 * 
	 * @param _nodeToSel_
	 * 				Node to select.
	 */
	private void selectNode(VisualNode _nodeToSel_) {
		if (_nodeToSel_.getIsMovable()) {
			this.absoluteSelectNode(_nodeToSel_);
		}
	}
	
	/**
	 * Selects the given node and adds it to the list whether it is immovable or not.
	 * 
	 * @param _nodeToSel_
	 * 				Node to select.
	 */
	private void absoluteSelectNode(VisualNode _nodeToSel_) {
		if (!_nodeToSel_.getIsSelected()) {
			_nodeToSel_.toggleSelectedStatus();
			alSelNodes.add(_nodeToSel_);
		}
	}
	
	/**
	 * Toggles highlight off for all the current nodes
	 */
	private void unhighlightNodes() {
		for (VisualNode vn : alHighNodes)
			// Only turn the highlight off if its on (which should be always)
			if (vn.getIsHighlighted())
				vn.highlightNode();
		// Clear the list
		alHighNodes.clear();
	}
	
	/**
	 * Highlights the given node and adds it to the list.
	 * 
	 * @param _nodeToHighlight_
	 * 				Node to highlight.
	 */
	private void highlightNode(VisualNode _nodeToHighlight_) {
		if (!_nodeToHighlight_.getIsHighlighted()) {
			_nodeToHighlight_.highlightNode();
			if (!alHighNodes.contains(_nodeToHighlight_)) {
				alHighNodes.add(_nodeToHighlight_);
			}
		}
	}
	
	/**
	 * Unlights the given node and removes it from the list.
	 * 
	 * @param _nodeToUnhighlight_
	 * 				Node to unhighlight.
	 */
	private void unhighlightNode(VisualNode _nodeToUnhighlight_) {
		if (_nodeToUnhighlight_.getIsHighlighted()) {
			_nodeToUnhighlight_.highlightNode();
			if (alHighNodes.contains(_nodeToUnhighlight_))
				alHighNodes.remove(_nodeToUnhighlight_);
		}
	}
	
	/**
	 * Pulls all the references the given nodes has closer to the given node.
	 * 
	 * @param _centerNodes_
	 * 				The nodes whose references will be pulled closer.
	 */
	private void pullReferencesCloser(ArrayList<VisualNode> _centerNodes_) {
		// Iterate over the nodes and pull their children closer
		for (VisualNode singleCenter : _centerNodes_) {
			for (NodeConnection nc : singleCenter.getConnections()) {
				// Get the node that is not the center node from the connection
				VisualNode otherNode = nc.getNode(0);
				if (singleCenter == otherNode) {
					otherNode = nc.getNode(1);
				}
				
				// If the other node is one of the center nodes, don't pull it closer
				if (!_centerNodes_.contains(otherNode)) {
					// Move the node closer to the center node
					this.pullSingleNode(singleCenter, otherNode, 0.2);
				}
			}
		}
	}
	
	/**
	 * Pulls all the references the given node has closer to the given node.
	 * 
	 * @param _centerNode_
	 * 				The node whose references will be pulled closer.
	 */
	private void pullReferencesCloser(VisualNode _centerNode_) {
		ArrayList<VisualNode> passList = new ArrayList<VisualNode>();
		passList.add(_centerNode_);
		pullReferencesCloser(passList);
	}
	
	/**
	 * Pulls the given nodes closer to its highlighted parent.
	 * Only works if there is only 1 node highlighted.
	 * 
	 * @param _childNodes_
	 * 				Nodes to pull closer.
	 */
	private void pullChildCloserToHighlighted(ArrayList<VisualNode> _childNodes_) {
		if (alHighNodes.size() != 1) {
			return;
		}
		// This only works if only one node is highlighted, otherwise it has no idea what to do
		else {
			VisualNode centerNode = alHighNodes.get(0);
			
			for (VisualNode singleChild : _childNodes_) {
				boolean isValidChild = false;
				// Iterate over the connections to check if the node really is a child node
				for (NodeConnection nc : centerNode.getConnections()) {
					// Get the node that is not the center node from the connection
					VisualNode otherNode = nc.getNode(0);
					if (centerNode == otherNode) {
						otherNode = nc.getNode(1);
					}
					
					if (singleChild == otherNode)
						isValidChild = true;
				}
				
				// If the node really is a child of the center node, pull it closer
				if (isValidChild)
					this.pullSingleNode(centerNode, singleChild, 0.8);
			}
		}
	}
	
	/**
	 * Pulls a single child closer to the highlighted node.
	 * 
	 * @param _childNode_
	 * 				Node to pull closer.
	 */
	private void pullChildCloserToHighlighted(VisualNode _childNode_) {
		ArrayList<VisualNode> passList = new ArrayList<VisualNode>();
		passList.add(_childNode_);
		pullChildCloserToHighlighted(passList);
	}
	
	/**
	 * Moves a single node closer to the center node.
	 * 
	 * @param _centerNode_
	 * 				Stationary node.
	 * @param _moveNode_
	 * 				Node to move.
	 */
	private void pullSingleNode(VisualNode _centerNode_, VisualNode _moveNode_, double weight) {
		// Move the center node closer
		Vector2Int moveAmount = new Vector2Int(_centerNode_.getWorldPosition().sub(_moveNode_.getWorldPosition()));
		moveAmount = moveAmount.scale(weight);
		_moveNode_.move(moveAmount);
	}
	
	/**
	 * Opens the visual node menu for the given node.
	 * 
	 * @param _vn_
	 * 				The visual node who is the owner of the menu
	 */
	private void openVisualNodeMenu(VisualNode _vn_, Vector2Int _openPos_) {
		ArrayList<VisualNode> vns = new ArrayList<VisualNode>();
		vns.add(_vn_);
		this.openVisualNodeMenu(vns, _openPos_);
	}
	/**
	 * Opens the visual node menu for the given array of nodes.
	 * 
	 * @param _vns_
	 * 				The visual nodes who will be affected by the menu
	 */
	private void openVisualNodeMenu(ArrayList<VisualNode> _vns_, Vector2Int _openPos_) {
		this.closeVisualNodeMenu();
		
		vnmMenu = new VisualNodeMenu(_vns_, displayMain, _openPos_);
	}
	
	/**
	 * Closes any existing visual node menu
	 */
	private void closeVisualNodeMenu() {
		// If there is already a menu
		if (vnmMenu != null)
			vnmMenu.destroyMenu();
		vnmMenu = null;
	}
	
	/**
	 * Switch case handles what should happen for each option.
	 * 
	 * @param _option_
	 * 				String name of the option.
	 */
	private void doVisualNodeOption(String _option_) {
	
		switch (_option_) {
		case VisualNodeMenu.HIGHLIGHT:
			this.toggleHighlightNode(vnmMenu.getVisualNodes());
			break;
		case VisualNodeMenu.PULL:
			this.pullReferencesCloser(vnmMenu.getVisualNodes());
			break;
		case VisualNodeMenu.PUSH:
			this.pullChildCloserToHighlighted(vnmMenu.getVisualNodes());
			break;
		case VisualNodeMenu.COMMENT:
			this.openAddCommentMenu(vnmMenu.getVisualNodes(), true);
			break;
		case VisualNodeMenu.DESCRIPTION:
			this.openAddCommentMenu(vnmMenu.getVisualNodes(), false);
			break;
		case VisualNodeMenu.MOVABLE:
			this.setNodesMovable(vnmMenu.getVisualNodes());
			break;
		case VisualNodeMenu.DISABLE:
			this.disableNodeConnections(vnmMenu.getVisualNodes());
			break;
		default:
			System.out.println("Invalid menu item: " + _option_);
			break;
		}
		
		closeVisualNodeMenu();
	}
	
	/**
	 * Highlights the nodes associated with the menu
	 * 
	 * @param _nodes_
	 * 				The visual nodes whose highlight status will be toggled.
	 */
	private void toggleHighlightNode(ArrayList<VisualNode> _nodes_) {
		boolean atLeastOneHigh = false;
		for (VisualNode vn : _nodes_) {
			if (vn.getIsHighlighted()) {
				atLeastOneHigh = true;
				break;
			}
		}
		
		// If multiple highlight is not enabled, turn off highlight and clear the list
		if (!bMultiHigh) {
			this.unhighlightNodes();
		}
		
		// If even one node is currently highlighted, we unhighlight all nodes in the list
		if (atLeastOneHigh) {
			for (VisualNode vn : _nodes_) {
				if (vn.getIsHighlighted())
					this.unhighlightNode(vn);
			}
		}
		// If not even one node is highlighted, highlight all the nodes in the list
		else {
			for (VisualNode vn : _nodes_) {
				if (!vn.getIsHighlighted())
					this.highlightNode(vn);
			}
		}	
		
	}
	
	/**
	 * Opens a pop-up menu where the user can see and add a comment/description.
	 * 
	 * @param _selNodes_
	 * 				ArrayList that should contain only 1 node whose comment/description we are viewing/setting.
	 * @param _commentOrDesc_
	 * 				True = comment. False = description.
	 */
	private void openAddCommentMenu(ArrayList<VisualNode> _selNodes_, boolean _commentOrDesc_) {
		if (_selNodes_.size() < 0) {
			System.out.println("ERROR - No nodes selected for open comment");
		}
		
		VisualNode vn = _selNodes_.get(0);
		
		// Set the comment text box
		JTextField tf = new JTextField(20);

		// Separate the comment into multiple lines
		final int MAX_CHAR = 50;
		String concernedStr = _commentOrDesc_ ? vn.getComment() : vn.getDescription();
		String labelText = "<html>";
		for (int i = 0; i < concernedStr.length() / MAX_CHAR + 1; ++i) {
			int endIndex = (i + 1) * MAX_CHAR;
			endIndex = endIndex < concernedStr.length() ? endIndex : concernedStr.length();
			labelText += concernedStr.substring(i * MAX_CHAR, endIndex);
			labelText += "<br/>";
		}
		labelText += "</html>";
		JLabel label = new JLabel(labelText);
		
		// Add the text field and the label as children
		Component[] childComps = new Component[] {tf, label};
		Vector2Int windowDim = new Vector2Int(500, 309);
		
		
		String popName = _commentOrDesc_ ? vn.getName() + " Comment" : vn.getName() + " Description";
		
		DisplayPopup commentMenu = new DisplayPopup(popName, windowDim,
				displayMain.getNewWindowCenterPos(windowDim), childComps);
		
		// Add the key listener
		tf.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				// When enter is pressed, set the comment/description and then close the window.
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					if (_commentOrDesc_)
						vn.setComment(tf.getText());
					else
						vn.setDescription(tf.getText());
					commentMenu.dispose();
				}
			}
			@Override
			public void keyReleased(KeyEvent arg0) { }
			@Override
			public void keyTyped(KeyEvent arg0) { }
			
		});
	}
	
	/**
	 * Opens a popup for prompting to build a new WSSout file.
	 */
	private void openProgramFileBuilderMenu() {
		// The main JPanel that we will orient things for the popup on
		JPanel mainCard = new JPanel(new BorderLayout(8, 2));
		
		// Create the JPanel to hold the input both and some text
		JPanel searchCard = new JPanel(new BorderLayout(8, 2));
		JLabel searchText = new JLabel("Parse Location: ");
		JTextField searchField = new JTextField(20);
		searchField.setText(Searcher.DEFAULT_PATH);
		searchCard.add(searchText, BorderLayout.WEST);
		searchCard.add(searchField, BorderLayout.EAST);
		
		// Create the JPanel to hold the buttons
		JPanel buttonCard = new JPanel(new BorderLayout(8, 2));
		JButton runButton = new JButton("Run");
		JButton expButton = new JButton("Choose in File Explorer");
		buttonCard.add(runButton, BorderLayout.EAST);
		buttonCard.add(expButton, BorderLayout.WEST);
		
		// Place the text input card above the buttons card
		mainCard.add(searchCard, BorderLayout.NORTH);
		mainCard.add(buttonCard, BorderLayout.SOUTH);
		
		// Create the popup
		Vector2Int popDim = new Vector2Int(500, 309);
		Component[] childs = new Component[] { mainCard };
		DisplayPopup parseMenu = new DisplayPopup("Run New Searcher",
				popDim, displayMain.getNewWindowCenterPos(popDim), childs);
		
		// Specify what to do when the buttons are pressed
		runButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				File searchFile = new File(searchField.getText());
				if (searchFile.exists()) {
					String localPath = new File("").getAbsolutePath();
					Searcher.BuildFile(searchField.getText(), localPath);
					reloadNodesAndConnections();
				}
				else {
					showErrorMessage("Invalid Path");
				}
			}
		});
		expButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// If the current entered path is valid, open the chooser starting at that path
				String searchPathStr = "";
				File searchPathFile = new File(searchField.getText());
				if (searchPathFile.exists())
					searchPathStr = searchPathFile.getAbsolutePath();
				
				// Open the chooser
				JFileChooser chooser = new JFileChooser(searchPathStr);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				
				// When they have chosen a folder, set the text of the field
				int returnVal = chooser.showOpenDialog(parseMenu);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					searchField.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
	}
	
	/**
	 * Toggles the selected nodes to be immovable or movable.
	 * 
	 * @param _selNodes_
	 * 				Nodes that have been selected.
	 */
	private void setNodesMovable(ArrayList<VisualNode> _selNodes_) {
		boolean atLeastOneImmove = false;
		for (VisualNode vn : _selNodes_) {
			if (!vn.getIsMovable()) {
				atLeastOneImmove = true;
				break;
			}
		}
		// If at least one of the nodes is immovable, set them all to be movable
		// If all of the nodes are movable, set them all to immovable
		for (VisualNode vn : _selNodes_) {
			vn.setIsMovable(atLeastOneImmove);
		}
	}
	
	/**
	 * Toggles the selected nodes to display their connections or to hide them.
	 * 
	 * @param _selNodes_
	 * 				Nodes that have been selected.
	 */
	private void disableNodeConnections(ArrayList<VisualNode> _selNodes_) {
		boolean atLeastOneEn = false;
		for (VisualNode vn : _selNodes_) {
			if (vn.getConnectionsVisible()) {
				atLeastOneEn = true;
				break;
			}
		}
		// If at least one of the nodes is enabled, set them all to be disabled
		// If all of the nodes are disabled, enable them all
		for (VisualNode vn : _selNodes_) {
			vn.setConnectionsVisible(!atLeastOneEn);
		}
	}
	
	/**
	 * Removes all nodes and connections from the display.
	 */
	private void removeAllNodesAndConnections(){
		ArrayList<String> connKeys = new ArrayList<String>();
		for (String key : hmConnections.keySet())
			connKeys.add(key);
		ArrayList<String> nodeKeys = new ArrayList<String>();
		for (String key : hmNodes.keySet())
			nodeKeys.add(key);
		
		for (String key : connKeys)
			removeConneciton(hmConnections.get(key));
		for (String key : nodeKeys)
			removeNode(hmNodes.get(key));
		
		hmConnections.clear();
		hmNodes.clear();
		
		alSelNodes.clear();
		alHighNodes.clear();
	}
	
	/**
	 * Reloads all the nodes and connections.
	 * Removes all current ones first.
	 */
	private void reloadNodesAndConnections() {
		removeAllNodesAndConnections();
		
		nLoad = new NodeLoader(this);
		try {
			nLoad.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		transWorldAnchor.translate(new Vector2Int());
	}
	
	/**
	 * Converts the given screen position to world position.
	 * 
	 * @param _screenPos_
	 * 				The screen position to change to world position
	 * @return Vector4
	 */
	public Vector4 screenToWorldPos(Vector2Int _screenPos_) {
		return transWorldAnchor.getTransformationMatrix().mul(new Vector4(_screenPos_));
	}
	
	/**
	 * Returns the visual node hash map.
	 * 
	 * @return HashMap<String, VisualNode>
	 */
	public HashMap<String, VisualNode> getVisualNodeMap() { return hmNodes; }
}
