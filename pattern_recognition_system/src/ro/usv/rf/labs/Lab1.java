package ro.usv.rf.labs;

import ro.usv.rf.utils.*;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */

public class Lab1 {
	public static void main(String[] args) {
		String inputFile = "in.txt";
		String outputFile = "out.csv";

		// Read pattern set from file
		double[][] patternSet = FileUtils1.readMatrixFromFileStream(inputFile);
		System.out.println("Original Pattern Set:");
		DataUtils.printMatrix(patternSet);

		// Normalize the pattern set
		double[][] normalizedPatternSet = DataUtils.normalizeLearningSet(patternSet);
		System.out.println("Normalized Pattern Set:");
		DataUtils.printMatrix(normalizedPatternSet);

		// Write normalized pattern set to file
		FileUtils1.writePatternSetToFile(outputFile, normalizedPatternSet, ",");
		System.out.println("Normalized data written to " + outputFile);
	}
}