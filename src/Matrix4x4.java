
public class Matrix4x4 {
	private float[] faData;	// The matrix data. len=16.
	
	/**
	 * Constructs the 4x4 matrix identity.
	 */
	public Matrix4x4() {
		this(new float[] {1, 0, 0, 0,
						  0, 1, 0, 0,
						  0, 0, 1, 0,
						  0, 0, 0, 1});
	}
	
	/**
	 * Constructs a 4x4 matrix with the passed in data.
	 * 
	 * @param _data_
	 * 				The 4x4 matrix data.
	 */
	public Matrix4x4(float[] _data_) {
		faData = new float[16];
		// Add the first 16 data points in the array, or if contains less than 16
		// add all the data points and add points of the identity matrix.
		int stopPoint = Math.min(16, _data_.length);
		for (int i = 0; i < stopPoint; ++i) {
			faData[i] = _data_[i];
		}
		for (int i = stopPoint; i < 16; ++i) {
			if (i % 5 == 0)
				faData[i] = 1.0f;
			else
				faData[i] = 0.0f;
		}
	}
	
	/**
	 * Multiples the this matrix with the passed in matrix. The result is returned.
	 * Neither matrix has its data changed.
	 * 
	 * @param _otherMat_
	 * 				The right side matrix of the multiplication.
	 * @return Matrix4x4
	 */
	public Matrix4x4 mul(Matrix4x4 _otherMat_) {
		Matrix4x4 rtnMat = new Matrix4x4();
		
		for (int i = 0; i < 4; ++i) {
			for (int k = 0; k < 4; ++k) {
				float total = 0;
				for (int entry = 0; entry < 4; ++entry) {
					total += this.getEntry(entry, i) * _otherMat_.getEntry(k, entry);
				}
				//System.out.println("Col " + k + ". Row " + i + ". Total: " + total);
				rtnMat.setEntry(k, i, total);
			}
		}
		
		return rtnMat;
	}
	
	
	public Vector4 mul(Vector4 _vec_) {
		Vector4 rtnVec = new Vector4();
		
		for (int row = 0; row < 4; ++row) {
			float total = 0;
			//System.out.println("Row " + row);
			for (int entry = 0; entry < 4; ++entry) {
				//System.out.println(getEntry(entry, row) + " * " + _vec_.get(entry));
				total += getEntry(entry, row) * _vec_.get(entry);
			}
			rtnVec.set(row, total);
		}
		
		return rtnVec;
	}
	
	/**
	 * Gets a data entry by column and row.
	 * 
	 * @param _col_
	 * 				Column entry is at.
	 * @param _row_
	 * 				Row entry is at.
	 * @return float.
	 */
	public float getEntry(int _col_, int _row_) {
		
		return faData[getIndex(_col_, _row_)];
	}
	
	/**
	 * Sets an entry at the specified column and row to the passed in value.
	 * 
	 * @param _col_
	 * 				Column of the new entry.
	 * @param _row_
	 * 				Row of the new entry.
	 * @param _entry_
	 * 				Value to insert at the location.
	 */
	public void setEntry(int _col_, int _row_, float _entry_) {
		setEntry(getIndex(_col_, _row_), _entry_);
	}
	
	/**
	 * Sets an entry at the specified index to the passed in value.
	 * 
	 * @param _index_
	 * 				Index to insert at.
	 * @param _entry_
	 * 				Value to insert at the location.
	 */
	private void setEntry(int _index_, float _entry_) {
		if (_index_ < 0 || _index_ > 16) {
			System.out.println("ERROR. Index out of range (Matrix4x4.setEntry).");
			return;
		}
		
		faData[_index_] = _entry_;
	}
	
	/**
	 * Prints the data to the console.
	 */
	public void print() {
		System.out.println(toString());
	}
	
	/**
	 * Converts the matrix to a string of the form [ 0 1 2 3 ]
	 * 											   [ 4 5 6 7 ]
	 * 											   [ 8 9 10 11 ]
	 */
	public String toString() {
		return  "[ " + floatToString(faData[0]) + " " + floatToString(faData[1]) + " " +
					   floatToString(faData[2]) + " " + floatToString(faData[3]) + " ]" + "\n" +
				"[ " + floatToString(faData[4]) + " " + floatToString(faData[5]) + " " +
					   floatToString(faData[6]) + " " + floatToString(faData[7]) + " ]" + "\n" +
				"[ " + floatToString(faData[8]) + " " + floatToString(faData[9]) + " " +
					   floatToString(faData[10]) + " " + floatToString(faData[11]) + " ]" + "\n" +
				"[ " + floatToString(faData[12]) + " " + floatToString(faData[13]) + " " +
					   floatToString(faData[14]) + " " + floatToString(faData[15]) + " ]";
	}
	
	/**
	 * Converts a float to a specific String format.
	 * 
	 * @param _f_
	 * 				Float to change to a String.
	 * @return String.
	 */
	private static String floatToString(float _f_) {
		return String.format("%6s", _f_);
	}
	
	/**
	 * Gets the index in the data array by the column and row.
	 * 
	 * @param _col_
	 * 				Column entry is at.
	 * @param _row_
	 * 				Row entry is at.
	 * @return integer.
	 */
	private static int getIndex(int _col_, int _row_) {
		if (_col_ > 4 || _row_ > 4 || _col_ < 0 || _row_ < 0) {
			System.out.println("ERROR. col/row not in range (Matrix4x4.getIndex).");
			return -1;
		}
		
		return 4 * _row_ + _col_;
	}
	
	/**
	 * Retrieves the position from this matrix.
	 * 
	 * @return Vector4
	 */
	public Vector4 extractPosition() {
		return new Vector4(getEntry(3, 0), getEntry(3, 1), getEntry(3, 2), 1);
	}
	public Vector4 extractScale() {
		Vector4 col0 = new Vector4(getEntry(0, 0), getEntry(0, 1), getEntry(0, 2), 0);
		Vector4 col1 = new Vector4(getEntry(1, 0), getEntry(1, 1), getEntry(1, 2), 0);
		Vector4 col2 = new Vector4(getEntry(2, 0), getEntry(2, 1), getEntry(2, 2), 0);
		
		return new Vector4(col0.mag(), col1.mag(), col2.mag(), 0);
	}
}
