package ro.usv.rf.utils;

import ro.usv.rf.Pattern;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class StatisticsUtils {

	public static double calculateFeatureAverage(double[] feature) {
		double average = 0.0;
		for (double v : feature) {
			average += v;
		}
		average = average / feature.length;
		return average;
	}

	public static Map<Pattern, Integer> getPatternsMapFromInitialSet(double[][] patternSet) {
		Map<Pattern, Integer> patternsMap = new HashMap<>();
		for (double[] set : patternSet) {
			var pattern = new Pattern(set);
			if (!patternsMap.containsKey(pattern)) {
				patternsMap.put(pattern, 0);
			}
			patternsMap.put(pattern, patternsMap.get(pattern) + 1);
		}
		return patternsMap;
	}

	public static double[] calculateWeightedAverages(Map<Pattern, Integer> patternsMap, int numberOfFeatures) {
		double[] weightedAverages = new double[numberOfFeatures];
		int totalNumberOfPatterns = patternsMap.values().stream().mapToInt(Integer::intValue).sum();

		for (var dataSet : patternsMap.keySet()) {
			for (int i = 0; i < numberOfFeatures; i++) {
				weightedAverages[i] += dataSet.getPatternValues()[i] * ((double) patternsMap.get(dataSet) /totalNumberOfPatterns);
			}
		}

		return weightedAverages;
	}

	public static double[] calculateWeightedPatternFrequency(Map<Pattern, Integer> patternsMap){
		int totalNumberOfSets = patternsMap.size();
		int totalNumberOfPatterns = patternsMap.values().stream().mapToInt(Integer::intValue).sum();
		int i = 0;
		double[] patternFrequency = new double[totalNumberOfSets];

		for (var dataSet : patternsMap.values()) {
			patternFrequency[i++] = (double) dataSet /totalNumberOfPatterns;
		}

		return patternFrequency;
	}

	public static double[] calculateFeatureDispersion(double[][] patternSet, double[] featureAverages) {
		int numFeatures = featureAverages.length;
		double[] dispersions = new double[numFeatures];
		int numPatterns = patternSet.length;

		for (int j = 0; j < numFeatures; j++) {
			double sum = 0;
			for (double[] doubles : patternSet) {
				sum += Math.pow(doubles[j] - featureAverages[j], 2);
			}
			dispersions[j] = sum / numPatterns;
		}
		return dispersions;
	}

	public static double calculateCovariance(double[][] patternSet, int featureX, int featureY, double avgX, double avgY) {
		int numPatterns = patternSet.length;
		double sum = 0;

		for (double[] doubles : patternSet) {
			sum += (doubles[featureX] - avgX) * (doubles[featureY] - avgY);
		}
		return sum / numPatterns;
	}

	public static double calculateCorrelationCoefficient(double covariance, double dispersionX, double dispersionY) {
		return covariance / Math.sqrt(dispersionX * dispersionY);
	}

	public static double calculateAverageSquareDeviation(double[] dispersions) {
		double sum = 0;
		for (double dispersion : dispersions) {
			sum += dispersion;
		}
		return Math.sqrt(sum / dispersions.length);
	}

	public static double[] calculateStandardDeviation(double[] dispersions) {
		double[] deviations = new double[dispersions.length];
		for (int i = 0; i < dispersions.length; i++) {
			deviations[i] = Math.sqrt(dispersions[i]);
		}
		return deviations;
	}

	public static double[][] autoScaleFeatures(double[][] patternSet, double[] featureAverages, double[] dispersions) {
		int numPatterns = patternSet.length;
		int numFeatures = featureAverages.length;
		double[][] scaledFeatures = new double[numPatterns][numFeatures];

		for (int i = 0; i < numPatterns; i++) {
			for (int j = 0; j < numFeatures; j++) {
				scaledFeatures[i][j] = (patternSet[i][j] - featureAverages[j]) / Math.sqrt(dispersions[j]);
			}
		}
		return scaledFeatures;
	}
}
