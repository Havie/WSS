import java.io.IOException;
import java.util.ArrayList;

public class Driver {

	public static void main(String[] args) throws IOException
	{
		print("---Welcome to 2020 Interns: WebSpeed Search (WSS) for WebSpeed9---");
		
		Searcher searcher= new Searcher();
		//searcher.TraceProgram("MOVE0105.htm");
		//searcher.TraceProgram("FOrk0800");
		//searcher.TraceProgram("load0100x");
		searcher.TraceProgram("fork0103");
		ArrayList<String> seen = new ArrayList<String>();
		
		//Here we should get a list of ALL the relevant found program names
		//Then we should Trace Program again on everyone.
		//We need to repeat this until there are no more relevant programs.
		ArrayList<String> set = searcher.NewPrograms();
		ArrayList<String> newPrograms= CloneSet(set);
		//print("Size ="+newPrograms.size());
		int size=newPrograms.size();
		String lastKnown="";
		while(size!=0) 
		{ 	print("While x: "+size);
			for( String program : newPrograms)
			{
				if(!seen.contains(program)) //Means weve already done a search for what specifically calls this program
				{							//Yet somehow we get cut short still... and search for MOVE0105 will end at fork0103 but a search for fork0103 will end at fork0800x 
					print("Tracing for:" +program);
					String searchable= searcher.FixExtension(program, true);
					searcher.TraceProgram(searchable);
					seen.add(program);
					lastKnown=program;
				}
			}
			newPrograms = CloneSet(searcher.NewPrograms()); //See if theres more, not sure if this ever ends
			size=newPrograms.size();
			//print("Size of new program="+size);
		}
		//Once thats true, we call searcher.BuildPaths()
		// this function will get the original program node from the master map, 
		// and go through all of its references tracing them all the way up

		searcher.PrintMap();

		
		String path=searcher.BuildPaths(lastKnown);
		print("result  : "+path);
		
	}
	public static ArrayList<String> CloneSet(ArrayList<String> set)
	{	ArrayList<String> newset= new ArrayList<String>();
		for(String s : set)
		{
			newset.add(s);
		}
		return newset;
		
	}
	
	public static void print(String s)
	{
		System.out.println(s);
	}
}


