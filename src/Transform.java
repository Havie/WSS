import java.util.ArrayList;

public class Transform {
	private Vector2Int v2ScreenPos;		// Position of the transform in pixel coordinates
	private Vector4 v4ScreenScale;	// Scale of the transform in local pixel coordinates
	
	//private Matrix4x4 matScale; // Scale matrix
	//private Matrix4x4 matRotate;// Rotation matrix
	//private Matrix4x4 matTrans;	// Translation matrix
	private Matrix4x4 matTransformation;	// The transformation matrix
	
	// The points that will be affected by the transformation matrix
	private ArrayList<Vector4> alModelPoints;
	private ArrayList<Vector4> alTransModelPoints;	// Model points with transformation applied
	
	/**
	 * Constructs a Transformation. Default.
	 */
	public Transform() {
		this(new ArrayList<Vector4>());
	}
	
	/**
	 * Constructs a Transform with the given model points.
	 */
	public Transform(ArrayList<Vector4> _modelPoints_) {
		v2ScreenPos = new Vector2Int(0, 0);
		v4ScreenScale = new Vector4(1, 1, 1);
		
		//matScale = new Matrix4x4();
		//matRotate = new Matrix4x4();
		//matTrans = new Matrix4x4();
		matTransformation =  new Matrix4x4();
		
		alModelPoints = (ArrayList<Vector4>) _modelPoints_.clone();
		alTransModelPoints = (ArrayList<Vector4>) alModelPoints.clone();
	}
	
	/**
	 * Translates the transform by the given vector.
	 * 
	 * @param _moveVec_
	 * 				The vector that position will be updated by.
	 */
	public void translate(Vector4 _moveVec_) {
		Matrix4x4 matTrans = new Matrix4x4();
		matTrans.setEntry(3, 0, matTrans.getEntry(3, 0) + _moveVec_.getX());
		matTrans.setEntry(3, 1, matTrans.getEntry(3, 1) + _moveVec_.getY());
		matTrans.setEntry(3, 2, matTrans.getEntry(3, 2) + _moveVec_.getZ());
		
		// Also set the screen position
		v2ScreenPos.setX((int)matTrans.getEntry(3, 0));
		v2ScreenPos.setY((int)matTrans.getEntry(3, 1));
		
		// Multiply this matrix to the transformation matrix.
		recalculateTransformationMatrix(matTrans);
	}
	
	/**
	 * Translates the transform by the given vector.
	 * 
	 * @param _moveVec_
	 * 				The vector that position will be updated by.
	 */
	public void translate(Vector2Int _moveVec_) {
		translate(new Vector4(_moveVec_));
	}
	
	/**
	 * Sets the the transform's position.
	 * 
	 * @param _pos_
	 * 				The new position of the transform.
	 */
	public void setPosition(Vector4 _pos_) {
		matTransformation.setEntry(3, 0, _pos_.getX());
		matTransformation.setEntry(3, 1, _pos_.getY());
		matTransformation.setEntry(3, 2, _pos_.getZ());
		
		// Also set the screen position
		v2ScreenPos.setX((int)_pos_.getX());
		v2ScreenPos.setY((int)_pos_.getY());
		
		// Recalculate the model points
		recalculateModelPoints();
	}
	
	/**
	 * Sets the the transform's position.
	 * 
	 * @param _pos_
	 * 				The new position of the transform.
	 */
	public void setPosition(Vector2Int _pos_) {
		setPosition(new Vector4(_pos_));
	}
	
