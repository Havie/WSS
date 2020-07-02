import java.util.ArrayList;

public class Transform {
	private Matrix4x4 matParentTrans;		// The transformation matrix of the parents of this transform
	private Matrix4x4 matTransformation;	// The transformation matrix
	
	// The points that will be affected by the transformation matrix
	private ArrayListVec4 alModelPoints;
	private ArrayListVec4 alTransModelPoints;	// Model points with transformation applied
	
	// Hierarchy
	private Transform parent;
	private ArrayList<Transform> children;
	
	// The visual component of the transform, if any
	private PaintableObject paintComp;
	
	/**
	 * Constructs a Transformation. Default.
	 */
	public Transform() {
		this(new ArrayListVec4());
	}
	
	/**
	 * Constructs a Transform with the given model points.
	 */
	public Transform(ArrayListVec4 _modelPoints_) {
		this(_modelPoints_, null);
	}
	
	/**
	 * Constructs a Transform with the given model points and paintableComp.
	 */
	public Transform(ArrayListVec4 _modelPoints_, PaintableObject _paintComp_) {
		matParentTrans = new Matrix4x4();
		matTransformation = new Matrix4x4();
		
		if (_modelPoints_ != null) {
			alModelPoints = _modelPoints_.deepCopy();
			alTransModelPoints = alModelPoints.deepCopy();
		}
		else {
			alModelPoints = new ArrayListVec4();
			alTransModelPoints = new ArrayListVec4();
		}
		
		parent = null;
		children = new ArrayList<Transform>();
		
		paintComp = _paintComp_;
	}
	
	/**
	 * Helper function.
	 * Returns the parent's transformation matrix multiplied by this transformation matrix.
	 * 
	 * @return Matrix4x4
	 */
	protected Matrix4x4 getCompleteTransMatrix() {
		return matParentTrans.mul(matTransformation);
	}
	
