import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;


public class Searcher {
	public final static String DEFAULT_PATH = "G:/AppPro/MMW/MMWD";
	
	
	private HashMap<String,Node> map;
	private HashMap<String,Node> newPrograms;
	//private ArrayList<String> folders=new ArrayList<String>();
	//Each Folder name will be the key, the value will be a list of its sub files
	private HashMap<String,ArrayList<String>> filesInFolders= new HashMap<String,ArrayList<String>>();
	private HashMap<String,ArrayList<String>> foldersInFolders = new HashMap<String,ArrayList<String>>();	
	private HashMap<String,String> pathsToFiles = new HashMap<String,String>();
	private String defaultPath = DEFAULT_PATH;
	private static String outputPath = "C:\\Users\\sdatz\\WSSout.txt";
	private String programName="ignore";

	public Searcher()
	{
		map=new HashMap<String,Node>();
		newPrograms=new HashMap<String,Node>();
	}

	/** Entry Point Into the Program
	 * Explores and builds a mapping of all contents in default location
	 * (no longer requires an initial program name to search for)
	 * @param programName
	 * @throws IOException
	 */
	public void TraceProgram() throws IOException
	{
		//this.programName=programName.toLowerCase(); 
		filesInFolders.clear();
		foldersInFolders.clear();
		TraceProgram(defaultPath, "MMWD"); // could put root here

	}

	/**
	 * Recursively called to explore all subfolders and files of a given path
	 * @param path
	 * @param lastFolder
	 * @throws IOException
	 */
	public void TraceProgram(String path, String lastFolder) throws IOException
	{
		if(programName==null) {Driver.print("null"); return;}
		if(programName.equals("")){Driver.print("found empty string in programName"); return;}

		ArrayList<String> subFolders=ParseLocation(path, lastFolder);

		for (String folder : subFolders)
		{	
			TraceProgram(path+"/"+folder, folder); 
		}

	}
	//Looks at the contents of the folder were in, returns a list subfolders to further explore
	private ArrayList<String> ParseLocation(String path, String lastFolder) throws IOException
	{
		ArrayList<String> _folders= new ArrayList<String>();
		File file= new File(path);
		for (File entry : file.listFiles())
		{
			if(entry.isDirectory())
			{
				AddFolder(lastFolder,entry , _folders, foldersInFolders, path );
			}
			else 
			{
				AddFile( lastFolder,  entry,  filesInFolders, path);

			}
		}
		return _folders;
	}

	private void AddFolder(String currentDir, File folder ,ArrayList<String> folders, HashMap<String,ArrayList<String>> foldersInFolders, String path )
	{
		folders.add(folder.getName()); // collection of subfolders we return
		//Lets not keep folders, we dont need
		//if(!pathsTo.containsKey(folder.getName()))
		//	pathsTo.put(folder.getName(), path); // lets keep track of how to get back here, might be faster

		if(foldersInFolders.containsKey(folder)) //just append to the arrayList for that Folder
		{
			ArrayList<String> list= foldersInFolders.get(folder.getName());
			list.add(folder.getName());
		}
		else //First time seeing-Start a new List
		{
			ArrayList<String> list= new ArrayList<String>();
			list.add(folder.getName());
			foldersInFolders.put(currentDir, list);
		}
	}
	private void AddFile(String currDir, File file, HashMap<String,ArrayList<String>> filesInFolders, String path) throws IOException
	{
		//Driver.print("Adding a File:"+ file.getName() +"   To CurrDir: " + currDir);
		if(!pathsToFiles.containsKey(file.getName()))
			pathsToFiles.put(file.getName(), path); // lets keep track of how to get back here

		if( filesInFolders.containsKey(currDir))
		{
			//Folder exists, so look if the list has the file
			ArrayList<String> list= filesInFolders.get(currDir);
			if (! list.contains(file.getName()))
			{
				list.add(file.getName());
				//filesInFolders.put(file.getName(), list);
				//ReadForMatch(file);
				CreateNode(file); //New faster way, just create a node right away, read later
			}
			else  //Should never happen
				Driver.print("ERROR?:..File already exists in Dir: " +file.getName());
		}
		else //This is the first time we've entered this Directory
		{
			//Create our starting list of files in this dir
			ArrayList<String> list= new ArrayList<String>(); 
			list.add(file.getName());
			//ReadForMatch(file);
			CreateNode(file);
			filesInFolders.put(currDir, list);
		}
	}
	//Creates and adds a Node to our map if it hasn't been created
	private void CreateNode(File file) throws IOException 
	{
		//Driver.print("Found A Match in: "+file.getName() + " for Program:" + programName);
		Node child;
		if ( !map.containsKey(file.getName()))
		{
			//create node
			child= new Node(file.getName());
			map.put(file.getName(),child);
		}
	}

