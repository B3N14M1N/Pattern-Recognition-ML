package ro.usv.rf.labs;

import ro.usv.rf.misc.DistanceMatrix;
import ro.usv.rf.utils.DataUtils;
import ro.usv.rf.utils.DistanceUtils;
import ro.usv.rf.utils.FileUtils1;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class Lab3 {
	private static final String INPUT_FILE_DIRECTORY = "input/";
	private static final String OUTPUT_FILE_DIRECTORY = "output/";

	public static void main(String[] args) {
		String inputFile = INPUT_FILE_DIRECTORY + "in.txt";
		String outputFile = OUTPUT_FILE_DIRECTORY + "out.csv";

		double[][] patternSet = FileUtils1.readMatrixFromFileStream(inputFile);

		System.out.println("Manhattan distance for the pattern set:");
		DistanceMatrix distMat = new DistanceMatrix(patternSet, DistanceUtils::distManhattan);
		System.out.println(distMat);
		FileUtils1.writePatternSetToFile(outputFile, distMat.getMatDist());

		System.out.println("Distance neighbors for pattern 0:");
		DataUtils.printMatrix(distMat.neighbors(0));
	}
}
