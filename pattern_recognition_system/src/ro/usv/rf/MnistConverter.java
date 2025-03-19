package ro.usv.rf;

import ro.usv.rf.learningsets.SupervisedLearningSet;

import java.io.*;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 * Utility class to convert MNIST data to SupervisedLearningSet format
 */

public class MnistConverter {

	/**
	 * Converts MNIST dataset to a custom file format and saves it
	 */
	public static void convertMnistToCustomFormat(String imagesFile,
	                                              String labelsFile,
	                                              String outputFile,
	                                              int maxSamples) throws IOException {

		// Load the MNIST data
		DataInputStream imagesStream = new DataInputStream(
				new BufferedInputStream(new FileInputStream(imagesFile)));

		int magicNumber = imagesStream.readInt();
		if (magicNumber != 2051) {
			throw new IOException("Invalid magic number in image file: " + magicNumber);
		}

		int numImages = imagesStream.readInt();
		int numRows = imagesStream.readInt();
		int numCols = imagesStream.readInt();

		// Read labels
		DataInputStream labelsStream = new DataInputStream(
				new BufferedInputStream(new FileInputStream(labelsFile)));

		magicNumber = labelsStream.readInt();
		if (magicNumber != 2049) {
			throw new IOException("Invalid magic number in label file: " + magicNumber);
		}

		int numLabels = labelsStream.readInt();

		if (numImages != numLabels) {
			throw new IOException("Number of images does not match number of labels");
		}

		// Limit number of samples to read
		int samplesToRead = Math.min(maxSamples, numImages);

		// Save data in custom format
		try (DataOutputStream dos = new DataOutputStream(
				new BufferedOutputStream(new FileOutputStream(outputFile)))) {

			// Write header information
			dos.writeInt(samplesToRead);  // Number of samples
			dos.writeInt(numRows);        // Image height
			dos.writeInt(numCols);        // Image width

			// Read and write data
			byte[] pixelBuffer = new byte[numRows * numCols];

			for (int i = 0; i < samplesToRead; i++) {
				// Read image
				imagesStream.readFully(pixelBuffer);

				// Write pixel data directly
				dos.write(pixelBuffer);

				// Read and write label
				int label = labelsStream.readByte() & 0xFF;
				dos.writeInt(label + 1);  // Convert to 1-indexed
			}
		}

		imagesStream.close();
		labelsStream.close();

		System.out.println("Converted " + samplesToRead + " samples to " + outputFile);
	}

	/**
	 * Load data from our custom format into a SupervisedLearningSet
	 */
	public static SupervisedLearningSet loadFromCustomFormat(String filename) throws IOException {
		try (DataInputStream dis = new DataInputStream(
				new BufferedInputStream(new FileInputStream(filename)))) {

			// Read header information
			int numSamples = dis.readInt();
			int numRows = dis.readInt();
			int numCols = dis.readInt();
			int featureCount = numRows * numCols;

			// Prepare data structures
			double[][] X = new double[numSamples][featureCount];
			int[] iClass = new int[numSamples];

			// Create class names
			String[] classNames = new String[11]; // 0-9 digits + 1 (for 1-indexed)
			for (int i = 0; i <= 10; i++) {
				classNames[i] = String.valueOf(i);
			}

			// Read data
			byte[] pixelBuffer = new byte[featureCount];

			for (int i = 0; i < numSamples; i++) {
				// Read image data
				dis.readFully(pixelBuffer);

				// Convert to doubles between 0 and 1
				for (int j = 0; j < featureCount; j++) {
					X[i][j] = (pixelBuffer[j] & 0xFF) / 255.0;
				}

				// Read label
				iClass[i] = dis.readInt();
			}

			return new SupervisedLearningSet(X, iClass, classNames);
		}
	}

	/**
	 * Main method for direct conversion
	 */
	public static void main(String[] args) {
		try {
			// Convert training set
			convertMnistToCustomFormat(
					"train-images.idx3-ubyte",
					"train-labels.idx1-ubyte",
					"mnist_train.dat",
					60000); // Use all 60,000 training images

			// Convert test set
			convertMnistToCustomFormat(
					"t10k-images.idx3-ubyte",
					"t10k-labels.idx1-ubyte",
					"mnist_test.dat",
					10000); // Use all 10,000 test images

			// Test loading
			SupervisedLearningSet trainingSet = loadFromCustomFormat("mnist_train.dat");
			System.out.println("Successfully loaded " + trainingSet.getX().length + " training samples");

			SupervisedLearningSet testSet = loadFromCustomFormat("mnist_test.dat");
			System.out.println("Successfully loaded " + testSet.getX().length + " test samples");

		} catch (IOException e) {
			System.err.println("Error during conversion: " + e.getMessage());
			e.printStackTrace();
		}
	}
}