	/**
	 * Translates the transform by the given vector.
	 * 
	 * @param _moveVec_
	 * 				The vector that position will be updated by.
	 */
	public void translate(Vector4 _moveVec_) {
		Matrix4x4 matTrans = new Matrix4x4();
		matTrans.setEntry(3, 0, _moveVec_.getX());
		matTrans.setEntry(3, 1, _moveVec_.getY());
		matTrans.setEntry(3, 2, _moveVec_.getZ());
		
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
	 * Sets the the transform's local position.
	 * 
	 * @param _pos_
	 * 				The new local position of the transform.
	 */
	public void setLocalPosition(Vector4 _pos_) {
		matTransformation.setEntry(3, 0, _pos_.getX());
		matTransformation.setEntry(3, 1, _pos_.getY());
		matTransformation.setEntry(3, 2, _pos_.getZ());
		
		// Also recalculate the model points and update the children
		recalculateModelPoints();
		updateChildren();
	}
	/**
	 * Sets the the transform's local position.
	 * 
	 * @param _pos_
	 * 				The new local position of the transform.
	 */
	public void setLocalPosition(Vector2Int _pos_) {
		setLocalPosition(new Vector4(_pos_));
	}
	
	/**
	 * Sets the transform's world position.
	 * 
	 * @param _pos_
	 * 				The new world position of the transform.
	 */
	public void setPosition(Vector4 _pos_) {
		if (parent != null) {
			Matrix4x4 parentMat = parent.getTransformationMatrix();
			Vector4 localPos = this.extractPosition();
			
			// This will absolutely not work if rotation is ever added
			float x = (_pos_.getX() - parentMat.getEntry(3, 0) * localPos.getW()) / parentMat.getEntry(0, 0);
			float y = (_pos_.getY() - parentMat.getEntry(3, 1) * localPos.getW()) / parentMat.getEntry(1, 1);
			float z = (_pos_.getZ() - parentMat.getEntry(3, 2) * localPos.getW()) / parentMat.getEntry(2, 2);
			
			setLocalPosition(new Vector4(x, y, z, localPos.getW()));
		}
		else
			setLocalPosition(_pos_);
	}
	/**
	 * Sets the transform's world position.
	 * 
	 * @param _pos_
	 * 				The new world position of the transform.
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
		matScale.setEntry(0, 0, _scaleVec_.getX());
		matScale.setEntry(1, 1, _scaleVec_.getY());
		matScale.setEntry(2, 2, _scaleVec_.getZ());
		
		translate(_pos_.flip());
		
		// Apply the scale transformation
		recalculateTransformationMatrix(matScale);
		
		translate(_pos_);
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
	 * Scales the current transform.
	 * 
	 * @param _scaleVec_
	 * 				The vector to scale the transform by.
	 */
	public void scale(Vector2Int _scaleVec_) {
		scale(new Vector4(_scaleVec_));
	}
	/**
	 * Scales the current transform.
	 * 
	 * @param _scaleVec_
	 * 				The vector to scale the transform by.
	 */
	public void scale(Vector4 _scaleVec_) {
		scale(_scaleVec_, new Vector4());
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
				
		// Also recalculate the model points and update the children
		recalculateModelPoints();
		updateChildren();
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
		
		// Recalculate the model points
		recalculateModelPoints();
		
		// Recalculate the parent matrix for the children
		updateChildren();
	}
	
	/**
	 * Sets the model points of the transform.
	 * 
	 * @param _modelPoints_
	 * 				The new model points.
	 */
	public void setModelPoints(ArrayListVec4 _modelPoints_) {
		alModelPoints = _modelPoints_.deepCopy();
		// Also need to recalculate alTransModelPoints
		recalculateModelPoints();
	}
	
	/**
	 * Helper function for updateChildren.
	 * Allows the parent to set the child's parentTrans.
	 * 
	 * @param _parentTrans_
	 * 				The new parent transform.
	 */
	private void setParentTransMatrix(Matrix4x4 _parentTrans_) {
		matParentTrans = _parentTrans_;
		recalculateModelPoints();
	}
	
	/**
	 * Recalculates all the model points.
	 */
	private void recalculateModelPoints(){
		Vector4 sumOfPoints = new Vector4();
		
		alTransModelPoints.clear();
		for (int i = 0; i < alModelPoints.size(); ++i) {
			Vector4 result = getCompleteTransMatrix().mul(alModelPoints.get(i));
			alTransModelPoints.add(i, result); 
			
			sumOfPoints = sumOfPoints.add(result);
		}
		
		// Repaint the paintable object now that the model points are updated
		if (paintComp != null) {
			paintComp.updateObjectVisuals();
			paintComp.repaint();
		}
	}
	
	/**
	 * Sets the parent of the transform.
	 * 
	 * @param _parent_
	 * 				The new parent of the transform.
	 */
	public void setParent(Transform _parent_) {
		parent = _parent_;
		_parent_.setChild(this);
	}
	/**
	 * Helper function for setParent.
	 * Adds a child to the parent.
	 * 
	 * @param _child_
	 * 				New child to add.
	 */
	private void setChild(Transform _child_) {
		children.add(_child_);
	}
	
	/**
	 * Sets the given child's parent to be the worldTrans.
	 * 
	 * @param _child_
	 * 				New child.
	 */
	public void addChild(Transform _child_) {
		_child_.setParent(this);
	}
	
	/**
	 * Extracts the position vector from the full transformation matrix.
	 * 
	 * @return Vector4
	 */
	private Vector4 extractPosition() {
		Matrix4x4 fullTransMat = getCompleteTransMatrix();
		
		return fullTransMat.extractPosition();
	}
	/**
	 * Extracts the scale vector from the full transformation matrix.
	 * 
	 * @return Vector4
	 */
	private Vector4 extractScale() {
		Matrix4x4 fullTransMat = getCompleteTransMatrix();
		
		return fullTransMat.extractScale();
	}
	
	public void flipChildHierarchy(){
		ArrayList<Transform> oldHierarchy = (ArrayList<Transform>) children.clone();
		
		children.clear();
		
		int index = oldHierarchy.size() - 1;
		while (index >= 0) {
			children.add(oldHierarchy.get(index));
			--index;
		}
	}
	
	/**
	 * Called when this transform's transformation matrix is changed.
	 * Updates the matParentTrans of all this transform's children.
	 */
	private void updateChildren() {
		// Update each child's parent transform and recursively call this.
		for (Transform child : children) {
			child.setParentTransMatrix(getCompleteTransMatrix());
			child.updateChildren();
		}
	}
	
	/**
	 * Sets the paintable component.
	 * 
	 * @param _paintComp_
	 * 					The paintable component.
	 */
	public void setPaintableComponent(PaintableObject _paintComp_) {
		paintComp = _paintComp_;
	}
	
	/**
	 * Returns the transformation matrix.
	 * @return Matrix4x4
	 */
	public Matrix4x4 getTransformationMatrix() { return matTransformation; }
	/**
	 * Returns the model points transformed by the transformation matrix.
	 * 
	 * @return ArrayListVec4
	 */
	public ArrayListVec4 getTransformedModelPoints() { return alTransModelPoints; }
	/**
	 * Returns the screen position.
	 * 
	 * @return Vector2Int
	 */
	public Vector2Int getScreenPosition() { return new Vector2Int(extractPosition()); }
	/**
	 * Returns the screen scale.
	 * 
	 * @return Vector2Int
	 */
	public Vector4 getScreenScale() { return extractScale(); }
	/**
	 * Returns the paintable object.
	 * 
	 * @return Paintable Object.
	 */
	public PaintableObject getPaintComp() { return paintComp; }
}
