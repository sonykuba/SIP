package com.pwr.sip;

import Jama.Matrix;

public class BiPolarUtil {
	public static double bipolar2double(final boolean b) {
		return b ? 1 : -1;
	}

	public static double[][] bipolar2double(final boolean b[]) {
		double[][] result = new double[1][b.length];
		for (int i = 0; i < b.length; i++)
			result[0][i] = b[i] ? 1 : -1;
		return result;
	}

	public static double[][] bipolar2double(final boolean b[][]) {
		double[][] result = new double[b.length][b[0].length];
		for (int i = 0; i < b.length; i++)
			for (int l = 0; l < b[0].length; l++)
				result[i][l] = b[i][l] ? 1 : -1;
		return result;
	}

	public static boolean double2bipolar(final double d) {
		return d > 0 ? true : false;
	}

	public static boolean[] double2bipolar(final double d[]) {
		boolean[] result = new boolean[d.length];
		for (int i = 0; i < d.length; i++)
			result[i] = d[i] > 0 ? true : false;
		return result;
	}

	public static boolean[][] double2bipolar(final double d[][]) {
		boolean[][] result = new boolean[d.length][d[0].length];
		for (int i = 0; i < d.length; i++)
			for (int l = 0; l < d[0].length; l++)
				result[i][l] = d[i][l] > 0 ? true : false;
		return result;
	}

	public static boolean[] double2d_2_1dbipolar(final double[][] pixelArray) {
		boolean[] result = new boolean[pixelArray.length * pixelArray.length];
		for (int i = 0; i < pixelArray.length; i++)
			for (int l = 0; l < pixelArray[0].length; l++)
				result[(i * pixelArray.length) + l] = pixelArray[i][l] > 0 ? true : false;
		return result;
	}

	public static double[][] bipolar_2_2ddouble(final boolean[] b) {
		double[][] result = new double[(int) Math.sqrt(b.length)][(int) Math.sqrt(b.length)];
		for (int i = 0; i < (int) Math.sqrt(b.length); i++)
			for (int l = 0; l < (int) Math.sqrt(b.length); l++)
				result[i][l] = b[(i * ((int) Math.sqrt(b.length))) + l] ? 1 : -1;
		return result;
	}

	public static boolean[] matrix2bipolar(final Matrix matrix) {
		double[][] mat = matrix.getArray();
		boolean[] result = new boolean[mat.length * mat[0].length];
		for (int i = 0; i < mat.length; i++)
			for (int l = 0; l < mat.length; l++) {
				result[i * mat.length + l] = mat[i][l] > 0 ? true : false;
			}
		return result;
	}
}
