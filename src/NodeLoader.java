import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

// TO DO make ordered list based on most references

public class NodeLoader {
	private Parser parser;		// The parser with all the nodes.
	private GameController gc;	// The game controller that the nodes will be loaded into.
	private HashMap<String, VisualNode> hmVNodes; 
	
	private final float NOISE = 1000;	// The amount of noise.
	private final int VERT_SPACING = 125;
	private final int HOR_SPACING = 180;
	
	/**
	 * Constructs a NodeLoader.
	 * 
	 * @param _gameCont_
	 * 				The game controller to load to
	 */
	public NodeLoader(GameController _gameCont_){
		parser = new Parser("C:\\Users\\wsenalik\\");
		gc = _gameCont_;
		hmVNodes = new HashMap<String, VisualNode>();
	}
	
	/**
	 * Loads the nodes in the parser into the game controller.
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException 
	{
		parser.ImportData();
		
		HashMap<String, Node> nodeMap = parser.GetMap();
		
		// Create connections
		//loadCircleNodes(nodeMap); // In a semi-random circle
		loadClusterNodes(nodeMap);	// In clusters
		//loadBasedOnRefSize(nodeMap); // Circle weighted with reference size
		
		// Create the connections between the loaded nodes
		loadConnections(nodeMap);
		
		// Add the nodes after the connections
		addNodes();
	}
	
	/**
	 * Loads the nodes semi-randomly in a circle with some noise.
	 * 
	 * @param _nodeMap_
	 * 				The nodes to load.
	 */
	private void loadCircleNodes(HashMap<String, Node> _nodeMap_) {
		int OG_radius = _nodeMap_.size() * 5;
		float radianIncrement = 2.0f / _nodeMap_.size() * (float)Math.PI;
		int counter = 0;
		Random rand = new Random();
		
		// Create all the nodes in a circle
		for (String key : _nodeMap_.keySet()) {
			Node n = _nodeMap_.get(key);
			
			int sign = rand.nextInt(2);
			sign = sign == 0 ? 1 : -1;
			float radius = OG_radius + sign * rand.nextFloat() * NOISE;
			
			int x = (int)(radius * Math.cos(counter * radianIncrement));
			int y = (int)(radius * Math.sin(counter * radianIncrement));
			
			VisualNode vn = new VisualNode(n, new Vector2Int(x, y));
			hmVNodes.put(vn.getName(), vn);
			
			++counter;
		}
	}
	
	/**
	 * Loads the nodes in clusters (still somewhat in a circle).
	 * 
	 * @param _nodeMap_
	 * 				The nodes to load.
	 */
	private void loadClusterNodes(HashMap<String, Node> _nodeMap_) {
		final Vector2Int CLUSTER_SPACE = new Vector2Int(1000, 1000);
		final Vector2Int NODE_SPACE = new Vector2Int(180, 120);
		
		// The nodes we have already tested
		HashMap<String, Node> testedNodes = new HashMap<String, Node>();
		// The clusters of nodes
		ArrayList<HashMap<String, Node>> nodeClusters = new ArrayList<HashMap<String, Node>>();
		
		// Iterate over the node map to get a list of the clusters.
		for (String key : _nodeMap_.keySet()) {
			key = key.toUpperCase();
			// Check if the node has not already been tested.
			if (!(testedNodes.containsKey(key))) {
				// This node is the root node for the cluster.
				Node n = _nodeMap_.get(key);
				
				HashMap<String, Node> singleCluster = new HashMap<String, Node>();
				
				testedNodes.put(key, n);
				addChildNodesToCluster(singleCluster, testedNodes, n, _nodeMap_);
				
				nodeClusters.add(singleCluster);
			}
		}
		
		final int CLUSTERS_PER_ROW = 30;
		final Vector2Int START_POS = new Vector2Int(-700 * 32, -480 * 32);
		
		// Iterate over the clusters to process each cluster individually.
		int counter = 0;
		int nodesPerRow = CLUSTER_SPACE.getX() / NODE_SPACE.getX();
		Vector2Int clusterPos = START_POS.clone();
		for (HashMap<String, Node> singleCluster : nodeClusters) {
			// The starting position for this cluster
			if (counter > CLUSTERS_PER_ROW) {
				counter = 0;
				clusterPos = clusterPos.add(new Vector2Int(0, CLUSTER_SPACE.getY()));
				clusterPos.setX(START_POS.getX());
			}
			else
				clusterPos = clusterPos.add(new Vector2Int(CLUSTER_SPACE.getX(), 0));
			
			Vector2Int offsetPos = new Vector2Int();
			ArrayList<Node> sortedCluster = getNodesInOrderByRefSize(singleCluster);
			int nodesInRow = 0;
			//System.out.println("-------------------------------");
			//System.out.println("Loading next cluster");
			for (Node n : sortedCluster) {
				if (nodesInRow > nodesPerRow) {
					nodesInRow = 0;
					offsetPos.setX(0);
					offsetPos = offsetPos.add(new Vector2Int(0, NODE_SPACE.getY()));
				}
				else {
					offsetPos = offsetPos.add(new Vector2Int(NODE_SPACE.getX(), 0));
				}
				
				Vector2Int curNodePos = clusterPos.add(offsetPos);
				VisualNode vn = new VisualNode(n, curNodePos);
				hmVNodes.put(n.getName(), vn);
				
				//System.out.println("Loading " + n.getName() + " at " + curNodePos.toString());
				
				++nodesInRow;
			}
			
			++counter;
		}
	}
	
