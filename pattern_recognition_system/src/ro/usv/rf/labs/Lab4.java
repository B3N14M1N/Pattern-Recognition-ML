package ro.usv.rf.labs;

import ro.usv.rf.classifiers.Classifier_1NN;
import ro.usv.rf.learningsets.SupervisedLearningSet;
import ro.usv.rf.learningsets.UnsupervisedLearningSet;
import ro.usv.rf.utils.DistanceUtils;

import java.util.Arrays;

import static ro.usv.rf.classifiers.Classifier_1NN.classifyAndDisplayResult;

public class Lab4 {

	public static void demoUnsupervisedLearningSet() {

		UnsupervisedLearningSet setU = new UnsupervisedLearningSet("file.txt");
		System.out.println(setU);
		//System.out.println(setU.getF().length);
	}

	public static void demoSupervisedLearningSet() {

		double[][] x = new double[][]{
				{1, 5}, {5, 2}, {3, 5}, {3, 3.5}};
		SupervisedLearningSet setSuperv1 = new SupervisedLearningSet(x, new int[]{1, 2, 1, 2});
		System.out.println("Set 1 - X[][] is provided and iClass[]:\n" + setSuperv1);

		SupervisedLearningSet setSuperv2 = new SupervisedLearningSet("file.txt", null);
		System.out.println("Set 2 - file.txt with numeric class:\n" + setSuperv2);

		String[] numeClase = new String[]{"", "A", "B", "C"};
		SupervisedLearningSet setSuperv3 = new SupervisedLearningSet("testexam_numeric.txt", numeClase);
		System.out.println("Set 3 - testexam_numeric.txt + classNames[M+1]:\n" + setSuperv3);

	}

	public static void demoClassifier_1NN() {
		// for Problem 3.1.
		String[] numeClase = new String[]{"", "A", "B", "C"};
		SupervisedLearningSet setSuperv3 = new SupervisedLearningSet("testexam_numeric.txt", numeClase);
		Classifier_1NN nnClassifier_1nn = new Classifier_1NN(DistanceUtils::distManhattan);
		setSuperv3.getF()[3] = 2; // to prove ambiguities solution
		nnClassifier_1nn.train(setSuperv3);
		System.out.println("indiceClasa (cf. dist CityBlock)=" + nnClassifier_1nn.predict(new double[]{2, 4}));

		nnClassifier_1nn = new Classifier_1NN(); //Euclidian Distance
		nnClassifier_1nn.train(setSuperv3);
		System.out.println("indiceClasa (cf. dist. Euclidiene)=" + nnClassifier_1nn.predict(new double[]{2, 4}));

		// for Problem 3.2.
		nnClassifier_1nn.setDebug(false);
		double[][] testSet = new double[][]{{2, 4}, {4, 2}, {10, 5}, {5, 5}};
		classifyAndDisplayResult(nnClassifier_1nn, setSuperv3.getClassNames(), testSet);
		classifyAndDisplayResult(nnClassifier_1nn, setSuperv3.getClassNames(), setSuperv3.getX());

		// to check RunTime exceptions:
		//classif_1NN_CityBlock.train((double[][])null);   //no data set provided
		//classif_1NN_CityBlock.train(new double[][] {});  //no patterns in data set
		//classif_1NN_CityBlock.train(new double[1][0]);   //patterns with no features
		//classif_1NN_CityBlock.train(new double[1][1]);   //no supervised learning set (M=0)

	}

	public static void demoPredict_1NN(){
		String[] classNames = {"", "A", "B", "C"};
		SupervisedLearningSet set = new SupervisedLearningSet("testexam_numeric.txt", classNames);
		System.out.println("Loaded supervised learning set:\n" + set);

		Classifier_1NN classifier = new Classifier_1NN();
		classifier.train(set);
		double[][] testPatterns = {{2, 4}, {4, 2}, {10, 5}, {5, 5}};
		System.out.println("Predictions: ");
		for (double[] pattern : testPatterns) {
			System.out.println("Pattern: " + Arrays.toString(pattern) + " -> Class: " + set.getClassNames()[classifier.predict(pattern)]);
		}
	}
	public static void main(String[] args) {
		String separatorLine = "-------------------------------------------------------------";
		// demo existing code
		demoUnsupervisedLearningSet();
		System.out.println(separatorLine);
		demoSupervisedLearningSet();
		System.out.println(separatorLine);
		demoClassifier_1NN();
		System.out.println(separatorLine);
		demoPredict_1NN();
	}

}
