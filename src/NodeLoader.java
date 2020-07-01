import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class NodeLoader {
	private Parser parser;		// The parser with all the nodes.
	private GameController gc;	// The game controller that the nodes will be loaded into.
	
	private final float NOISE = 1000;	// The amount of noise.
	
	/**
	 * Constructs a NodeLoader.
	 * 
	 * @param _gameCont_
	 * 				The game controller to load to
	 */
	public NodeLoader(GameController _gameCont_){
		parser = new Parser("C:\\Users\\sfdatz\\");
		gc = _gameCont_;
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
		
		loadCircleNodes(nodeMap);
		
		// Create the connections between the loaded nodes
		loadConnections(nodeMap);
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
			gc.addNode(vn);
			
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
		HashMap<String, VisualNode> vnMap = gc.getVisualNodeMap();
		// Iterate over the passed in node map to access references.
		for (String key : _nodeMap_.keySet()) {
			Node n = _nodeMap_.get(key);
			
			// Iterate over the current node's references.
			ArrayList<String> currentReferences = n.getReferences();
			for (String otherName : currentReferences) {
				VisualNode node1 = vnMap.get(n.getName());
				VisualNode node2 = vnMap.get(otherName.toUpperCase());
				
				System.out.println("First Node: " + n.getName() + ". Exists? " + node1 +
						"Second Node: " + otherName + ". Exists? " + node2);
				
				if (node1 != null && node2 != null) {
					NodeConnection conn = new NodeConnection(node1, node2);
				
					gc.addConnection(conn);
				}
			}
		}
	}
	
}
