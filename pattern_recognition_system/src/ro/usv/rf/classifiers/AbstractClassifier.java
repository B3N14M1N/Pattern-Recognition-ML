package ro.usv.rf.classifiers;

import ro.usv.rf.learningsets.SupervisedLearningSet;
import ro.usv.rf.learningsets.UnsupervisedLearningSet;

import java.util.Arrays;

public abstract class AbstractClassifier {
	protected int n;
	protected int p;
	protected int M;
	protected double[][] X;
	protected double[] f;
	protected int[] iClass;
	protected String[] classNames;
	protected double[][] w;

	abstract public void training();

	abstract public int predict(double[] z);

	public int[] predict(double[][] testSet) {
		return Arrays.stream(testSet)
				.mapToInt(z -> predict(z))
				.toArray();
	}

	private void train(double[][] X, double[] f, int[] iClass, String[] classNames) {
		if (X == null)
			throw new RuntimeException("train(): NO data set provided");
		this.X = X;
		n = X.length;    //number of patterns
		if (n == 0)
			throw new RuntimeException("train(): No patterns in data set (n=0)");
		p = X[0].length; //number of features (defined in Abstract Classifier)
		if (p == 0)
			throw new RuntimeException("train(): Patterns with no features in data set (p=0)");
		if (f == null) {
			f = new double[n];
			Arrays.fill(f, 1.);
		}
		this.f = f;
		// set M, the number of classes (in AbstractClassifier)
		this.iClass = iClass;
		if (iClass != null) {
			M = (int) Arrays.stream(iClass)
					.distinct()
					.count();
			if (classNames == null)
				classNames = SupervisedLearningSet.obtainClassNames(iClass);
		} else {
			M = 0;
		}
		this.classNames = classNames;
		training();   // to be overrided in sub-classes
	}

	public void train(SupervisedLearningSet supervisedSet) {
		train(supervisedSet.getX(), supervisedSet.getF(), supervisedSet.getIClass(),
				supervisedSet.getClassNames());
	}

	public void train(UnsupervisedLearningSet unsupervisedSet) {
		train(unsupervisedSet.getX(), unsupervisedSet.getF(), null, null);
	}

	// ---- for compatibility with other
	// Pattern Recognition/ Machine Learning systems ----
	//
	// supervised context
	public void train(double[][] X, int[] iClass) {
		train(X, null, iClass, null);
	}

	private void train(double[][] X, double[] f, int[] iClass) {
		train(X, f, iClass, null);
	}

	// unsupervised context
	public void train(double[][] X) {
		train(X, null, null, null);
	}

	private void train(double[][] X, double[] f) {
		train(X, f, null, null);
	}

	public double[][] getClassifierModel() {
		//return the coefficients of discriminant functions
		return w;
	}
}
