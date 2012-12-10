package com.pwr.sip;

import Jama.Matrix;

public class HopfieldNetwork {

	/**
	 * The weight matrix for this neural network. A Hopfield neural network is a
	 * single layer, fully connected neural network.
	 * 
	 * The inputs and outputs to/from a Hopfield neural network are always
	 * boolean values.
	 */
	private Matrix weightMatrix;
	private double learningRate = 0.9;
	private double desiredError = 0.1;

	public HopfieldNetwork(final int size) {
		this.weightMatrix = new Matrix(size, size);
		for (int i = 0; i < size; i++)
			for (int l = 0; l < size; l++) {
				if (i != l)
					this.weightMatrix.set(i, l, Math.random());
			}
		for (int i = 0; i < this.weightMatrix.getColumnDimension(); i++)
			this.weightMatrix.set(i, i, 0);
	}

	/**
	 * Get the weight matrix for this neural network.
	 * 
	 * @return
	 */
	public Matrix getMatrix() {
		return this.weightMatrix;
	}

	/**
	 * Get the size of this neural network.
	 * 
	 * @return
	 */
	public int getSize() {
		return this.weightMatrix.getRowDimension();
	}

	public double getLearningRate() {
		return learningRate;
	}

	public void setLearningRate(double learningRate) {
		this.learningRate = learningRate;
	}

	/**
	 * Present a pattern to the neural network and receive the result.
	 * 
	 * @param pattern
	 *            The pattern to be presented to the neural network.
	 * @return The output from the neural network.
	 * @throws HopfieldException
	 *             The pattern caused a matrix math error.
	 */
	public boolean[] present(final boolean[] pattern) {

		final boolean output[] = new boolean[pattern.length];

		// convert the input pattern into a matrix with a single row.
		// also convert the boolean values to bipolar(-1=false, 1=true)
		final Matrix inputMatrix = new Matrix(BiPolarUtil.bipolar2double(pattern));

		// Process each value in the pattern
		for (int col = 0; col < pattern.length; col++) {
			Matrix columnMatrix = this.weightMatrix.getMatrix(0, this.weightMatrix.getRowDimension() - 1, col, col);
			// columnMatrix = columnMatrix.transpose();

			// The output for this input element is the dot product of the
			// input matrix and one column from the weight matrix.
			final double dotProduct = inputMatrix.times(columnMatrix).get(0, 0);

			// Convert the dot product to either true or false.
			if (dotProduct > 0) {
				output[col] = true;
			} else {
				output[col] = false;
			}
		}

		return output;
	}

	/**
	 * Train the neural network for the specified pattern. The neural network
	 * can be trained for more than one pattern. To do this simply call the
	 * train method more than once.
	 * 
	 * @param patternsArray
	 *            The pattern to train on.
	 * @throws Exception
	 * @throws HopfieldException
	 *             The pattern size must match the size of this neural network.
	 */
	public void learnPseudoInversion(final boolean[][] patternsArray) {

		final Matrix m2 = new Matrix(BiPolarUtil.bipolar2double(patternsArray));
		final Matrix m1 = m2.transpose();
		final Matrix m3 = m1.times(m2);
		for (int i = 0; i < m3.getColumnDimension(); i++)
			m3.set(i, i, 0);

		final Matrix m4 = m3.inverse();

		Matrix m5 = m2.times(m4);
		Matrix m6 = m1.times(m5);
		
		this.weightMatrix = m6;

	}

	public void learnDelta(final boolean[] pattern, int patternNumber) {
		final Matrix patternMatrix = new Matrix(BiPolarUtil.bipolar2double(pattern));
		final Matrix A = patternMatrix.times(this.weightMatrix);
		final Matrix B = patternMatrix.minus(A);
		final Matrix C = patternMatrix.transpose();
		final Matrix D = C.times(B);

		for (int i = 0; i < D.getColumnDimension(); i++)
			D.set(i, i, 0);
		this.weightMatrix = this.weightMatrix.plus(D.times(learningRate / this.weightMatrix.getColumnDimension()));
	}

	public void learnHebb(final boolean[] pattern) {
		final Matrix patternMatrix = new Matrix(BiPolarUtil.bipolar2double(pattern));
		final Matrix actualMatrix = new Matrix(BiPolarUtil.bipolar2double(present(pattern)));
		for (int i = 0; i < this.weightMatrix.getColumnDimension(); i++) {
			for (int l = 0; l < this.weightMatrix.getRowDimension(); l++) {
				if (i != l) {
					this.weightMatrix.set(i, l, this.weightMatrix.get(i, l) + (learningRate * (actualMatrix.get(0, l) * patternMatrix.get(0, l))));
				}
			}
		}
	}

	public boolean getError(final boolean[] pattern, int patternNumber) {
		final Matrix patternMatrix = new Matrix(BiPolarUtil.bipolar2double(pattern));

		final Matrix A = patternMatrix.times(this.weightMatrix);
		final Matrix B = patternMatrix.minus(A);

		double value = Math.sqrt(RMS(B));
		System.out.println("patternNumber:" + patternNumber + " value:" + value);
		return value < getDesiredError();

	}

	public double getDesiredError() {
		return desiredError;
	}

	public void setDesiredError(double desiredError) {
		this.desiredError = desiredError;
	}

	private double RMS(Matrix errorMatrix) {
		double value = 0;
		for (int i = 0; i < errorMatrix.getRowDimension(); i++)
			for (int l = 0; l < errorMatrix.getColumnDimension(); l++) {
				value += errorMatrix.get(i, l) * errorMatrix.get(i, l);
			}
		return value;
	}

	public static double dotProduct(Matrix a, Matrix b) {
		double result = 0;
		for (int i = 0; i < a.getRowDimension(); i++)
			for (int l = 0; l < a.getColumnDimension(); l++) {
				result += a.get(i, l) * b.get(i, l);
			}
		return result;

	}
}
