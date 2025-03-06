package ro.usv.rf.misc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class DistanceMatrix {
	private final double[] matDist;
	private final int n;

	public DistanceMatrix(double[][] pattern, BiFunction<double[], double[], Double> func) {
		this.n = pattern.length;
		int size = (n * (n + 1)) / 2; // Storage size for lower triangle
		matDist = new double[size];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j <= i; j++) {
				matDist[index(i, j)] = func.apply(pattern[i], pattern[j]);
			}
		}
	}

	private int index(int i, int j) {
		if (i < j) {
			int temp = i;
			i = j;
			j = temp;
		}
		return (i * (i + 1)) / 2 + j;
	}

	public double d(int i, int j) {
		return matDist[index(i, j)];
	}

	public double[][] neighbors(int i) {
		double[][] result = new double[2][n];
		Integer[] indices = new Integer[n];

		for (int j = 0; j < n; j++) {
			indices[j] = j;
		}

		Arrays.sort(indices, Comparator.comparingDouble(j -> d(i, j)));

		for (int k = 0; k < n; k++) {
			result[0][k] = indices[k];
			result[1][k] = d(i, indices[k]);
		}

		return result;
	}

	public double[][] getMatDist() {
		double[][] mat = new double[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				mat[i][j] = d(i, j);
			}
		}

		return mat;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				s.append(d(i, j)).append(" ");
			}
			s.append("\n");
		}
		return s.toString();
	}
}