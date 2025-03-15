package ro.usv.rf.learningsets;

import ro.usv.rf.utils.FileUtils1;
import ro.usv.rf.utils.StatisticsUtils;

public class UnsupervisedLearningSet {
	protected double[][] X;  // the pattern matrix
	protected double[] f;  // the weight of each pattern
	protected int n;         // number of patterns
	protected int p;         // number of features
	protected int M;         // number of classes (=0 for unsupervised set)

	//
	public UnsupervisedLearningSet(String dataFileName) {
		X = FileUtils1.readMatrixFromFileStream(dataFileName);
		fillFieldValues(X, null);
	}

	public UnsupervisedLearningSet(double[][] X) {
		fillFieldValues(X, null);
	}

	public UnsupervisedLearningSet(double[][] X, double[] f) {
		fillFieldValues(X, f);
	}

	protected UnsupervisedLearningSet() {
	}

	protected void fillFieldValues(double[][] X, double[] f) {
		this.X = X;
		n = X == null ? 0 : X.length;
		p = X == null || X[0] == null ? 0 : X[0].length;
		// TODO check if all the patterns have the same number of features
		//            if not throw an Exception
		try {
			if (X == null)
				throw new Exception("Pattern set is null");
			for (double[] form : X) {
				if (form.length != p)
					throw new Exception("Different forms features");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (f == null) {
			f = this.calculateWeightsValues();
		}
		this.f = f;
	}

	public double[] calculateWeightsValues() {
		// TODO: replace it by counting identical pattern
		//  if a reduced set is produce update tve value of n
		var patterns = StatisticsUtils.getPatternsMapFromInitialSet(X);
		X = StatisticsUtils.getPatterns(patterns);
		n = X.length;
		f = StatisticsUtils.getPatternWeigths(patterns);
		return f;
	}

	public int getN() {
		return n;
	}

	public int getP() {
		return p;
	}

	public int getM() {
		return M;
	}

	public double[][] getX() {
		return X;
	}

	public double[] getF() {
		return f;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName()).append(": n=").append(n).append(", p=").append(p).append(", M=").append(M);
		sb.append("\nNr.crt. ,  X ,  [f]:\n");
		for (int i = 0; i < n; i++) {
			sb.append(String.format("%d.  ", i + 1));
			for (double elemCrt : X[i])
				sb.append(String.format("%5.2f  ", elemCrt));
			sb.append(String.format("   [%5.2f]", f[i])).append("\n");
		}
		return sb.toString();
	}
}
