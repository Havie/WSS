import java.util.ArrayList;

public class Node {
	//This is My Node
	private String programName;
	private ArrayList<String> references;
	private ArrayList<String> referencedBy;
	private Vector2Int location;
	private boolean IsMoveable;
	private boolean isRoot;
	
	public Node(String programName)
	{
		this.programName=programName;
		references = new ArrayList<String>();
		referencedBy= new ArrayList<String>();
		isRoot=false;
		location = new Vector2Int(); //start at 0,0
		IsMoveable=true;
	}
	public Node(String programName, ArrayList<String> references, boolean isRootNode)
	{
		this.programName=programName;
		this.references=references;
		referencedBy= new ArrayList<String>(); // meh? add later
		isRoot=isRootNode;
		IsMoveable=true;
	}
	
	
	public String getName(){ return programName;}
	public boolean getIsRoot() { return isRoot;}
	public boolean getIsMoveable() {return IsMoveable;}
	public ArrayList<String> getReferences() {return references;}
	public ArrayList<String> getReferencedBy() {return referencedBy;}
	public ArrayList<String> getCombinedReferences() {
		ArrayList<String> rtnList = new ArrayList<String>();
		for (String refName : references) {
			rtnList.add(refName);
		}
		for (String refdName : referencedBy) {
			if (!rtnList.contains(refdName)) {
				rtnList.add(refdName);
			}
		}
		
		return rtnList;
	}
	public void SetIsRoot(boolean isRootNode) {isRoot=isRootNode;}
	public void SetIsMoveable(boolean isMoveable){this.IsMoveable=isMoveable;}
	public boolean AddReference(String program)
	{
		//boolean temp= !(references.contains(program) || program.equals(programName));
		//Driver.print(programName+" adds Reference: " +program+ "   ="+temp);
		
		if (references.contains(program) || program.equals(programName))
			return false;
		
		references.add(program);
		return true;
	}
	public boolean AddReferencedBy(String program)
	{
		if (referencedBy.contains(program) || program.equals(programName))
			return false;
		
		referencedBy.add(program);
		return true;
	}
	public void SetLocation(Vector2Int loc){location=loc;}
	public int getLocX() { return location.getX();}
	public int getLocY() { return location.getY();}
	
}