	/** 
	 * Should Probably be renamed to Find References, and take in nothing.
	 * Looks through all found files, and searches them one at a time for references all other found files.
	 * @param programName
	 * @throws IOException
	 */
	public void FindReferences() throws IOException
	{
		//this.programName=programName.toLowerCase(); 
		int count=0;
		for (String fileName : pathsToFiles.keySet())
		{
			if (!fileName.equals(programName)) // probably need to fix extension?
			{	//System.out.println("Looking for :"+fileName+"  at Path: "+pathsTo.get(fileName));
				File file= new File(pathsToFiles.get(fileName)+"/"+fileName);
				//if (!file.isDirectory()) // only adding files to PathsTo
				{
					//Driver.print("We opened file:"+file.getName());
					//ReadForMatch(file);
					Long localStart =Instant.now().getEpochSecond();
					SearchALLOnce(file);
					Long localEnd =Instant.now().getEpochSecond();
					System.out.println("("+count+") Tracing for:" +fileName+ " Time elapsed="+ (localEnd-localStart) +" seconds");
					++count;
				}

			}
		}

	}

	// Opens a File, and checks line by line if it contains any references to any known programs.
	private void SearchALLOnce(File file) throws IOException
	{
		//Driver.print("The Size of= "+pathsToFiles.size());

		//file.setWritable(true, false);
		if (file.getName().contains(".r") || file.getName().contains(".htm") ) //||file.getName().contains(".htm" )
		{	//if(programName.contains("fork0103x"))
			//	Driver.print(".....Looking at:"+file.getName() + "  searching for reference to program: " +programName);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line= reader.readLine();
			boolean valid=line != null;
			while (line != null && valid)
			{ 
				for (String pName: pathsToFiles.keySet())
				{
					//Driver.print(line);
					if(line.contains(FixExtension(pName,true)))
					{
						//Driver.print("Found A Match in: "+file.getName() + " for Program:" + programName);
						Node child = map.get(file.getName());
						if(! FixExtension(child.getName(),true).equals(pName))
						{
							//Driver.print("We added "+programName+" to "+child.getName()+"'s references");
							child.AddReference(pName);
						}
						
						//Debugging
						/*if ( (pName.contains("LOAD0200".toLowerCase()) ) )
								{ if (file.getName().contains("LOAD0400".toLowerCase()))
										Driver.print("We found: " +pName +" in program: "+file.getName() );
								}*/
					}
				}
				line=reader.readLine();
				if(line!=null)
					line=line.toLowerCase();
			}
			reader.close();
		}

	}
	/** 
	 * Deprecated, don't use
	 * @return
	 */
	public ArrayList<String> NewPrograms()
	{
		Driver.print("Called NewPrograms, size=" +newPrograms.size());
		ArrayList<String> keys=new ArrayList<String>();
		for( String key : newPrograms.keySet())
		{
			//Driver.print("newPrograms Key=" +key);
			keys.add(key);
		}
		ClearNewPrograms();
		return keys;
	}

