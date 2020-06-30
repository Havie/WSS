import java.lang.Math;

public class Vector2Int {
	private int x;	// x portion of the vector
	private int y;	// y portion of the vector
	
	/**
	 * Constructs a Vector2Int with 0 values.
	 */
	public Vector2Int(){
		this.x = 0;
		this.y = 0;
	}
	
	/**
	 * Constructs a Vector2Int with the passed in values.
	 * 
	 * @param _x_
	 * 				x component of the vector.
	 * @param _y_
	 * 				y component of the vector.
	 */
	public Vector2Int(int _x_, int _y_) {
		this.x = _x_;
		this.y = _y_;
	}
	
	/**
	 * Returns x component of the vector.
	 * 
	 * @return int x.
	 */
	public int getX() { return this.x; }
	/**
	 * Returns y component of the vector.
	 * 
	 * @return int y.
	 */
	public int getY() { return this.y; }
	/**
	 * Sets x component of the vector.
	 * 
	 * @param _x_
	 * 				New x component of the vector.
	 */
	public void setX(int _x_) { this.x = _x_; }
	/**
	 * Sets y component of the vector.
	 * 
	 * @param _y_
	 * 				New y component of the vector.
	 */
	public void setY(int _y_) { this.y = _y_; }
	
	/**
	 * Calculates the magnitude of the vector and returns it
	 * 
	 * @return double magnitude of vector.
	 */
	public double mag(){
		return Math.sqrt(this.mag2());
	}
	
	/**
	 * Calculates the magnitude squared of the vector and returns it.
	 * 
	 * @return integer squared magnitude of vector.
	 */
	public int mag2() {
		return this.x * this.x + this.y + this.y;
	}
	
	/**
	 * Calculates the normalized vector and returns it.
	 * Does not change the current vector.
	 * 
	 * @return Vector2Int normalized vector.
	 */
	public Vector2Int hat() {
		return scale(1.0 / mag2());
	}
	
	/**
	 * Calculates the vector scaled by the value and returns it.
	 * Does not change the current vector.
	 * 
	 * @param _scalar_ double value to multiply the vector by.
	 * @return Vector2 scaled vector.
	 */
	public Vector2Int scale(double _scalar_) {
		return new Vector2Int((int)(this.x * _scalar_), (int)(this.y * _scalar_));
	}
	
	/**
	 * Calculates the two vectors added together and returns the result.
	 * Does not change the either vector.
	 * 
	 * @param _otherVec_ Vector2 vector to add to the current vector.
	 * @return Vector2 sum of the two vectors.
	 */
	public Vector2Int add(Vector2Int _otherVec_) {
		return new Vector2Int(this.x + _otherVec_.x, this.y + _otherVec_.y);
	}
	
	/**
	 * Calculates the two vectors subtracted and returns the result.
	 * Does not change the either vector.
	 * 
	 * @param _otherVec_ Vector2 vector to subtract from the current vector.
	 * @return Vector2 sum of the two vectors.
	 */
	public Vector2Int sub(Vector2Int _otherVec_) {
		return new Vector2Int(this.x - _otherVec_.x, this.y - _otherVec_.y);
	}
	
	/**
	 * Makes a copy of the current vector and returns it.
	 * 
	 * @return Vector2 new Vector2 object with same data as current.
	 */
	public Vector2Int clone() {
		return new Vector2Int(this.x, this.y);
	}
	
	/**
	 * Converts the Vector2Int to a string of the format "(x, y)".
	 * 
	 * @return String
	 */
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	
	/**
	 * Prints the Vector2Int to the console.
	 */
	public void print() {
		System.out.println(toString());
	}
}
