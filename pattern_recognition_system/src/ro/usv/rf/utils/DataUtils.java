package ro.usv.rf.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class DataUtils {
	public static void printMatrix(double[][] x) {
		for (double[] row : x) {
			for (double value : row) {
				System.out.print(value + "\t");
			}
			System.out.println();
		}
	}

	public static double[][] normalizeLearningSet(double[][] patternSet) {
		int rows = patternSet.length;
		int cols = patternSet[0].length;
		double[][] normalizedPatternSet = new double[rows][cols];

		for (int j = 0; j < cols; j++) {
			double min = Double.MAX_VALUE;
			double max = Double.MIN_VALUE;
			for (double[] doubles : patternSet) {
				min = Math.min(min, doubles[j]);
				max = Math.max(max, doubles[j]);
			}
			for (int i = 0; i < rows; i++) {
				normalizedPatternSet[i][j] = (patternSet[i][j] - min) / (max - min);
			}
		}
		return normalizedPatternSet;
	}

	public static double[][] convertToBiDimensionalArray(List<ArrayList<Double>> matrixList) {
		return matrixList.stream()
				.map(lineList -> lineList.stream()
						.mapToDouble(Double::doubleValue)
						.toArray())
				.toArray(double[][]::new);
	}
}
