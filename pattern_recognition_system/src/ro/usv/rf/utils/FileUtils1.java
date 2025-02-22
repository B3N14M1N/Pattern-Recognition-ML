package ro.usv.rf.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class FileUtils1 {
	private static final String INPUT_FILE_SEPARATOR = "[ \t]";

	public static double[][] readMatrixFromFileStream(String fileName) {
		List<ArrayList<Double>> matrixList = new ArrayList<>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			matrixList = stream.map(line -> Stream.of(line.trim().split(INPUT_FILE_SEPARATOR))
							.map(Double::parseDouble)
							.collect(Collectors.toCollection(ArrayList::new)))
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return DataUtils.convertToBiDimensionalArray(matrixList);
	}

	public static void writePatternSetToFile(String fileName, double[][] patternMatrix, String separator) {
		StringBuilder stringBuilder = new StringBuilder();
		for (double[] row : patternMatrix) {
			for (int i = 0; i < row.length; i++) {
				stringBuilder.append(row[i]);
				if (i < row.length - 1) {
					stringBuilder.append(separator);
				}
			}
			stringBuilder.append("\n");
		}

		try {
			Files.write(Paths.get(fileName), stringBuilder.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
