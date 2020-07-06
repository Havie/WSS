import java.util.ArrayList;

public class ArrayListVec4 extends ArrayList<Vector4> {

	/**
	 * Default
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates and returns a deep copy of a vector4 array list.
	 * 
	 * @return ArrayListVec4.
	 */
	public ArrayListVec4 deepCopy() {
		ArrayListVec4 rtnArr = new ArrayListVec4();
		
		for (Vector4 vec : this) {
			rtnArr.add(new Vector4(vec));
		}
		
		return rtnArr;
	}
}
