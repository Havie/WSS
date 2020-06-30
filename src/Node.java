import java.util.ArrayList;

public class Node {
	//This is My Node
	private String programName;
	private ArrayList<String> references;
	private boolean isRoot;
	
	public Node(String programName)
	{
		this.programName=programName;
		references = new ArrayList<String>();
		isRoot=false;
	}
	public Node(String programName, ArrayList<String> references, boolean isRootNode)
	{
		this.programName=programName;
		this.references=references;
		isRoot=isRootNode;
	}
	
	
	public String getName(){ return programName;}
	public boolean getIsRoot() { return isRoot;}
	public ArrayList<String> getReferences() {return references;}
	public void SetIsRoot(boolean isRootNode) {isRoot=isRootNode;}
	public boolean AddReference(String program)
	{
		//boolean temp= !(references.contains(program) || program.equals(programName));
		//Driver.print(programName+" adds Reference: " +program+ "   ="+temp);
		
		if (references.contains(program) || program.equals(programName))
			return false;
		
		references.add(program);
		return true;
	}
	
	
}
