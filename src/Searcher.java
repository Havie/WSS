import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.awt.Desktop;

public class Searcher {

	private HashMap<String,Node> map;
	private HashMap<String,Node> newPrograms;
	//private ArrayList<String> folders=new ArrayList<String>();
	//Each Folder name will be the key, the value will be a list of its sub files
	private HashMap<String,ArrayList<String>> filesInFolders= new HashMap<String,ArrayList<String>>();
	private HashMap<String,ArrayList<String>> foldersInFolders = new HashMap<String,ArrayList<String>>();	
	private HashMap<String,String> pathsToFiles = new HashMap<String,String>();
	private ArrayList<String> buildingPathsSeen;
	private String defaultPath="G:/AppPro/MMW/MMWD";
	private String programName="";

	public Searcher()
	{
		map=new HashMap<String,Node>();
		newPrograms=new HashMap<String,Node>();
		buildingPathsSeen= new ArrayList<String>();
	}

	public void TraceProgram(String programName) throws IOException
	{
		this.programName=programName.toLowerCase(); 
		filesInFolders.clear();
		foldersInFolders.clear();
		TraceProgram(defaultPath, "MMWD");

	}


	private void TraceProgram(String path, String lastFolder) throws IOException
	{
		if(programName==null) {Driver.print("null"); return;}
		if(programName.equals("")){Driver.print("found empty string in programName"); return;}

		ArrayList<String> subFolders=ParseLocation(path, lastFolder);

		//Need to add these sub folders to main folder structure?
		for (String folder : subFolders)
		{	
			TraceProgram(path+"/"+folder, folder); 
		}

		//Print our "Root Folder"
		//TestPrint(subFolders, lastFolder);

	}

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
			else // clean the folders from our files
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
			//Do I have to readd the list, or are we working on the same list? PassBy reference?
		}
		else //Start the new arrayList
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
				CreateNode(file);
			}
			else  
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

	private void PrintAllFilesFound()
	{
		for( String key : pathsToFiles.keySet())
		{
			Driver.print(pathsToFiles.get(key)+"//" +key);
		}
	}
	private void ReadForMatch(File file) throws IOException 
	{
		//if (file.getName().contains(programName)){return;}
		//Driver.print("looking @"+file.getName() + "  for program:  "+ programName);
		//file.setReadable(true, false);
		if (file.getName().contains(".r") ) //||file.getName().contains(".htm" )
		{	//if(programName.contains("fork0103x"))
			//	Driver.print(".....Looking at:"+file.getName() + "  searching for reference to program: " +programName);

			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line= reader.readLine();
			boolean valid=line != null;
			//Driver.print("found=="+valid);

			while (line != null && valid)
			{ 
				//Driver.print(line);
				if(line.contains(programName))
				{
					//Driver.print("Found A Match in: "+file.getName() + " for Program:" + programName);
					Node child;
					if ( !map.containsKey(file.getName()))
					{
						//create node
						child= new Node(file.getName());
						map.put(file.getName(),child);
						//Driver.print("We added "+file.getName()+" to newPrograms  while looking for program: " + programName);
						newPrograms.put(file.getName(),child);
					}
					//Get the parent node and add link
					child= map.get(file.getName());
					if(! FixExtension(child.getName(),true).equals(programName))
					{
						//Driver.print("We added "+programName+" to "+child.getName()+"'s references");
						child.AddReference(programName);

						//Keep track of any new programs we visited/updated
						if(! newPrograms.containsKey(file.getName()))
						{
							//Driver.print("We added"+file.getName()+" to newPrograms");
							newPrograms.put(file.getName(),child);
						}
					}
					valid=false; // break out of this program
				}

				line=reader.readLine();
				if(line!=null)
					line=line.toLowerCase();
			}
			reader.close();
		}
	}

	public void SearchForSpecific(String programName) throws IOException
	{
		this.programName=programName.toLowerCase(); 
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
					//System.out.println("("+count+") Tracing for:" +fileName+ " Time elapsed="+ (localEnd-localStart) +" seconds");
					++count;
				}

			}
		}

	}
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
		//Get the parent node and add link
		/*child= map.get(file.getName());
		if(! FixExtension(child.getName(),true).equals(programName))
		{
			//Driver.print("We added "+programName+" to "+child.getName()+"'s references");
			child.AddReference(programName);

			//Keep track of any new programs we visited/updated
			if(! newPrograms.containsKey(file.getName()))
			{
				//Driver.print("We added"+file.getName()+" to newPrograms");
				newPrograms.put(file.getName(),child);
			}
		}*/
	}
	public void SearchALLOnce(File file) throws IOException
	{
		//Driver.print("The Size of= "+pathsToFiles.size());
		
		if (file.getName().contains("all0001"))
			//Driver.print("RESET:"+file.getName());
				//Driver.print("looking @"+file.getName() + "  for program:  "+ programName);
				file.setWritable(true, false);
				if (file.getName().contains(".r") || file.getName().contains(".htm") ) //||file.getName().contains(".htm" )
				{	//if(programName.contains("fork0103x"))
					//	Driver.print(".....Looking at:"+file.getName() + "  searching for reference to program: " +programName);

					BufferedReader reader = new BufferedReader(new FileReader(file));
					String line= reader.readLine();
					boolean valid=line != null;
					//Driver.print("found=="+valid);

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
								//break;
								
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

	public String BuildPaths(String programName)
	{
		//Driver.print("BuildingPath :: " +programName);

		if( buildingPathsSeen.contains(programName))
			return programName;
		else
			buildingPathsSeen.add(programName);

		if(programName==null || programName=="")
			return programName;

		String s=programName;
		//Go through each 

		String searchable= FixExtension(programName,false);
		//if(!searchable.contains(".r"))
		//searchable=searchable+".r";

		Node n=map.get(searchable.toLowerCase());  //.toLowerCase()
		if (n==null)
		{
			//Driver.print("N is nulll???? for :  "+searchable);
			return s;
		}
		//Driver.print("   has ref= #"+ n.getReferences());
		ArrayList<String> refs= n.getReferences();
		if(refs == null || refs.size()==0 )
		{
			return s;
		}
		else
		{	//Driver.print("What is: " +refs.get(0) );
			return s+ "--> " + BuildPaths(refs.get(0));
		}
	}
	public void ClearNewPrograms()
	{
		//Driver.print("WeCleared newPrograms");
		newPrograms.clear(); //Clear our programs
		filesInFolders.clear(); 	// !! I think im doing something dumb and not using these wisely?
		foldersInFolders.clear();	// !! I think im doing something dumb and not using these wisely?
	}
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

	public void WriteMapToFile() throws IOException
	{
		Driver.print("The Size of Files= " + pathsToFiles.size());
		
		File myOutFile= new File("C:\\Users\\sdatz\\WSSout.txt");

		if(myOutFile.createNewFile())
			Driver.print("out File Created"); 
		else
			Driver.print("out File Exists"); 
		
		WriteToFile();
	}
	private void WriteToFile() throws IOException
	{
		FileWriter myWriter = new FileWriter("C:\\Users\\sdatz\\WSSout.txt");
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
}
