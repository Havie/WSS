import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Desktop;

public class Searcher {
	
	private HashMap<String,Node> map;
	private HashMap<String,Node> newPrograms;
	//private ArrayList<String> folders=new ArrayList<String>();
	//Each Folder name will be the key, the value will be a list of its sub files
	private HashMap<String,ArrayList<String>> filesInFolders= new HashMap<String,ArrayList<String>>();
	private HashMap<String,ArrayList<String>> foldersInFolders = new HashMap<String,ArrayList<String>>();	
	
	private String defaultPath="G:/AppPro/MMW/MMWD";
	private String programName="";
	
	public Searcher()
	{
		map=new HashMap<String,Node>();
		newPrograms=new HashMap<String,Node>();
	}
	
	public void TraceProgram(String programName) throws IOException
	{
		this.programName=programName.toLowerCase();
		TraceProgram(defaultPath, "MMWD");
		newPrograms.clear(); //Clear our programs
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
				AddFolder(lastFolder,entry , _folders, foldersInFolders );
			}
			else // clean the folders from our files
			{
				AddFile( lastFolder,  entry,  filesInFolders);
				
			}
		}
		return _folders;
	}
	
	private void AddFolder(String currentDir, File folder ,ArrayList<String> folders, HashMap<String,ArrayList<String>> foldersInFolders )
	{
		folders.add(folder.getName());
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
	private void AddFile(String currDir, File file, HashMap<String,ArrayList<String>> filesInFolders) throws IOException
	{
		//Driver.print("Adding a File:"+ file.getName() +"   To CurrDir: " + currDir);
		if( filesInFolders.containsKey(currDir))
		{
			//Folder exists, so look if the list has the file
			ArrayList<String> list= filesInFolders.get(currDir);
			if (! list.contains(file.getName()))
			{
				list.add(file.getName());
				//filesInFolders.put(file.getName(), list);
				ReadForMatch(file);
			}
			else
				Driver.print("..File already exists in Dir");

		}
		else
		{
			ArrayList<String> list= new ArrayList<String>();
			list.add(file.getName());
			filesInFolders.put(currDir, list);
		}
	}

	private void getFiles(String currentDir, File file , HashMap<String,ArrayList<String>> filesInFolders )
	{
		
	}
	private void getFolders()
	{
		
	}
	
	private void TestPrint(ArrayList<String> folders, String folder)
	{
		//TEST PRINT
		Driver.print("FOLDERS:: "+folder);
		int count=0;
		for (String f : folders)
		{
			Driver.print(" ("+count+") "+f);
			++count;
		}
		if (count==0)
			Driver.print("none");
		
		Driver.print("FILES:: "+ folder);
		//TEST FILES
		count=0;
		if(filesInFolders.containsKey(folder))
		{
			ArrayList<String> files= filesInFolders.get(folder);
			for (String f: files)
			{
				Driver.print("  ("+count+") "+f);
				++count;
			}
		}
		else
			Driver.print("none");
	}
	private void ReadForMatch(File file) throws IOException 
	{
		//if (file.getName().contains(programName)){return;}
		file.setReadOnly();
		if (file.getName().contains(".r") ||file.getName().contains(".htm" ))
		{
			//Driver.print("Looking at:"+file.getName());
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line= reader.readLine();
			while (line != null)
			{
				//if (file.getName().equals("fork0800")
				//Driver.print(line);
				if(line.contains(programName))
				{
					Driver.print("Found A Match in"+file.getName());
					Node child;
					if ( !map.containsKey(file.getName()))
					{
						//create node
						 child= new Node(file.getName());
						 map.put(file.getName(),child);
						 newPrograms.put(file.getName(),child);
					}
					//Get the parent node and add link
					child= map.get(file.getName());
					child.AddReference(programName);
					//Keep track of any new programs we visited/updated
					if(!newPrograms.containsKey(file.getName()))
					{
						newPrograms.put(file.getName(),child);
					}
				}
				line=reader.readLine();
				if(line!=null)
					line.toLowerCase();
			}
			reader.close();
		}
	}
	
	public Set<String> NewPrograms()
	{
		return newPrograms.keySet();
	}
	
	public void BuildPaths()
	{
		//Go through each 
	}
}
