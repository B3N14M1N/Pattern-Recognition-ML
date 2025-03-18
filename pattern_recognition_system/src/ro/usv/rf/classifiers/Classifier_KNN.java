package ro.usv.rf.classifiers;

import ro.usv.rf.utils.DistanceUtils;
import ro.usv.rf.utils.IDistance;

import java.util.Arrays;

public class Classifier_KNN extends AbstractClassifier {
	IDistance d;
	int k;
	private boolean debug = true;

	public Classifier_KNN(int k) {
		super();
		this.k = k;
		this.d = DistanceUtils::distEuclid;
		this.debug = false;
	}
	public Classifier_KNN(int k, IDistance d) {
		super();
		this.k = k;
		this.d = d;
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
		// Early validation
		if (X == null || X.length == 0) {
			throw new RuntimeException("predict(): No training data available");
		}

		// Array to store distances and corresponding class indices
		double[][] distancesAndClasses = new double[n][2];

		// Calculate distance from z to each training instance
		for (int i = 0; i < n; i++) {
			double distance = d.distance(z, X[i]);
			distancesAndClasses[i][0] = distance;
			distancesAndClasses[i][1] = iClass[i];

			if (debug) {
				System.out.println("Distance between z and X[" + i + "] = " + distance);
			}
		}

		// Sort by distance (ascending)
		Arrays.sort(distancesAndClasses, (a, b) -> Double.compare(a[0], b[0]));

		// Count occurrences of each class in the k nearest neighbors
		int[] classCount = new int[M + 1]; // +1 because class indices start from 1

		for (int i = 0; i < k && i < n; i++) {
			int classIndex = (int) distancesAndClasses[i][1];
			classCount[classIndex]++;

			if (debug) {
				System.out.println("Neighbor " + (i + 1) + ": class " + classIndex +
						" (distance: " + distancesAndClasses[i][0] + ")");
			}
		}

		// Find the most frequent class
		int predictedClass = 0;
		int maxCount = -1;

		for (int i = 1; i <= M; i++) {
			if (classCount[i] > maxCount) {
				maxCount = classCount[i];
				predictedClass = i;
			}
		}

		if (debug) {
			System.out.println("Predicted class: " + predictedClass);
		}

		return predictedClass;
	}


	static public void classifyAndDisplayResult(AbstractClassifier classifier, String[] classNames, double[][] testSet) {
		System.out.println("\nPatterns class:" + Arrays.deepToString(testSet) + ":");
		Arrays.stream(classifier.predict(testSet))
				.mapToObj(k -> (classNames == null ? k : classNames[k]) + " ")
				.forEach(System.out::print);
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}


}
