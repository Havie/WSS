import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.IOException;
import java.awt.Desktop;

public class Searcher {
	
	private HashMap<String,Node> map;
	//private ArrayList<String> folders=new ArrayList<String>();
	//Each Folder name will be the key, the value will be a list of its sub files
	private HashMap<String,ArrayList<String>> filesInFolders= new HashMap<String,ArrayList<String>>();
	private HashMap<String,ArrayList<String>> foldersInFolders = new HashMap<String,ArrayList<String>>();	
	
	private String defaultPath="G:/AppPro/MMW/MMWD";
	
	public Searcher()
	{
		map=new HashMap<String,Node>();
	}
	
	public void TraceProgram(String programName)
	{
		TraceProgram(programName, defaultPath, "root");
	}
	
	
	public void TraceProgram(String programName, String path, String lastFolder)
	{
		if(programName==null) {Driver.print("null"); return;}
		if(programName.equals("")){Driver.print("found empty string in programName"); return;}
	
		//G:\AppPro\MMW\MMWD
		
		//File file= new File("G:/AppPro/MMW/MMWD");
		
		//ArrayList<String> folders=new ArrayList<String>();
		//Each Folder name will be the key, the value will be a list of its sub files
		//HashMap<String,ArrayList<String>> filesInFolders= new HashMap<String,ArrayList<String>>();
		//HashMap<String,ArrayList<String>> foldersInFolders = new HashMap<String,ArrayList<String>>();	
		//String lastFolder="root";
		
		ArrayList<String> subfolders=ParseLocation(path, lastFolder);
		
		//Need to add these sub folders to main folder structure?
		
		for (String folder: subfolders)
		{
			ParseLocation(path+"/"+folder, folder);
		}
		
		//Print our "Root Folder"
		TestPrint(subfolders);
		
	}
	
	private ArrayList<String> ParseLocation(String path, String lastFolder)
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
	private void AddFile(String currDir, File file, HashMap<String,ArrayList<String>> filesInFolders)
	{
		Driver.print("Adding a File:"+ file.getName() +"   To CurrDir: " + currDir);
		if( filesInFolders.containsKey(currDir))
		{
			//Folder exists, so look if the list has the file
			ArrayList<String> list= filesInFolders.get(currDir);
			if (! list.contains(file.getName()))
			{
				list.add(file.getName());
				//filesInFolders.put(file.getName(), list);
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
	
	private void TestPrint(ArrayList<String> folders)
	{
		//TEST PRINT
		Driver.print("FOLDERS::");
		int count=0;
		for (String f : folders)
		{
			Driver.print(" ("+count+") "+f);
			++count;
		}
		
		Driver.print("FILES::");
		//TEST FILES
		count=0;
		ArrayList<String> files= filesInFolders.get("root");
		for (String f: files)
		{
			Driver.print("  ("+count+") "+f);
			++count;
		}
	}
}
