package ro.usv.rf.labs;

import ro.usv.rf.utils.DataUtils;
import ro.usv.rf.utils.FileUtils1;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class Lab1 {
	private static final String INPUT_FILE_DIRECTORY = "input/";
	private static final String OUTPUT_FILE_DIRECTORY = "output/";

	public static void main(String[] args) {
		String inputFile = INPUT_FILE_DIRECTORY + "in.txt";
		String outputFile = OUTPUT_FILE_DIRECTORY + "out.csv";

		// Read pattern set from file
		var patternSet = FileUtils1.readMatrixFromFileStream(inputFile);
		System.out.println("\nOriginal Pattern Set:");
		DataUtils.printMatrix(patternSet);

		// Normalize the pattern set
		var normalizedPatternSet = DataUtils.normalizeLearningSet(patternSet);
		System.out.println("\nNormalized Pattern Set:");
		DataUtils.printMatrix(normalizedPatternSet);

		// Write normalized pattern set to file
		FileUtils1.writePatternSetToFile(outputFile, normalizedPatternSet);
		System.out.println("\nNormalized data written to \"" + outputFile + "\"");
	}
}