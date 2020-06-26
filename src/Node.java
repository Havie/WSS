import java.util.ArrayList;

public class Node {
	//This is My Node
	private String programName;
	private ArrayList<String> references;
	
	public Node(String programName)
	{
		this.programName=programName;
		references = new ArrayList<String>();
	}
	public Node(String programName, ArrayList<String> references)
	{
		this.programName=programName;
		this.references=references;
	}
	
	
	public String getName(){ return programName;}
	public ArrayList<String> getReferences() {return references;}
	
	public boolean AddReference(String program)
	{
		if (references.contains(program))
			return false;
		
		references.add(program);
		return true;
	}
}
