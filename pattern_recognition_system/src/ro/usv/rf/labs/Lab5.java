package ro.usv.rf.labs;

import ro.usv.rf.classifiers.Classifier_KNN;
import ro.usv.rf.learningsets.SupervisedLearningSet;
import ro.usv.rf.utils.FileUtils1;

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


		//Problem 3
		FileUtils1.setinputFileValuesSeparator(","); // by default is white spaces
		SupervisedLearningSet countyLearningSet = new SupervisedLearningSet("county_data.txt");
		//prints more than 13.000 lines
		//System.out.println("Set 6 - test_data.csv + classNames[M+1]:\n" + countyLearningSet);
		Classifier_KNN classif_kNN_Euclid = new Classifier_KNN(9); // Euclidian Distance	
		classif_kNN_Euclid.train(countyLearningSet);
		int iclass = classif_kNN_Euclid.predict(new double[]{25.89, 47.56});
		System.out.println("class index  ( dist. Euclidian)=" + iclass + " <"
				+ countyLearningSet.getClassNames()[iclass] + ">");

		// Problem 4
		for (int k = 1; k < 9; k += 2) {
			System.out.println("\n\n*************  k = " + k + "  *************");
			// build classifier for k neighbors
			Classifier_KNN kNNclassifier = new Classifier_KNN(k); // Euclidian Distance	
			// train classifier
			kNNclassifier.train(setSuperv5);
			// Self test evaluation
			kNNclassifier.evaluateAccuracy(true);   // test with other patterns
		}


	}
}
