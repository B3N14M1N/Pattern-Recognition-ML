package ro.usv.rf.misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class CreateInputFile {
	private static final Random RANDOM = new Random();
	private static final String GENERATED_FILE = "input/in.txt";

	public static void main(String[] args) {
		int rows = 5;
		int cols = 4;
		String dataType = "double"; // "int", "double", "word"
		double min = -10.0, max = 10.0;

		List<String> lines = generateMatrix(rows, cols, dataType, min, max);

		try {
			Files.write(Paths.get(GENERATED_FILE), lines);
			System.out.println("Input file \"" + GENERATED_FILE + "\" created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<String> generateMatrix(int rows, int cols, String dataType, double min, double max) {
		return IntStream.range(0, rows)
				.mapToObj(i -> IntStream.range(0, cols)
						.mapToObj(j -> generateRandomValue(dataType, min, max))
						.collect(Collectors.joining(" ")))
				.collect(Collectors.toList());
	}

	public static String generateRandomValue(String dataType, double min, double max) {
		return switch (dataType.toLowerCase()) {
			case "int" -> String.valueOf(RANDOM.nextInt((int) min, (int) max + 1));
			case "double" -> String.format("%.2f", min + (max - min) * RANDOM.nextDouble());
			case "word" -> generateRandomWord((int) max);
			default -> "0";
		};
	}

	public static String generateRandomWord(int length) {
		length = Math.abs(length);
		length = (length == 0) ? 2 : length + 1;
		return RANDOM.ints(RANDOM.nextInt(1, length), 'a', 'z' + 1)
				.mapToObj(c -> String.valueOf((char) c))
				.collect(Collectors.joining());
	}
}