package ro.usv.rf.misc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class CreateInputFile {
	public static void main(String[] args) {
		List<String> lines = List.of(
				"1 7 -2 4.5",
				"2 3 -5 2.3",
				"1 6 -2 1.2",
				"2 5 -2 4.5",
				"1 6 -5 2.3"
		);

		try {
			Files.write(Paths.get("in.txt"), lines);
			System.out.println("Input file 'in.txt' created successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
