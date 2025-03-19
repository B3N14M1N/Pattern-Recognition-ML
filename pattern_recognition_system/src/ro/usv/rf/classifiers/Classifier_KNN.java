package ro.usv.rf.classifiers;

import ro.usv.rf.utils.DistanceUtils;
import ro.usv.rf.utils.IDistance;

import java.util.Arrays;
import java.util.PriorityQueue;

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
	/* Brute force
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
	*/
	@Override
	public int predict(double[] z) {
		// Early validation
		if (X == null || X.length == 0) {
			throw new RuntimeException("predict(): No training data available");
		}

		// Use a max heap to efficiently find k nearest neighbors
		// The PriorityQueue in Java is a min heap by default, so we invert the comparison
		// to make it a max heap (largest distance at the top)
		// the pseudocode is in course 3_2_knn.pdf
		PriorityQueue<DistanceClassPair> maxHeap = new PriorityQueue<>(k + 1,
				(a, b) -> Double.compare(b.distance, a.distance));

		// Calculate distance from z to each training instance
		for (int i = 0; i < n; i++) {
			// Skip the pattern if it's the same as the one being classified (self-testing case)
			if (isPatternEqual(z, X[i])) {
				if (debug) {
					System.out.println("Skipping X[" + i + "] as it appears to be the same pattern as z");
				}
				continue;
			}

			double distance = d.distance(z, X[i]);
			DistanceClassPair pair = new DistanceClassPair(distance, iClass[i]);

			if (debug) {
				System.out.println("Distance between z and X[" + i + "] = " + distance);
			}

			// Add to max heap
			maxHeap.offer(pair);

			// If heap size exceeds k, remove the farthest element (which is at the top of the max heap)
			if (maxHeap.size() > k) {
				maxHeap.poll();
			}
		}

		// Count occurrences of each class in the k nearest neighbors
		int[] classCount = new int[M + 1]; // +1 because class indices start from 1

		int neighborCount = 0;
		while (!maxHeap.isEmpty()) {
			DistanceClassPair pair = maxHeap.poll();
			int classIndex = pair.classIndex;
			classCount[classIndex]++;

			if (debug) {
				System.out.println("Neighbor " + (++neighborCount) + ": class " + classIndex +
						" (distance: " + pair.distance + ")");
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

	private static class DistanceClassPair {
		double distance;
		int classIndex;

		public DistanceClassPair(double distance, int classIndex) {
			this.distance = distance;
			this.classIndex = classIndex;
		}
	}

	// This is used to exclude the self test pattern from prediction
	private boolean isPatternEqual(double[] a, double[] b) {
		if (a.length != b.length) {
			return false;
		}

		for (int i = 0; i < a.length; i++) {
			// Using small epsilon for floating point comparison
			if (Math.abs(a[i] - b[i]) > 1e-10) {
				return false;
			}
		}

		return true;
	}

	static public void classifyAndDisplayResult(AbstractClassifier classifier, String[] classNames, double[][] testSet) {
		System.out.println("\nPatterns class:" + Arrays.deepToString(testSet) + ":");
		Arrays.stream(classifier.predict(testSet))
				.mapToObj(k -> (classNames == null ? k : classNames[k]) + " ")
				.forEach(System.out::print);
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public IDistance getD() {
		return d;
	}

	public void setD(IDistance d) {
		this.d = d;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
