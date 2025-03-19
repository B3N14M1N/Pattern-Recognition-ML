package ro.usv.rf.labs;

import ro.usv.rf.classifiers.Classifier_KNN;
import ro.usv.rf.learningsets.SupervisedLearningSet;
import ro.usv.rf.utils.DistanceUtils;
import ro.usv.rf.utils.FileUtils1;
import ro.usv.rf.utils.IDistance;

import java.util.Arrays;

public class Lab5 {

	public static void main(String[] args) {
		FileUtils1.setinputFileValuesSeparator(); // by default is white spaces

		// Problem 1
		System.out.println("\nTraining set 5 si and test set z1, ..., z5");
		SupervisedLearningSet setSuperv5 = new SupervisedLearningSet("testexam_strings.txt");

		// Problem 2
		// Preparing test set
		SupervisedLearningSet testSetz15 = new SupervisedLearningSet("test_z1_z5.txt");
		System.out.println("Before:\n" + testSetz15);
		testSetz15.doSameClassIndexAs(setSuperv5);
		System.out.println("After:\n" + testSetz15);

		for (int k = 1; k < 9; k += 2) {
			System.out.println("\n\n*************  k = " + k + "  *************");
			// build classifier for k neighbors
			Classifier_KNN kNNclassifier = new Classifier_KNN(k); // by default, Euclidian Distance	
			// train classifier
			kNNclassifier.train(setSuperv5);
			// Evaluation with test set
			kNNclassifier.evaluateAccuracy(testSetz15, true);   // test with other patterns
		}

		// problem 3
		System.out.print("\n\n************* (dist. Euclidian) *************");
		problem3(DistanceUtils::distEuclid);
		System.out.print("\n\n************* (dist. Manhattan) *************");
		problem3(DistanceUtils::distManhattan);
		System.out.print("\n\n************* (dist. Chebyshev) *************");
		problem3(DistanceUtils::distChebyshev);

		// problem 4
		problem4(setSuperv5);
	}

	private static void problem3(IDistance distance) {
		FileUtils1.setinputFileValuesSeparator(","); // by default is white spaces
		SupervisedLearningSet countyLearningSet = new SupervisedLearningSet("county_data.txt");
		Classifier_KNN classifier_kNN = new Classifier_KNN(9, distance);
		classifier_kNN.train(countyLearningSet);
		classifier_kNN.setDebug(false);
		double[][] testSets = new double[][]{{25.89, 47.56},
				{24, 45.15},
				{25.33, 45.44}
		};
		int[] kNN = new int[]{9, 11, 17, 31};

		for (int k : kNN) {
			System.out.println("\n\n*************  k = " + k + "  *************");
			classifier_kNN.setK(k);

			for (double[] testSet : testSets) {
				int iClass = classifier_kNN.predict(testSet);
				System.out.println(Arrays.toString(Arrays.stream(testSet).toArray()) + " class index = " + iClass + " <"
						+ countyLearningSet.getClassNames()[iClass] + ">");
			}
		}
	}

	private static void problem4(SupervisedLearningSet supervisedSet){
		for (int k = 1; k < 9; k += 2) {
			System.out.println("\n\n*************  k = " + k + "  *************");
			// build classifier for k neighbors
			Classifier_KNN classifier = new Classifier_KNN(k); // Euclidian Distance
			// train classifier
			classifier.train(supervisedSet);
			classifier.setSelfTest(true);
			// Self test evaluation
			classifier.evaluateAccuracy(true);   // test with other patterns
		}
	}
}
