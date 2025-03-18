package ro.usv.rf.classifiers;

import java.util.Arrays;
import java.util.Random;

public class Classifier_KNN extends AbstractClassifier {


	int k;
	private boolean debug = true;

	public Classifier_KNN(int k) {
		super();
		this.k = k;
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
		//d.calculateDistance(z, X[0]);
		return new Random().nextInt(M) + 1; // :-) (to be replaced!)

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
