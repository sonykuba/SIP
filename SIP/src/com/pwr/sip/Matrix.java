package com.pwr.sip;

public class Matrix {
	private final int M; // number of rows
	private final int N; // number of columns
	private final double[][] data; // M-by-N array

	// create M-by-N matrix of 0's
	public Matrix(int M, int N) {
		this.M = M;
		this.N = N;
		data = new double[M][N];
	}

	public Matrix(double[][] matrix) {
		data = matrix;
		this.M = matrix.length;
		this.N = matrix[0].length;
	}

	public int getRows() {
		return M;
	}

	public Matrix getCol(int col) {
		Matrix result = new Matrix(N, 1);
		for (int i = 0; i < N; i++)
			result.data[i][0] = data[i][col];
		return result;
	}

	public static Matrix createRowMatrix(double[] bipolar2double) {
		Matrix result = new Matrix(1,bipolar2double.length);
		for(int i=0;i<bipolar2double.length;i++)
			result.set(0, i, bipolar2double[i]);
		return result;
	}

	public int getCols() {
		return N;
	}

	public void set(final int row, final int col, final double value) {
		data[row][col] = value;
	}

	public double get(final int row, final int col) {
		return data[row][col];
	}

	public double[] toPackedArray() {
		double[] result = new double[M * N];
		for (int i = 0; i < N; i++)
			for (int l = 0; l < M; l++) {
				result[l * N + i] = data[l][i];
			}
		return result;
	}
}