	/**
	 * Depreciated, don't use
	 */
	public void ClearNewPrograms()
	{
		//Driver.print("WeCleared newPrograms");
		newPrograms.clear(); //Clear our programs
		filesInFolders.clear(); 	// !! I think im doing something dumb and not using these wisely?
		foldersInFolders.clear();	// !! I think im doing something dumb and not using these wisely?
	}
	/**
	 * Used to add or remove .r Extensions of a program Name
	 * EX: if true and given ALL001 --> ALL001.r will be returned.
	 * @param s
	 * @param remove
	 * @return
	 */
	public String FixExtension(String s, boolean remove)
	{
		//Driver.print("Fixing Extension :: " +s + "  remove=" + remove);
		String searchable= s;
		if(remove) //Remove .r Extension
		{
			if(searchable.contains(".r"))
			{
				int index=searchable.indexOf('.');
				//Driver.print("Removed EXT: ");
				searchable= searchable.substring(0, index);
			}
		}
		else //Add .r extension
		{
			if(!searchable.contains(".r"))
				searchable=searchable+".r";
		}
		//Driver.print("returning::" +searchable);
		return searchable;
	}
	/** 
	 * Prints the list of found programs and their references to console.
	 */
	/** 
	 * Prints all the Files to console 
	 */
	public void PrintAllFilesFound()
	{
		for( String key : pathsToFiles.keySet())
		{
			Driver.print(pathsToFiles.get(key)+"-->" +key);
		}
	}
	/** 
	 * Prints the mapping of programs and references
	 */
	public void PrintMap()
	{
		for (String key: map.keySet())
		{
			Driver.print("Program: "+key.toUpperCase() + " calls:");
			Node n= map.get(key);
			for(String ref: n.getReferences())
			{
				Driver.print("   "+ref);
			}
		}
	}
	/**
	 * Writes a File of Found Programs to Disk using outputPath
	 * @throws IOException
	 */
	public void WriteMapToFile() throws IOException
	{
		Driver.print("The Size of Files= " + pathsToFiles.size());

		File myOutFile= new File(outputPath);

		if(myOutFile.createNewFile())
			Driver.print("out File Created"); 
		else
			Driver.print("out File Exists"); 

		WriteToFile();
	}
	private void WriteToFile() throws IOException
	{
		FileWriter myWriter = new FileWriter(outputPath);
		for (String key: map.keySet())
		{
			myWriter.write("Program: "+key.toUpperCase() + " calls:");
			myWriter.write("\n");
			Node n= map.get(key);
			for(String ref: n.getReferences())
			{
				myWriter.write("   "+ref);
				myWriter.write("\n");
			}
		}
		myWriter.close();
	}
	/** 
	 * Change default path from "G:/AppPro/MMW/MMWD"
	 * Should be a Folder
	 * @param path
	 */
	public void ChangeStartingPath(String path)
	{
		defaultPath=path;
	}
	/** 
	 * Changes default output from "C:\\Users\\sdatz\\WSSout.txt"
	 * Should include a file name and extension of .txt
	 * @param path
	 */
	public void ChangeOutPutPath(String path)
	{
		outputPath = path;
	}
	
	public static void BuildFile(String searchFolderPath, String saveDest)
	{
		outputPath = saveDest + "\\WSSout.txt";
	
	    System.out.println("Start= "+Instant.now().toString());
	    Long start =Instant.now().getEpochSecond();
	    
		Searcher searcher= new Searcher();
		try {
			File f = new File(searchFolderPath);
			if (!f.exists()) {
				System.out.println("ERROR. Invalid path");
			}
			
			searcher.TraceProgram(searchFolderPath, f.getName());
			searcher.FindReferences();
			searcher.WriteMapToFile();
		} catch (Exception e) {
			e.printStackTrace();
		}

		Long end =Instant.now().getEpochSecond();
	    System.out.println("total Time="+ (end-start) +" seconds");
		System.out.println("---      done     ---");
	}
}
