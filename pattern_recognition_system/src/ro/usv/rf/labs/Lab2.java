package ro.usv.rf.labs;

import ro.usv.rf.utils.FileUtils1;
import ro.usv.rf.utils.StatisticsUtils;

public class Lab2 {
	public static void main(String[] args) {
		double[][] patternSet = FileUtils1.readMatrixFromFileStream("input/in.txt");
		int numberOfPatterns = patternSet.length;
		int numberOfFeatures = patternSet[0].length;

		double[] featureAverages = new double[numberOfFeatures];
		for (int j = 0; j < numberOfFeatures; j++) {
			double[] feature = new double[numberOfPatterns];
			for (int i = 0; i < numberOfPatterns; i++) {
				feature[i] = patternSet[i][j];
			}
			featureAverages[j] = StatisticsUtils.calculateFeatureAverage(feature);
			System.out.println("Feature average: " + featureAverages[j]);
		}

		var patternsMap = StatisticsUtils.getPatternsMapFromInitialSet(patternSet);
		double[] weightedAverages = StatisticsUtils.calculateWeightedAverages(patternsMap, numberOfFeatures);
		double[] patternFrequency = StatisticsUtils.calculateWeightedPatternFrequency(patternsMap);
		double[] dispersions = StatisticsUtils.calculateFeatureDispersion(patternSet, featureAverages);
		double[] standardDeviations = StatisticsUtils.calculateStandardDeviation(dispersions);
		double[][] autoScaled = StatisticsUtils.autoScaleFeatures(patternSet, featureAverages, dispersions);
		double averageSquareDeviation = StatisticsUtils.calculateAverageSquareDeviation(dispersions);
		double covariance = StatisticsUtils.calculateCovariance(patternSet, 0, 1, featureAverages[0], featureAverages[1]);
		double correlationCoefficient = StatisticsUtils.calculateCorrelationCoefficient(covariance, dispersions[0], dispersions[1]);

		System.out.println("\nWeighted Averages: " + java.util.Arrays.toString(weightedAverages));

		System.out.println("\nPatterns Frequency: " + java.util.Arrays.toString(patternFrequency));

		System.out.println("\nFeature Dispersions: " + java.util.Arrays.toString(dispersions));

		System.out.println("\nStandard Deviations: " + java.util.Arrays.toString(standardDeviations));

		System.out.println("\nAverage Square Deviation: " + averageSquareDeviation);

		System.out.println("\nCovariance between feature 0 and 1: " + covariance);

		System.out.println("\nCorrelation Coefficient between feature 0 and 1: " + correlationCoefficient);

		System.out.println("\nAuto-Scaled Features:");
		for (double[] row : autoScaled) {
			System.out.println(java.util.Arrays.toString(row));
		}
	}
}
