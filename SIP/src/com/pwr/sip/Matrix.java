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

	public boolean isTheSame(Matrix matrix) {
		for (int i = 0; i < N; i++)
			for (int l = 0; l < M; l++) {
				if (get(l, i) != matrix.get(l, i))
					return false;
			}
		return true;
	}

	public double RMS()
	{
		double value=0;
		for (int i = 0; i < N; i++)
			for (int l = 0; l < M; l++) {
				value+=get(l, i)*get(l, i);
			}
		return value;
	}
	
	public static Matrix createRowMatrix(double[] bipolar2double) {
		Matrix result = new Matrix(1, bipolar2double.length);
		for (int i = 0; i < bipolar2double.length; i++)
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

	public void show() {
		System.out.println("");
		for (int i = 0; i < M; i++) {
			String temp = "";
			for (int l = 0; l < N; l++)
				temp += data[i][l] + " ";
			System.out.println(temp);
		}
	}

	public String toString() {
		String temp = "";
		for (int i = 0; i < M; i++) {
			for (int l = 0; l < N; l++)
				temp += data[i][l] + " ";
			temp+=" \n ";
		}
		return temp;
	}

	public void clearDiagonal() {
		for (int i = 0; i < M; i++)
			data[i][i] = 0;
	}
}