	/**
	 * Helper function for loadClusterNodes.
	 * Recursively called to add all the nodes with references to each other into one cluster.
	 * 
	 * @param _currentCluster_
	 * 				The current hash map that contains the nodes in the current cluster.
	 * @param _testedNodes_
	 * 				The nodes that have already been tested.
	 * @param _parentNode_
	 * 				The node whose children we will be adding to the cluster.
	 * @param _allNodes_
	 * 				Hashmap will all the nodes.
	 */
	private void addChildNodesToCluster(HashMap<String, Node> _currentCluster_, HashMap<String, Node> _testedNodes_,
			Node _parentNode_, HashMap<String, Node> _allNodes_) {
		for (String childName : _parentNode_.getReferences()) {
			childName = childName.toUpperCase();
			// Check if the child is already a part of the tested nodes
			if (!_testedNodes_.containsKey(childName)) {
				Node childNode = _allNodes_.get(childName);
				_currentCluster_.put(childName, childNode);
				_testedNodes_.put(childName, childNode);
				// Recursive call with the child node as the new parent.
				addChildNodesToCluster(_currentCluster_, _testedNodes_, childNode, _allNodes_);
			}
		}
	}
	
	private ArrayList<Node> getNodesInOrderByRefSize(HashMap<String, Node> _nodeMap_) {
		ArrayList<Node> sortedArr = new ArrayList<Node>();
		
		Object[] nmArr = _nodeMap_.keySet().toArray();
		for (int i = 0; i < nmArr.length; ++i) {
			String key = ((String)nmArr[i]).toUpperCase();
			Node n = _nodeMap_.get(key);
			
			int mostRefAm = n.getReferences().size();
			Node mostRefNode = n;
			for (int k = i + 1; k < nmArr.length; ++k) {
				String secondKey = ((String)nmArr[k]).toUpperCase();
				Node secondNode = _nodeMap_.get(secondKey);
				
				int currentRefAm = secondNode.getReferences().size();
				if (currentRefAm > mostRefAm) {
					mostRefAm = currentRefAm;
					mostRefNode = secondNode;
				}
			}
			
			sortedArr.add(mostRefNode);
		}
		
		return sortedArr;
	}
	
	/**
	 * Loads the nodes in circles based on the amount of references they have.
	 * 
	 * @param _nodeMap_
	 * 				The nodes to load.
	 */
	private void loadBasedOnRefSize(HashMap<String, Node> _nodeMap_) {
		final int REF_WEIGHT = 1;
		int OG_radius = _nodeMap_.size() * 5;
		float radianIncrement = 2.0f / _nodeMap_.size() * (float)Math.PI;
		int counter = 0;
		Random rand = new Random();
		
		// Create all the nodes in a circle
		for (String key : _nodeMap_.keySet()) {
			Node n = _nodeMap_.get(key);
			
			int sign = rand.nextInt(2);
			sign = sign == 0 ? 1 : -1;
			int refSize = n.getReferences().size();
			refSize = refSize <= 0 ? 1 : refSize;
			float radius = (OG_radius + sign * rand.nextFloat()) * ((refSize) * REF_WEIGHT);
			
			int x = (int)(radius * Math.cos(counter * radianIncrement));
			int y = (int)(radius * Math.sin(counter * radianIncrement));
			
			VisualNode vn = new VisualNode(n, new Vector2Int(x, y));
			hmVNodes.put(vn.getName(), vn);
			
			++counter;
		}
	}
	
	/**
	 * Helper function for load.
	 * Creates connections and loads them into the game controller.
	 * 
	 * @param _nodeMap_
	 * 				The map of nodes from the parser.
	 */
	private void loadConnections(HashMap<String, Node> _nodeMap_) {	
		// Get the hash map of visual nodes that should have been made from the game controller.
		HashMap<String, VisualNode> vnMap = hmVNodes;
		// Iterate over the passed in node map to access references.
		for (String key : _nodeMap_.keySet()) {
			
			Node n = _nodeMap_.get(key);
			
			// Iterate over the current node's references.
			ArrayList<String> currentReferences = n.getReferences();
			for (String otherName : currentReferences) {
				VisualNode node1 = vnMap.get(n.getName());
				VisualNode node2 = vnMap.get(otherName.toUpperCase());
				
				if (node1 != null && node2 != null) {
					NodeConnection conn = new NodeConnection(node1, node2);
				
					gc.addConnection(conn);
				}
			}
		}
	}
	
	private void addNodes() {
		for (String nodeName : hmVNodes.keySet()) {
			VisualNode vn = hmVNodes.get(nodeName);
			gc.addNode(vn);
		}
	}
	
}
