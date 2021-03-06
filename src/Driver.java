import java.io.IOException;
import java.time.Instant;


public class Driver {

	public static void main(String[] args) throws IOException
	{
		System.out.println("---Welcome to 2020 Interns: WebSpeed Search (WSS) for WebSpeed9---");

		//BuildTXT(); //Only need to do once, takes ~12min
		
		Parser p = new Parser("C:\\Users\\wsenalik\\");
		p.ImportData(); // Call this on application launch.
		//p.PrintReferencedBy("TRUM500.R");
		p.SaveData(); // Call this on application close. 
		
	}
	
	public static void BuildTXT() throws IOException
	{
	
	    System.out.println("Start= "+Instant.now().toString());
	    Long start =Instant.now().getEpochSecond();
	    //System.out.println("Start Long="+start);
	    
		Searcher searcher= new Searcher();
		//searcher.TraceProgram("MOVE0105.htm");
		//searcher.TraceProgram("FOrk0800");
		//searcher.TraceProgram("load0100x");
		searcher.TraceProgram("G:/AppPro/MMW/MMWD", "MMWD");
		
		searcher.FindReferences();
		

		//searcher.PrintMap();
		searcher.WriteMapToFile();
		
		searcher.PrintAllFilesFound();

		
		//System.out.println("End= "+Instant.now().toString());
		Long End =Instant.now().getEpochSecond();
	    //System.out.print("Start Long="+End);
		System.out.println("End= "+Instant.now().toString());
	    System.out.println("total Time="+ (End-start) +" seconds");
		System.out.println("---      done     ---");
	}

	
	public static void print(String s)
	{
		System.out.println(s);
	}
}


