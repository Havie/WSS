import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Parser 
{
	private String defaultPath="C:\\Users\\sdatz\\WSSout.txt";
	private String isRootPath="C:\\Users\\sdatz\\RootNodes.txt";
	//private String outputFileName="WSS_saved.txt"; //Incase we want to save some data?

	private HashMap<String,Node> map;


	public Parser()
	{
		map= new HashMap<String,Node>();
	}

	public Parser(String path, String rootPath)
	{
		defaultPath=path;
		isRootPath=rootPath;
		map= new HashMap<String,Node>();
	}
	public void ChangeDefaultPath(String path){defaultPath=path;}
	public void ChangeOutputFile(String outpath){isRootPath=outpath;}
	public HashMap<String,Node> GetMap(){return map;}

	/**
	 * Reads the specified file created by Searcher and recreates the map<String,Node>
	 * @throws IOException
	 */
	public void ImportData() throws IOException
	{
		Driver.print("----- Parsing Import File: " +defaultPath+" -----");
		BufferedReader reader = new BufferedReader(new FileReader(defaultPath));
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
	}
	private void MarkRootNodes() throws IOException
	{
		File f= new File(isRootPath);
		if (f.exists() && !f.isDirectory())
		{
			BufferedReader reader = new BufferedReader(new FileReader(isRootPath));
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
	public ArrayList<String> GetReferences(String programName)
	{
		if ( map.containsKey(programName))
			return map.get(programName).getReferences();


		return null;
	}
	public void UpdateRootNodes() throws IOException
	{
		FileWriter myWriter = new FileWriter(isRootPath);
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
}
