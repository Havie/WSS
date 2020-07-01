import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

public class NodeLoader {
	private Parser parser;
	private GameController gc;
	
	private final float NOISE = 1000;
	
	public NodeLoader(GameController _gameCont_){
		parser = new Parser("C:\\Users\\wsenalik\\WSSout.txt", "C:\\Users\\wsenalik\\RootNodes.txt");
		gc = _gameCont_;
	}
	
	/**
	 * Loads the nodes in the parser into the game controller
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException 
	{
		parser.ImportData();
		
		HashMap<String, Node> nodeMap = parser.GetMap();
		
		int OG_radius = nodeMap.size() * 5;
		float radianIncrement = 2.0f / nodeMap.size() * (float)Math.PI;
		int counter = 0;
		Random rand = new Random();
		
		for (String key : nodeMap.keySet()) {
			Node n = nodeMap.get(key);
			
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
	
}