	/**
	 * Scales the current transform.
	 * 
	 * @param _scaleVec_
	 * 				The vector to scale the transform by.
	 * @param _pos_
	 * 				Position to scale about.
	 */
	public void scale(Vector4 _scaleVec_, Vector4 _pos_) {		
		Matrix4x4 matScale = new Matrix4x4();
		matScale.setEntry(0, 0, matScale.getEntry(0, 0) * _scaleVec_.getX());
		matScale.setEntry(1, 1, matScale.getEntry(1, 1) * _scaleVec_.getY());
		matScale.setEntry(2, 2, matScale.getEntry(2, 2) * _scaleVec_.getZ());
				
		// Also set the screen scale
		v4ScreenScale.setX(v4ScreenScale.getX() * _scaleVec_.getX());
		v4ScreenScale.setY(v4ScreenScale.getY() * _scaleVec_.getY());
		
		// Also recalculate the transform matrix
		recalculateTransformationMatrix(matScale);
	}
	
	/**
	 * Scales the current transform.
	 * 
	 * @param _scaleVec_
	 * 				The vector to scale the transform by.
	 * @param _pos_
	 * 				Position to scale about.
	 */
	public void scale(Vector2Int _scaleVec_, Vector4 _pos_) {
		scale(new Vector4(_scaleVec_.getX(), _scaleVec_.getY(), 1), _pos_);
	}
	
	/**
	 * Sets the scale of the transform.
	 * 
	 * @param _sizeVec_
	 * 				The vector representing the new scale.
	 */
	public void setSize(Vector4 _sizeVec_) {
		matTransformation.setEntry(0, 0, _sizeVec_.getX());
		matTransformation.setEntry(1, 1, _sizeVec_.getY());
		matTransformation.setEntry(2, 2, _sizeVec_.getZ());
				
		// Also set the screen scale
		v4ScreenScale.setX(_sizeVec_.getX());
		v4ScreenScale.setY(_sizeVec_.getY());
				
		// Also recalculate the model points
		recalculateModelPoints();
	}
	
	/**
	 * Sets the scale of the transform.
	 * 
	 * @param _sizeVec_
	 * 				The vector representing the new scale.
	 */
	public void setSize(Vector2Int _sizeVec_) {
		setSize(new Vector4(_sizeVec_.getX(), _sizeVec_.getY(), 1));
	}
	
	/**
	 * Recalculates the transformation matrix and the model points.
	 */
	private void recalculateTransformationMatrix(Matrix4x4 _transMat_) {
		matTransformation = _transMat_.mul(matTransformation);		
		recalculateModelPoints();
	}
	
	/**
	 * Recalculates all the model points.
	 */
	private void recalculateModelPoints(){
		Vector4 sumOfPoints = new Vector4();
		
		alTransModelPoints.clear();
		for (int i = 0; i < alModelPoints.size(); ++i) {
			Vector4 result = matTransformation.mul(alModelPoints.get(i));
			alTransModelPoints.add(i, result); 
			
			sumOfPoints = sumOfPoints.add(result);
		}
		
		v2ScreenPos = new Vector2Int(sumOfPoints.scale(1.0f / alModelPoints.size()));
	}
	
	/**
	 * Returns the transformation matrix.
	 * @return Matrix4x4
	 */
	public Matrix4x4 getTransformationMatrix() {
		return matTransformation;
	}
	
	/**
	 * Returns the model points transformed by the transformation matrix.
	 * 
	 * @return ArrayList<Vector4>
	 */
	public ArrayList<Vector4> getTransformedModelPoints() {
		return alTransModelPoints;
	}
	
	/**
	 * Sets the model points of the transform.
	 * 
	 * @param _modelPoints_
	 * 				The new model points.
	 */
	public void setModelPoints(ArrayList<Vector4> _modelPoints_) {
		alModelPoints =  (ArrayList<Vector4>) _modelPoints_.clone();
		// Also need to recalculate alTransModelPoints
		recalculateModelPoints();
	}
	
	/**
	 * Returns the screen position.
	 * 
	 * @return Vector2Int
	 */
	public Vector2Int getScreenPosition() {
		return v2ScreenPos;
	}
	
	/**
	 * Returns the screen scale.
	 * 
	 * @return Vector2Int
	 */
	public Vector4 getScreenScale() {
		return v4ScreenScale;
	}
}
