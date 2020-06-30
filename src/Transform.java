
public class Transform {
	private Vector2Int v2ScreenPos;		// Position of the transform in pixel coordinates
	private Vector2Int v2ScreenLocalPos;	// Position of the transform in local pixel coordinates
	private Vector2Int v2ScreenScale;	// Scale of the transform in local pixel coordinates
	
	private Vector4 v4Pos;		// Position of the transform in world coordinates
	private Vector4 v4LocalPos;	// Position of the transform in local coordinates
	private Vector4 v4Scale;	// Scale of the transform in local coordinates
	
	private Matrix4x4 matTrans;	// Transformation matrix
	
	public Transform() {
		v2ScreenPos = new Vector2Int(0, 0);
		v2ScreenLocalPos = new Vector2Int(0, 0);
		v2ScreenScale = new Vector2Int(1, 1);
		
		v4Pos = new Vector4(0, 0, 0, 1);
		v4LocalPos = new Vector4(0, 0, 0, 1);
		v4Scale = new Vector4(1, 1, 1, 1);
		
		matTrans = new Matrix4x4();
	}
}
