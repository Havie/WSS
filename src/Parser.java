import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser 
{
	private String defaultPath="C:\\Users\\sdatz\\";
	private final String WSSNFile="WSSout.txt";
	private final String rootNodeFile="RootNodes.txt";
	private final String nodeLocsFile="NodeLocs.txt"; 
	
	private final String LOCXTEXT=" LocX=";
	private final String LOCYTEXT=" LocY=";
	private final String MOVEABLETEXT=" Moveable=";
	private HashMap<String,Node> map;


	public Parser()
	{
		map= new HashMap<String,Node>();
	}

	public Parser(String path)
	{
		defaultPath=path;
		map= new HashMap<String,Node>();
	}
	public void ChangeDefaultPath(String path){defaultPath=path;}
	public HashMap<String,Node> GetMap(){return map;}

	/**
	 * Reads the specified file created by Searcher and recreates the map<String,Node>
	 * @throws IOException
	 */
	public void ImportData() throws IOException // this will populate our Nodes and Refs
	{
		Driver.print("----- Parsing Import File: " +defaultPath+WSSNFile+" -----");
		BufferedReader reader = new BufferedReader(new FileReader(defaultPath+WSSNFile));
		String line= reader.readLine();
		Node lastNode=new Node("Dummy");
		String pIndex="Program: ";
		String subIndex= "   ";
		while(line!=null)
		{

			//Driver.print(line);
			if(line.indexOf(pIndex) != -1) // We've got a new program
			{
				String pName= line.substring(pIndex.length(), line.indexOf(" calls:"));
				//Driver.print("Program Name= "+ pName);

				if(!map.containsKey(pName))
				{
					lastNode= new Node(pName);
					//lastNode.SetIsRoot(true);
					map.put(pName, lastNode);
				}
				else
					Driver.print("ERROR, We've Seen this node before, data will be destructive");
			}
			else if (line.indexOf(subIndex) !=-1) //We're looking at sub programs
			{
				String pName= line.substring(subIndex.length(), line.length());
				//Driver.print("    Reference Name= "+ pName);
				ArrayList<String> refList= lastNode.getReferences();
				refList.add(pName);
			}

			line= reader.readLine();
		}
		reader.close();
		MarkRootNodes();
		ParseLocations();
		BuildReferencedBy();
		Driver.print("----- Completed -----");
	}
	private void MarkRootNodes() throws IOException // this will set our rootNodes;
	{
		File f= new File(defaultPath+rootNodeFile);
		if (f.exists() && !f.isDirectory())
		{
			BufferedReader reader = new BufferedReader(new FileReader(defaultPath+rootNodeFile));
			String line= reader.readLine();

			while(line!=null)
			{
				for(String pName : map.keySet())
				{
					if (line.contains(pName))
					{
						map.get(pName).SetIsRoot(true);
						//Driver.print("Marked Root: "+pName);
					}
				}

				line= reader.readLine();
			}
			reader.close();
		}
	}
	private void ParseLocations() throws IOException // this will set our rootNodes;
	{
		File f= new File(defaultPath+nodeLocsFile);
		if (f.exists() && !f.isDirectory())
		{
			BufferedReader reader = new BufferedReader(new FileReader(defaultPath+nodeLocsFile));
			String line= reader.readLine();

			while(line!=null)
			{
				for(String pName : map.keySet())
				{
					if (line.contains(pName))
					{
						//Will have to get a substring -todo
						String locx= line.substring(line.indexOf(LOCXTEXT)+LOCXTEXT.length(), line.indexOf(LOCYTEXT));
						String locy;
						String moveable="true";
						if (line.indexOf(MOVEABLETEXT) <0 )
								locy= line.substring(line.indexOf(LOCYTEXT)+LOCYTEXT.length(), line.length());
						else
							{
								locy= line.substring(line.indexOf(LOCYTEXT)+LOCYTEXT.length(), line.indexOf(MOVEABLETEXT));
								moveable= line.substring(line.indexOf(MOVEABLETEXT)+MOVEABLETEXT.length(), line.length());
							}
						
						//Driver.print(pName+": locx= " +locx  + " locy= " +locy);
						int x= Integer.parseInt(locx);
						int y= Integer.parseInt(locy);
						Vector2Int loc= new Vector2Int(x,y);
						map.get(pName).SetLocation(loc);
						map.get(pName).SetIsMoveable(Boolean.valueOf(moveable));
						//Driver.print("Marked Root: "+pName);
						//Driver.print("Value of= "+Boolean.valueOf(moveable));
					}
				}

				line= reader.readLine();
			}
			reader.close();
		}
	}
	private void BuildReferencedBy() throws IOException //this one will take awhile, lot of looping
	{
		BufferedReader reader = new BufferedReader(new FileReader(defaultPath+WSSNFile));
		String line= reader.readLine();
		Node lastNode=new Node("Dummy");
		String pIndex="Program: "; //size in text file
		String subIndex= "   ";
		while(line!=null)
		{
			for(String Program: map.keySet())
			{	
				String Plower= Program.toLowerCase();  // more efficient to only cast the program name to lower once, than every program found to upper
				//Driver.print(line);
				if(line.indexOf(pIndex) != -1) // We've got a new program
				{
					String pName= line.substring(pIndex.length(), line.indexOf(" calls:"));
					//Driver.print("Program Name= "+ pName);
					if(map.containsKey(pName))
					{
						lastNode=map.get(pName);
					}
					else
						Driver.print("ERROR, We've never seen this node before, data will be destructive");
				}
				else if (line.indexOf(subIndex) !=-1) //We're looking at programs our last node refs
				{
					String pName= line.substring(subIndex.length(), line.length());
					if (Plower.equals(pName))
					{
						ArrayList<String> refList= map.get(Program).getReferencedBy();
						refList.add(lastNode.getName());
					}
				}
				
			}
			line= reader.readLine();
		}
		reader.close();
	}
	public void PrintReferences(String programName)
	{
		System.out.println("Called for:"+programName);
		if ( map.containsKey(programName))
		{
			ArrayList<String> refs= map.get(programName).getReferences();
			if(refs==null)
				Driver.print("none");
			else
			{
				for(String ref : refs)
				{
					Driver.print(ref);
				}
			}
		}
	}
	public void PrintReferencedBy(String programName)
	{
		System.out.println("Called for:"+programName);
		if ( map.containsKey(programName))
		{
			ArrayList<String> refs= map.get(programName).getReferencedBy();
			if(refs==null)
				Driver.print("none");
			else
			{
				for(String ref : refs)
				{
					Driver.print(ref);
				}
			}
		}
	}
	
	public ArrayList<String> GetReferences(String programName)
	{
		if ( map.containsKey(programName))
			return map.get(programName).getReferences();


		return null;
	}
	
	/** 
	 * Call This method before closing the application to save node locations and which node has been 
	 * marked as a root node via the GUI. Will write to a file and re-read from that file next 
	 * time application is launched.
	 * @throws IOException
	 */
	public void SaveData() throws IOException
	{
		UpdateRootNodes();
		UpdateLocations();
		Driver.print("----- Save Completed -----");
	}
	private void UpdateRootNodes() throws IOException
	{
		FileWriter myWriter = new FileWriter(defaultPath+rootNodeFile);
		for (String key: map.keySet())
		{
			if (map.get(key).getIsRoot())
			{
				//Driver.print("Found rootnode: "+key);
				myWriter.write(key);
				myWriter.write("\n");
				
			}
		}
		myWriter.close();
	}
	private void UpdateLocations() throws IOException
	{
		FileWriter myWriter = new FileWriter(defaultPath+nodeLocsFile);
		for (String key: map.keySet())
		{
			//Driver.print("Found rootnode: "+key);
			Node n = map.get(key);
			
			myWriter.write(key+LOCXTEXT+n.getLocX()+LOCYTEXT+n.getLocY()+MOVEABLETEXT+n.getIsMoveable());
			myWriter.write("\n");
		}
		myWriter.close();
	}
	
	
	public String getLocationPath() {
		return defaultPath + nodeLocsFile;
	}
}
