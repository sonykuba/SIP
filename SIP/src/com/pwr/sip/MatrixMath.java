package com.pwr.sip;

public class MatrixMath {

	public static double dotProduct(Matrix a, Matrix b) {
		final double aArray[] = a.toPackedArray();
		final double bArray[] = b.toPackedArray();
		double result = 0;
		final int length = aArray.length;
		for (int i = 0; i < length; i++) {
			result += aArray[i] * bArray[i];
		}
		return result;
	}

	public static Matrix transpose(Matrix input) {
		final double inverseMatrix[][] = new double[input.getCols()][input
				.getRows()];
		for (int r = 0; r < input.getRows(); r++) {
			for (int c = 0; c < input.getCols(); c++) {
				inverseMatrix[c][r] = input.get(r, c);
			}
		}
		return new Matrix(inverseMatrix);

	}

	public static double vectorLength(final Matrix input) {
		double v[] = input.toPackedArray();
		double rtn = 0.0;
		for (int i = 0; i < v.length; i++) {
			rtn += Math.pow(v[i], 2);
		}
		return Math.sqrt(rtn);
	}

	public static Matrix identity(int size) {
		final Matrix result = new Matrix(size, size);
		for (int i = 0; i < size; i++) {
			result.set(i, i, 1);
		}
		return result;

	}

	public static Matrix multiply(Matrix A,Matrix B) {
		if (A.getCols() != B.getRows())
			throw new RuntimeException("Illegal matrix dimensions.");
		Matrix C = new Matrix(A.getRows(), B.getCols());
		for (int i = 0; i < C.getRows(); i++)
			for (int j = 0; j < C.getCols(); j++)
				for (int k = 0; k < A.getCols(); k++)
					C.set(i,j,(A.get(i,k) * B.get(k,j)));
		return C;
	}

	public static Matrix multiply(final Matrix a, final double b) {
		final double result[][] = new double[a.getRows()][a.getCols()];
		for (int row = 0; row < a.getRows(); row++) {
			for (int col = 0; col < a.getCols(); col++) {
				result[row][col] = a.get(row, col) * b;
			}
		}
		return new Matrix(result);
	}

	public static Matrix subtract(Matrix a, Matrix b) {
		final double result[][] = new double[a.getRows()][a.getCols()];
		for (int resultRow = 0; resultRow < a.getRows(); resultRow++) {
			for (int resultCol = 0; resultCol < a.getCols(); resultCol++) {
				result[resultRow][resultCol] = a.get(resultRow, resultCol)
						- b.get(resultRow, resultCol);
			}
		}
		return new Matrix(result);
	}

	public static Matrix divide(final Matrix a, final double b) {
		final double result[][] = new double[a.getRows()][a.getCols()];
		for (int row = 0; row < a.getRows(); row++) {
			for (int col = 0; col < a.getCols(); col++) {
				result[row][col] = a.get(row, col) / b;
			}
		}
		return new Matrix(result);
	}

	public static Matrix add(Matrix a, Matrix b) {
		final double result[][] = new double[a.getRows()][a.getCols()];
		for (int resultRow = 0; resultRow < a.getRows(); resultRow++) {
			for (int resultCol = 0; resultCol < a.getCols(); resultCol++) {
				result[resultRow][resultCol] = a.get(resultRow, resultCol)
						+ b.get(resultRow, resultCol);
			}
		}
		return new Matrix(result);
	}
	public static Matrix multiplyMatrixCells(Matrix a, Matrix b)
	{
		final double result[][] = new double[a.getRows()][a.getCols()];
		for (int row = 0; row < a.getRows(); row++) {
			for (int col = 0; col < a.getCols(); col++) {
				result[row][col] = a.get(row, col) * b.get(row, col);
			}
		}
		return new Matrix(result);
	}
	
	/**
	 * Convert a boolean array to the form [T,T,F,F]
	 * 
	 * @param b
	 *            A boolen array.
	 * @return The boolen array in string form.
	 */
	public static String formatBoolean(final boolean b[]) {
		final StringBuilder result = new StringBuilder();
		result.append('[');
		for (int i = 0; i < b.length; i++) {
			if (b[i]) {
				result.append("T");
			} else {
				result.append("F");
			}
			if (i != b.length - 1) {
				result.append(",");
			}
		}
		result.append(']');
		return (result.toString());
	}
}
