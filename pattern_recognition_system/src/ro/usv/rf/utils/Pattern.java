package ro.usv.rf.utils;

import java.util.Arrays;

public class Pattern {
	private double[] patternValues;
	private int Iclass;

	public Pattern(double[] patternValues) {
		super();
		this.patternValues = patternValues;
		this.Iclass = 0;
	}

	public Pattern(double[] patternValues, int iClass) {
		super();
		this.Iclass = iClass;
		this.patternValues = patternValues;
	}

	public double[] getPatternValues() {
		return patternValues;
	}

	public void setPatternValues(double[] patternValues) {
		this.patternValues = patternValues;
	}

	@Override
	public String toString() {
		return "Pattern [patternValues=" + Arrays.toString(patternValues) + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Iclass;
		result = prime * result + Arrays.hashCode(patternValues);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pattern other = (Pattern) obj;
		if (Iclass != other.Iclass)
			return false;
		return Arrays.equals(patternValues, other.patternValues);
	}
}
