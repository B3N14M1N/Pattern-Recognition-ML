package ro.usv.rf.classifiers;

import ro.usv.rf.utils.DistanceUtils;
import ro.usv.rf.utils.IDistance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Classifier_1NN extends AbstractClassifier {

	IDistance d;
	private boolean debug = true;

	public Classifier_1NN(IDistance d) {
		super();
		this.d = d;
	}

	public Classifier_1NN() {
		this(DistanceUtils::distEuclid);
	}

	@Override
	public void training() {
		if (M == 0)
			throw new RuntimeException("train(): No supervised learning set provided (M=0)");
		// all rest were done in super.train(X,F,iClass)
	}

	//TODO: implement predict method
	@Override
	public int predict(double[] z) {
		if (X == null || X.length == 0)
			throw new RuntimeException("predict(): No training data available");

		// Keep track of minimum distance and corresponding class
		double minDistance = Double.MAX_VALUE;
		int predictedClass = -1;

		// List to track all patterns at minimum distance (for ambiguity resolution)
		List<Integer> minDistancePatterns = new ArrayList<>();

		if (debug) {
			System.out.println("Distanta de la z=" + Arrays.toString(z) + " la:");
		}

		// Find patterns with minimum distance
		for (int i = 0; i < n; i++) {
			double distance = d.distance(z, X[i]);

			if (debug) {
				System.out.printf("x[%d]=%s este d=%.2f%n", i, Arrays.toString(X[i]), distance);
			}

			if (distance < minDistance) {
				minDistance = distance;
				predictedClass = iClass[i];
				minDistancePatterns.clear();
				minDistancePatterns.add(i);
			} else if (distance == minDistance) {
				// Found another pattern at same minimum distance
				minDistancePatterns.add(i);
			}
		}

		// Resolve ambiguities if there are multiple patterns at minimum distance
		if (minDistancePatterns.size() > 1) {
			double maxWeight = -1;

			for (int patternIndex : minDistancePatterns) {
				if (f[patternIndex] > maxWeight) {
					maxWeight = f[patternIndex];
					predictedClass = iClass[patternIndex];
				}
			}
		}

		return predictedClass;
	}


	static public void classifyAndDisplayResult(AbstractClassifier classifier, String[] classNames, double[][] testSet) {
		System.out.println("\nPatterns class:" + Arrays.deepToString(testSet) + ":");
		Arrays.stream(classifier.predict(testSet))
				.mapToObj(k -> (classNames == null ? k : classNames[k]) + " ")
				.forEach(System.out::print);
		System.out.println();
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}


}
