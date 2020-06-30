import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Driver {

	public static void main(String[] args) throws IOException
	{
		/*
		print("---Welcome to Interns:WebSpeed Search (WSS) for WebSpeed9");
		
		Searcher searcher= new Searcher();
		searcher.TraceProgram("move0105");
		ArrayList<String> seen = new ArrayList<String>();
		seen.add("move0105");

		searcher.BuildPaths();
		
		print("Start deep trace");
		DeepTrace(searcher, seen);
		
		print("Fniished");
		*/
		
		//Display myDisplay = new Display();
		
		Display gDemo = new Display();
		
		/*
		PaintablePolygon testPoly = new PaintablePolygon(new int[]{0, 0, 300, 300},
				new int[]{0, 300, 300, 0}, 4, Color.BLUE ,false);
		gDemo.addPaintableObj(testPoly);
		
		PaintableText testText = new PaintableText("Hello World", Color.BLACK,
				new Font("Serif", Font.PLAIN, 24), 50, 150);
		gDemo.addPaintableObj(testText);
		*/
		
		VisualNode testNode1 = new VisualNode(new Vector2Int(100, 100));
		testNode1.addNodeToDisplay(gDemo);
		
		gDemo.setVisible(true);
	}

	
	
	public static void print(String s)
	{
		System.out.println(s);
	}
	
	private static void DeepTrace(Searcher searcher, ArrayList<String> alreadyTracedPrograms) throws IOException
	{
		ArrayList<Searcher> subSearchers = new ArrayList<Searcher>();
		
		Iterator<Map.Entry<String, Node>> mapIt = searcher.GetMap().entrySet().iterator();
		while (mapIt.hasNext())
		{
			Map.Entry<String, Node> pair = (Map.Entry<String, Node>)mapIt.next();
			String progName = RemoveExtension(pair.getKey());
			
			if (!alreadyTracedPrograms.contains(progName))
			{	
				print("Tracing " + progName);
				Searcher currentSearcher = new Searcher();
				subSearchers.add(currentSearcher);
				
				currentSearcher.TraceProgram(progName);
				alreadyTracedPrograms.add(progName);
				
				DeepTrace(currentSearcher, alreadyTracedPrograms);
			}
			mapIt.remove();
		}
		
		for (Searcher singleSearcher : subSearchers)
		{
			singleSearcher.BuildPaths();
		}
	}
	
	private static String RemoveExtension(String fileName)
	{
		int index = fileName.indexOf('.');
		if (index > 0) {
			return fileName.substring(0, index);
		}
		return fileName;
	}
	
}


