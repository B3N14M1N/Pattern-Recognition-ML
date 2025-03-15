package ro.usv.rf.labs;

import ro.usv.rf.classifiers.Classifier_1NN;
import ro.usv.rf.learningsets.SupervisedLearningSet;
import ro.usv.rf.learningsets.UnsupervisedLearningSet;

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
		String[] numeClase = new String[]{"", "A", "B", "C"};
		SupervisedLearningSet setSuperv3 = new SupervisedLearningSet("testexam_numeric.txt", numeClase);


		Classifier_1NN classif_1NN_Euclid = new Classifier_1NN();  //Euclidian Distance
		classif_1NN_Euclid.train(setSuperv3);
		int cls2 = classif_1NN_Euclid.predict(new double[]{2, 4});
		System.out.println("class index  ( dist. Euclidian)=" + cls2 +
				" <" + setSuperv3.getClassNames()[cls2] + ">");


		// to check RunTime exceptions:
		//classif_1NN_CityBlock.train((double[][])null);   //no data set provided
		//classif_1NN_CityBlock.train(new double[][] {});  //no patterns in data set
		//classif_1NN_CityBlock.train(new double[1][0]);   //patterns with no features
		//classif_1NN_CityBlock.train(new double[1][1]);   //no supervised learning set (M=0)

	}

	public static void main(String[] args) {
		String separatorLine = "-------------------------------------------------------------";
		// demo existing code
		demoUnsupervisedLearningSet();
		System.out.println(separatorLine);
		demoSupervisedLearningSet();
		System.out.println(separatorLine);
		demoClassifier_1NN();
	}

}
