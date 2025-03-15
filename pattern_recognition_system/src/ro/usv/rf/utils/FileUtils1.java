package ro.usv.rf.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class FileUtils1 {
	private static final String INPUT_FILE_SEPARATOR = "[ \t]";
	private static final String OUTPUT_FILE_VALUES_SEPARATOR = ",";

	public static double[][] readMatrixFromFileStream(String fileName) {
		var dataLists = readMatrixFromFileStream(fileName, Double::parseDouble, INPUT_FILE_SEPARATOR);
		return DataUtils.convertToBiDimensionalArray(dataLists);
	}

	public static double[][] readMatrixFromFileStream(String fileName, String separator) {
		var dataLists = readMatrixFromFileStream(fileName, Double::parseDouble, separator);
		return DataUtils.convertToBiDimensionalArray(dataLists);
	}

	public static <T> List<ArrayList<T>> readMatrixFromFileStream(String fileName, Function<String, T> parser, String separator) {
		List<ArrayList<T>> matrixList = new ArrayList<>();

		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			matrixList = stream
					.map(line -> Stream.of(line.trim().split(separator))
							.filter(data -> !data.isEmpty())
							.map(parser)
							.collect(Collectors.toCollection(ArrayList::new)))
					.filter(data -> !data.isEmpty())
					.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return matrixList;
	}

	public static void writePatternSetToFile(String fileName, double[][] patternMatrix) {
		writePatternSetToFile(fileName, patternMatrix, OUTPUT_FILE_VALUES_SEPARATOR);
	}

	public static void writePatternSetToFile(String fileName, double[][] patternMatrix, String separator) {
		var stringBuilder = new StringBuilder();

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
