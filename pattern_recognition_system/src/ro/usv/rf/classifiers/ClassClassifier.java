/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.usv.rf.classifiers;

public class ClassClassifier extends AbstractClassifier {
	private int n;

	@Override
	public void training() {
		w = new double[M][p + 1];
		//... TODO ....
	}

	// int predict(doble x) may be already defined in AbstractClassifier
	//
	// Returns the predicted index of the class
	// to which the pattern x might belong.
	// Predict only for linear models of classifiers
	//
	@Override
	public int predict(double[] x) {
		int kmax = -1;
		double psik, psimax = Double.NEGATIVE_INFINITY;
		for (int k = 0; k < M; k++) {
			psik = w[k][p + 1];
			for (int j = 0; j < p; j++)
				psik += x[j] * w[k][j];
			if (psik > psimax) {
				psimax = psik;
				kmax = k;
			}
		}
		return kmax;
	}


}
