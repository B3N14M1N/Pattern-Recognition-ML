package ro.usv.rf.misc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BiFunction;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class DistanceMatrix {
	private double[][] matDist;

	public double[][] getMatDist() {
		return matDist;
	}

	public double d(int i, int j) {
		return matDist[i][j];
	}

	public void setMatDist(double[][] matDist) {
		this.matDist = matDist;
	}

	public DistanceMatrix(double[][] pattern, BiFunction<double[], double[], Double> func) {
		int n = pattern.length;
		matDist = new double[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matDist[i][j] = func.apply(pattern[i], pattern[j]);
			}
		}
	}

	public DistanceMatrix(double[][] pattern, BiFunction<double[], double[], Double> func1,
	                      BiFunction<double[], double[], Double> func2) {
		int n = pattern.length;
		matDist = new double[n][n];

		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				if (i > j)
					matDist[i][j] = func1.apply(pattern[i], pattern[j]);
				else
					matDist[i][j] = func2.apply(pattern[i], pattern[j]);
			}
		}
	}

	public double[][] neighbors(int i) {
		int n = matDist.length;
		double[][] result = new double[2][n];

		Integer[] indices = new Integer[n];
		for (int j = 0; j < n; j++) {
			indices[j] = j;
		}

		Arrays.sort(indices, Comparator.comparingDouble(j -> matDist[i][j]));

		for (int k = 0; k < n; k++) {
			result[0][k] = indices[k];
			result[1][k] = matDist[i][indices[k]];
		}

		return result;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (double[] line : matDist) {
			for (double v : line) {
				s.append(v).append(" ");
			}
			s.append("\n");
		}
		return s.toString();
	}
}
