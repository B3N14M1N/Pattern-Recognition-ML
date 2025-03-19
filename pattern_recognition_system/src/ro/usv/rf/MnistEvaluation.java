package ro.usv.rf;

import ro.usv.rf.classifiers.Classifier_KNN;
import ro.usv.rf.classifiers.Classifier_KNNWithKDTree;
import ro.usv.rf.learningsets.SupervisedLearningSet;
import ro.usv.rf.utils.DistanceUtils;
import ro.usv.rf.utils.IDistance;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Partially generated with Claude Sonnet 3.7
 * @author Beniamin Cioban??
 * @grupa 3143A??
 */
public class MnistEvaluation {

	// Number of neighbors for kNN algorithm
	private static final int K = 3;

	// Sample size for training and testing (use smaller values for quicker testing)
	private static final int TRAIN_SAMPLE_SIZE = 10000; // Max 60000 for MNIST
	private static final int TEST_SAMPLE_SIZE = 1000;  // Max 10000 for MNIST

	public static void main(String[] args) {
		try {
			// Load MNIST data
			System.out.println("Loading MNIST datasets...");
			SupervisedLearningSet trainingSet = loadDataset("train-images.idx3-ubyte",
					"train-labels.idx1-ubyte",
					TRAIN_SAMPLE_SIZE);

			SupervisedLearningSet testSet = loadDataset("t10k-images.idx3-ubyte",
					"t10k-labels.idx1-ubyte",
					TEST_SAMPLE_SIZE);

			System.out.println("Training data loaded: " + trainingSet.getX().length + " samples");
			System.out.println("Test data loaded: " + testSet.getX().length + " samples");

			Classifier_KNN standardKnn = new Classifier_KNN(K);
			standardKnn.setDebug(false);
			Classifier_KNNWithKDTree kdTreeKnn = new Classifier_KNNWithKDTree(K);

			System.out.print("\n\n************* (dist. Euclidian) *************");
			TestMNIST(DistanceUtils::distEuclid, trainingSet, testSet, standardKnn, kdTreeKnn);
			System.out.print("\n\n************* (dist. Manhattan) *************");
			TestMNIST(DistanceUtils::distManhattan, trainingSet, testSet, standardKnn, kdTreeKnn);
			System.out.print("\n\n************* (dist. Chebyshev) *************");
			TestMNIST(DistanceUtils::distChebyshev, trainingSet, testSet, standardKnn, kdTreeKnn);


		} catch (IOException e) {
			System.err.println("Error loading MNIST data: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void TestMNIST(IDistance distance,
	                              SupervisedLearningSet trainingSet,
	                              SupervisedLearningSet testSet,
	                              Classifier_KNN standardKnn,
	                              Classifier_KNNWithKDTree kdTreeKnn) {
		int[] kNN = new int[]{3, 7, 9, 11, 17, 31};
		for (int k : kNN) {
			System.out.println("\n\n*************  k = " + k + "  *************");
			standardKnn.setK(k);
			kdTreeKnn.setK(k);
			standardKnn.setD(distance);
			kdTreeKnn.setD(distance);

			// Test standard kNN implementation
			System.out.println("\n--- Standard KNN (Max Heap) ---");
			// Train and measure time
			System.out.println("Training...");
			long startTime = System.currentTimeMillis();
			standardKnn.train(trainingSet);
			long endTrainTime = System.currentTimeMillis();
			System.out.println("Training time: " + (endTrainTime - startTime) + " ms");

			// Test and measure accuracy
			System.out.println("Testing...");
			startTime = System.currentTimeMillis();
			double accuracy = standardKnn.evaluateAccuracy(testSet, false);
			long endTime = System.currentTimeMillis();

			System.out.println("Testing time: " + (endTime - startTime) + " ms");
			System.out.println("Accuracy: " + String.format("%.2f%%", accuracy * 100));

			// Test KD-Tree implementation
			System.out.println("\n--- KD-Tree KNN ---");
			// Train and measure time
			System.out.println("Training...");
			startTime = System.currentTimeMillis();
			kdTreeKnn.train(trainingSet);
			endTrainTime = System.currentTimeMillis();
			System.out.println("Training time: " + (endTrainTime - startTime) + " ms");

			// Test and measure accuracy
			System.out.println("Testing...");
			startTime = System.currentTimeMillis();
			accuracy = kdTreeKnn.evaluateAccuracy(testSet, false);
			endTime = System.currentTimeMillis();

			System.out.println("Testing time: " + (endTime - startTime) + " ms");
			System.out.println("Accuracy: " + String.format("%.2f%%", accuracy * 100));
		}
	}

	/**
	 * Load MNIST data directly from IDX files into a SupervisedLearningSet
	 */
	private static SupervisedLearningSet loadDataset(String imagesFile,
	                                                 String labelsFile,
	                                                 int maxSamples) throws IOException {

		// Read images
		DataInputStream imagesStream = new DataInputStream(
				new BufferedInputStream(new FileInputStream(imagesFile)));

		int magicNumber = imagesStream.readInt();
		if (magicNumber != 2051) {
			throw new IOException("Invalid magic number in image file: " + magicNumber);
		}

		int numImages = imagesStream.readInt();
		int numRows = imagesStream.readInt();
		int numCols = imagesStream.readInt();
		int featureCount = numRows * numCols;

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

		// Prepare data structures
		double[][] X = new double[samplesToRead][featureCount];
		int[] iClass = new int[samplesToRead];

		// Create class names
		String[] classNames = new String[11]; // 0-9 digits + 1 (for 1-indexed)
		for (int i = 0; i <= 10; i++) {
			classNames[i] = String.valueOf(i);
		}

		// Read data
		byte[] pixelBuffer = new byte[featureCount];

		for (int i = 0; i < samplesToRead; i++) {
			// Read image data
			imagesStream.readFully(pixelBuffer);

			// Convert to doubles between 0 and 1
			for (int j = 0; j < featureCount; j++) {
				X[i][j] = (pixelBuffer[j] & 0xFF) / 255.0;
			}

			// Read label (add 1 to make it 1-indexed for the classifier)
			int label = labelsStream.readByte() & 0xFF;
			iClass[i] = label + 1;
		}

		imagesStream.close();
		labelsStream.close();

		return new SupervisedLearningSet(X, iClass, classNames);
	}
}