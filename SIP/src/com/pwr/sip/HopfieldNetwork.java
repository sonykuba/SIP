package com.pwr.sip;

import java.util.ArrayList;

public class HopfieldNetwork {

	/**
	 * The weight matrix for this neural network. A Hopfield neural network is a
	 * single layer, fully connected neural network.
	 * 
	 * The inputs and outputs to/from a Hopfield neural network are always
	 * boolean values.
	 */
	private Matrix weightMatrix;
	private double learningRate = 0.7;
	private double desiredError = 2;

	public HopfieldNetwork(final int size) {
		this.weightMatrix = new Matrix(size, size);
		for (int i = 0; i < size; i++)
			for (int l = 0; l < size; l++) {
				if (i != l)
					this.weightMatrix.set(i, l, Math.random());
			}
		this.weightMatrix.clearDiagonal();
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
		return this.weightMatrix.getRows();
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
		final Matrix inputMatrix = Matrix.createRowMatrix(BiPolarUtil.bipolar2double(pattern));

		// Process each value in the pattern
		for (int col = 0; col < pattern.length; col++) {
			Matrix columnMatrix = this.weightMatrix.getCol(col);
			columnMatrix = MatrixMath.transpose(columnMatrix);

			// The output for this input element is the dot product of the
			// input matrix and one column from the weight matrix.
			final double dotProduct = MatrixMath.dotProduct(inputMatrix, columnMatrix);

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
	 * @param pattern
	 *            The pattern to train on.
	 * @throws HopfieldException
	 *             The pattern size must match the size of this neural network.
	 */
	public void learnPseudoInversion(final boolean[] pattern) {
		if (pattern.length != this.weightMatrix.getRows()) {
			throw new Error("Can't train a pattern of size " + pattern.length + " on a hopfield network of size " + this.weightMatrix.getRows());
		}
		final Matrix m2 = Matrix.createRowMatrix(BiPolarUtil.bipolar2double(pattern));
		final Matrix m1 = MatrixMath.transpose(m2);
		final Matrix m3 = MatrixMath.multiply(m1, m2);

		final Matrix identity = MatrixMath.identity(m3.getRows());

		final Matrix m4 = MatrixMath.subtract(m3, identity);

		this.weightMatrix = MatrixMath.add(this.weightMatrix, m4);

	}

	public void learnDelta(final boolean[] pattern, int patternNumber) {
		final Matrix patternMatrix = Matrix.createRowMatrix(BiPolarUtil.bipolar2double(pattern));
		final Matrix actualMatrix = Matrix.createRowMatrix(BiPolarUtil.bipolar2double(present(pattern)));

		final Matrix errorMatrix = MatrixMath.subtract(patternMatrix, actualMatrix);

		final Matrix equationResult = MatrixMath.multiplyMatrixCells(MatrixMath.multiply(errorMatrix, learningRate), patternMatrix);
		final Matrix weightMatrixFix = MatrixMath.multiply(MatrixMath.transpose(equationResult), equationResult);
		weightMatrixFix.clearDiagonal();
		this.weightMatrix = MatrixMath.add(this.weightMatrix, weightMatrixFix);
	}

	public void learnHebb(final boolean[] pattern) {
		final Matrix patternMatrix = Matrix.createRowMatrix(BiPolarUtil.bipolar2double(pattern));
		for (int i = 0; i < this.weightMatrix.getCols(); i++) {
			final Matrix actualMatrix = Matrix.createRowMatrix(BiPolarUtil.bipolar2double(present(pattern)));
			for (int l = 0; l < this.weightMatrix.getRows(); l++) {
				if (i != l) {
					double dotRow = MatrixMath.dotProduct(patternMatrix, this.weightMatrix.getRow(l));
					this.weightMatrix.set(i, l, this.weightMatrix.get(i, l) + (learningRate * ((actualMatrix.get(0, l) * patternMatrix.get(0, l) - actualMatrix.get(0, l) * dotRow))));
				}
			}
		}
	}

	public boolean getError(final boolean[] pattern, int patternNumber) {
		final Matrix patternMatrix = Matrix.createRowMatrix(BiPolarUtil.bipolar2double(pattern));
		final Matrix actualMatrix = Matrix.createRowMatrix(BiPolarUtil.bipolar2double(present(pattern)));

		final Matrix errorMatrix = MatrixMath.subtract(patternMatrix, actualMatrix);

		double value = Math.sqrt(errorMatrix.RMS());
		System.out.println("patternNumber:" + patternNumber + " value:" + value);
		return value < getDesiredError();

	}

	public double getDesiredError() {
		return desiredError;
	}

	public void setDesiredError(double desiredError) {
		this.desiredError = desiredError;
	}

}
