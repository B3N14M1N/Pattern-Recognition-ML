package ro.usv.rf.classifiers;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * @author Beniamin Cioban
 * @grupa 3143A
 */
public class Classifier_KNNWithKDTree extends AbstractClassifier {
	private int k;
	private boolean debug = false;
	private KDTree kdTree;

	public Classifier_KNNWithKDTree(int k) {
		super();
		this.k = k;
	}

	@Override
	public void training() {
		if (M == 0)
			throw new RuntimeException("train(): No supervised learning set provided (M=0)");

		// Build KD tree with training data
		kdTree = new KDTree(X, iClass, p);
	}

	@Override
	public int predict(double[] z) {
		if (kdTree == null)
			throw new RuntimeException("predict(): KD tree not built. Call training() first");

		// Find k nearest neighbors using KD tree
		PriorityQueue<NodeDistancePair> nearestNeighbors = new PriorityQueue<>(k,
				Comparator.comparingDouble(pair -> pair.distance));

		kdTree.findKNearestNeighbors(kdTree.root, z, nearestNeighbors, k);

		// Count class frequencies among k nearest neighbors
		int[] classCount = new int[M + 1]; // +1 because class indices start from 1

		for (NodeDistancePair pair : nearestNeighbors) {
			classCount[pair.classIndex]++;

			if (debug) {
				System.out.println("Neighbor: class " + pair.classIndex +
						" (distance: " + pair.distance + ")");
			}
		}

		// Find most frequent class
		int predictedClass = 0;
		int maxCount = -1;

		for (int i = 1; i <= M; i++) {
			if (classCount[i] > maxCount) {
				maxCount = classCount[i];
				predictedClass = i;
			}
		}

		if (debug) {
			System.out.println("Predicted class: " + predictedClass);
		}

		return predictedClass;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * Helper class to store node data and distance pairs
	 */
	private static class NodeDistancePair {
		double distance;
		int classIndex;

		public NodeDistancePair(double distance, int classIndex) {
			this.distance = distance;
			this.classIndex = classIndex;
		}
	}

	/**
	 * KD-Tree implementation for efficient nearest neighbor searches
	 */
	private class KDTree {
		Node root;
		final int dimensions;

		public KDTree(double[][] points, int[] classIndices, int dimensions) {
			this.dimensions = dimensions;
			// Create array of indices to build tree
			Integer[] indices = new Integer[points.length];
			for (int i = 0; i < indices.length; i++) {
				indices[i] = i;
			}

			// Build KD tree recursively
			root = buildTree(points, classIndices, indices, 0, indices.length - 1, 0);
		}

		/**
		 * Recursively build KD tree
		 */
		private Node buildTree(double[][] points, int[] classIndices, Integer[] indices,
		                       int start, int end, int depth) {
			if (start > end) {
				return null;
			}

			// Current axis based on depth
			int axis = depth % dimensions;

			// Sort indices based on the current axis value
			Arrays.sort(indices, start, end + 1, (a, b) ->
					Double.compare(points[a][axis], points[b][axis]));

			// Get median index
			int medianIndex = start + (end - start) / 2;

			// Create node with median point
			int originalIndex = indices[medianIndex];
			Node node = new Node(points[originalIndex], classIndices[originalIndex], axis);

			// Recursively build subtrees
			node.left = buildTree(points, classIndices, indices, start, medianIndex - 1, depth + 1);
			node.right = buildTree(points, classIndices, indices, medianIndex + 1, end, depth + 1);

			return node;
		}

		/**
		 * Find k nearest neighbors to target point
		 */
		public void findKNearestNeighbors(Node node, double[] target,
		                                  PriorityQueue<NodeDistancePair> nearestNeighbors, int k) {
			if (node == null) {
				return;
			}

			// Calculate distance to current node
			double distance = calculateEuclideanDistance(target, node.point);

			// Skip if this is the same point as target (for self-testing)
			if (distance > 1e-10) {
				// Add to priority queue if queue isn't full yet or if this point is closer
				// than the farthest point in the queue
				if (nearestNeighbors.size() < k) {
					nearestNeighbors.offer(new NodeDistancePair(distance, node.classIndex));
				} else {
					assert nearestNeighbors.peek() != null;
					if (distance < nearestNeighbors.peek().distance) {
						nearestNeighbors.poll(); // Remove farthest
						nearestNeighbors.offer(new NodeDistancePair(distance, node.classIndex));
					}
				}
			}

			// Determine which subtree to search first based on target's position
			// relative to the splitting axis
			int axis = node.axis;
			Node firstBranch = target[axis] < node.point[axis] ? node.left : node.right;
			Node secondBranch = target[axis] < node.point[axis] ? node.right : node.left;

			// Search first branch
			findKNearestNeighbors(firstBranch, target, nearestNeighbors, k);

			// If the distance to the splitting plane is less than the current kth best distance,
			// we also need to search the other branch
			double axisDistance = Math.abs(target[axis] - node.point[axis]);

			if (nearestNeighbors.size() < k || axisDistance < Objects.requireNonNull(nearestNeighbors.peek()).distance) {
				findKNearestNeighbors(secondBranch, target, nearestNeighbors, k);
			}
		}

		/**
		 * Node class for KD tree
		 */
		private class Node {
			double[] point;
			int classIndex;
			int axis;
			Node left;
			Node right;

			public Node(double[] point, int classIndex, int axis) {
				this.point = point;
				this.classIndex = classIndex;
				this.axis = axis;
			}
		}
	}

	/**
	 * Calculate Euclidean distance between two points
	 */
	private double calculateEuclideanDistance(double[] a, double[] b) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			double diff = a[i] - b[i];
			sum += diff * diff;
		}
		return Math.sqrt(sum);
	}
}