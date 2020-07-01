
public class Vector4 {
	private float x;	// x portion of the vector
	private float y;	// y portion of the vector
	private float z;	// z portion of the vector
	private float w;	// w portion of the vector
	
	/**
	 * Constructs a Vector4 with 0 values for x,y,z, and w
	 */
	public Vector4(){
		this(0, 0, 0);
	}
	
	/**
	 * Constructs a Vector4 with x and y from a Vector2Int.
	 * 
	 * @param _v2i_
	 * 				Vector2Int to get x and y from.
	 */
	public Vector4(Vector2Int _v2i_) {
		this(_v2i_.getX(), _v2i_.getY(), 0);
	}
	
	/**
	 * Constructs a Vector4 with the given x,y,z values.
	 * 
	 * @param _x_
	 * 				x component of the vector.
	 * @param _y_
	 * 				y component of the vector.
	 * @param _z_
	 * 				z component of the vector.
	 */
	public Vector4(float _x_, float _y_, float _z_) {
		this(_x_, _y_, _z_, 1);
	}
	
	/**
	 * Constructs a Vector4. Copy constructor.
	 * 
	 * @param _vec_
	 * 				The Vector4 to deep copy.
	 */
	public Vector4(Vector4 _vec_) {
		this(_vec_.x, _vec_.y, _vec_.z, _vec_.w);
	}
	
	/**
	 * Constructs a Vector4 with the passed in values.
	 * 
	 * @param _x_
	 * 				x component of the vector.
	 * @param _y_
	 * 				y component of the vector.
	 * @param _z_
	 * 				z component of the vector.
	 * @param _w_
	 * 				w component of the vector.
	 */
	public Vector4(float _x_, float _y_, float _z_, float _w_) {
		this.x = _x_;
		this.y = _y_;
		this.z = _z_;
		this.w = _w_;
	}
	
	/**
	 * Returns x component of the vector.
	 * 
	 * @return float x.
	 */
	public float getX() { return this.x; }
	/**
	 * Returns y component of the vector.
	 * 
	 * @return float y.
	 */
	public float getY() { return this.y; }
	/**
	 * Returns z component of the vector.
	 * 
	 * @return float z.
	 */
	public float getZ() { return this.z; }
	/**
	 * Returns w component of the vector.
	 * 
	 * @return float w.
	 */
	public float getW() { return this.w; }
	/**
	 * Gets x, y, z, or w depending on the index.
	 * 
	 * @param _index_
	 * 				Index of the value (x=0, y=1, z=2, w=3).
	 * @return float.
	 */
	public float get(int _index_) {
		switch (_index_) {
		case 0:
			return getX();
		case 1:
			return getY();
		case 2:
			return getZ();
		case 3:
			return getW();
		default:
			System.out.println("ERROR. Invalid index (Vector4.get).");
			return -1;
		}
	}
	/**
	 * Sets x component of the vector.
	 * 
	 * @param _x_
	 * 				New x component of the vector.
	 */
	public void setX(float _x_) { this.x = _x_; }
	/**
	 * Sets y component of the vector.
	 * 
	 * @param _y_
	 * 				New y component of the vector.
	 */
	public void setY(float _y_) { this.y = _y_; }
	/**
	 * Sets z component of the vector.
	 * 
	 * @param _z_
	 * 				New z component of the vector.
	 */
	public void setZ(float _z_) { this.z = _z_; }
	/**
	 * Sets w component of the vector.
	 * 
	 * @param _w_
	 * 				New w component of the vector.
	 */
	public void setW(float _w_) { this.w = _w_; }
	/**
	 * Sets x, y, z, or w depending on the index.
	 * 
	 * @param _index_
	 * 				Index of the value (x=0, y=1, z=2, w=3).
	 * @param _val_
	 * 				Value that will be set.
	 */
	public void set(int _index_, float _val_) {
		switch (_index_) {
		case 0:
			setX(_val_);
			//System.out.println("Setting X to " + _val_);
			break;
		case 1:
			setY(_val_);
			//System.out.println("Setting Y to " + _val_);
			break;
		case 2:
			setZ(_val_);
			//System.out.println("Setting Z to " + _val_);
			break;
		case 3:
			setW(_val_);
			//System.out.println("Setting W to " + _val_);
			break;
		default:
			System.out.println("ERROR. Invalid index (Vector4.get).");
			break;
		}
	}
	
	/**
	 * Calculates the magnitude of the vector and returns it
	 * 
	 * @return double magnitude of vector.
	 */
	public float mag(){
		return (float)Math.sqrt(this.mag2());
	}
	
	/**
	 * Calculates the magnitude squared of the vector and returns it.
	 * 
	 * @return float squared magnitude of vector.
	 */
	public float mag2() {
		return this.x * this.x + this.y * this.y + this.z * this.z + this.w * this.w;
	}
	
	/**
	 * Calculates the normalized vector and returns it.
	 * Does not change the current vector.
	 * 
	 * @return Vector4 normalized vector.
	 */
	public Vector4 hat() {
		return scale(1.0f / mag2());
	}
	
	/**
	 * Calculates the vector scaled by the value and returns it.
	 * Does not change the current vector.
	 * 
	 * @param _scalar_ float value to multiply the vector by.
	 * @return Vector4 scaled vector.
	 */
	public Vector4 scale(float _scalar_) {
		return new Vector4(this.x * _scalar_, this.y * _scalar_, this.z * _scalar_, this.w * _scalar_);
	}
	
	/**
	 * Calculates the two vectors added together and returns the result.
	 * Does not change the either vector.
	 * 
	 * @param _otherVec_ Vector4 vector to add to the current vector.
	 * @return Vector4 sum of the two vectors.
	 */
	public Vector4 add(Vector4 _otherVec_) {
		return new Vector4(this.x + _otherVec_.x, this.y + _otherVec_.y,
				this.z + _otherVec_.z, this.w + _otherVec_.w);
	}
	
	/**
	 * Calculates the two vectors subtracted and returns the result.
	 * Does not change the either vector.
	 * 
	 * @param _otherVec_
	 * 				vector to subtract from the current vector.
	 * @return Vector4 sum of the two vectors.
	 */
	public Vector4 sub(Vector4 _otherVec_) {
		return new Vector4(this.x - _otherVec_.x, this.y - _otherVec_.y,
				this.z - _otherVec_.z, this.w - _otherVec_.w);
	}
	
	/**
	 * Multiplies the x, y, z, and w components together respectively.
	 * 
	 * @param _otherVec_
	 * 				The other vector in the operation.
	 * @return Vector4 result.
	 */
	public Vector4 mul(Vector4 _otherVec_) {
		return new Vector4(this.x * _otherVec_.x, this.y * _otherVec_.y,
				this.z * _otherVec_.z, this.w * _otherVec_.w);
	}
	
	/**
	 * Creates a vector with -x and -y.
	 * 
	 * @return Vector4
	 */
	public Vector4 flip() {
		return new Vector4(-this.x, -this.y, -this.z, -this.w);
	}
	
	/**
	 * Makes a copy of the current vector and returns it.
	 * 
	 * @return Vector4 new Vector4 object with same data as current.
	 */
	public Vector4 clone() {
		return new Vector4(this.x, this.y, this.z, this.w);
	}
	
	/**
	 * Converts the Vector2Int to a string of the format "(x, y)".
	 * 
	 * @return String
	 */
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
	
	/**
	 * Prints the Vector2Int to the console.
	 */
	public void print() {
		System.out.println(toString());
	}
}
