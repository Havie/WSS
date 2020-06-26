import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class Driver {

	public static void main(String[] args) throws IOException
	{
		print("---Welcome to Interns:WebSpeed Search (WSS) for WebSpeed9");
		
		Searcher searcher= new Searcher();
		searcher.TraceProgram("MOVE0105");
		ArrayList<String> seen = new ArrayList<String>();
		
		//Here we should get a list of ALL the relevant found program names
		//Then we should Trace Program again on everyone.
		//We need to repeat this until there are no more relevant programs.
		Set<String> NewPrograms = searcher.NewPrograms();
		int test=0;
		while( NewPrograms.size()!=0) 
		{ 	print("While x:"+test++);
			for( String program : searcher.NewPrograms())
			{
				if(! seen.contains(program))
				{
					searcher.TraceProgram(program);
					seen.add(program);
					NewPrograms.remove(program);
				}
			}
			NewPrograms = searcher.NewPrograms(); //See if theres more, not sure if this ever ends
		}
		//Once thats true, we call searcher.BuildPaths()
		// this function will get the original program node from the master map, 
		// and go through all of its references tracing them all the way up
		searcher.BuildPaths();
		print("Fniished");

		
	}

	
	
	public static void print(String s)
	{
		System.out.println(s);
	}
}


