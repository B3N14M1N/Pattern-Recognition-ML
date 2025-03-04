package ro.usv.rf.utils;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class DistanceUtils {
	public static double distEuclid(double[] x, double[] y) {
		if (x.length != y.length) throw new SpatiiDeDimensiuniDiferite(
				"(" + x.length + ", " + y.length + ")");

		double d = 0;
		for (int j = 0; j < x.length; j++)
			d += (x[j] - y[j]) * (x[j] - y[j]);

		return Math.sqrt(d);
	}

	public static double distManhattan(double[] x, double[] y) {
		if (x.length != y.length) throw new SpatiiDeDimensiuniDiferite(
				"(" + x.length + ", " + y.length + ")");

		double d = 0;
		for (int j = 0; j < x.length; j++)
			d += Math.abs(x[j] - y[j]);

		return d;
	}

	public static double distChebyshev(double[] x, double[] y) {
		if (x.length != y.length) throw new SpatiiDeDimensiuniDiferite(
				"(" + x.length + ", " + y.length + ")");

		double d = 0;
		for (int j = 0; j < x.length; j++) {
			var dist = Math.abs(x[j] - y[j]);
			if (dist > d)
				d = dist;
		}
		return d;
	}
}

class SpatiiDeDimensiuniDiferite extends RuntimeException {
	public SpatiiDeDimensiuniDiferite(String message) {
		super(message);
	}
}