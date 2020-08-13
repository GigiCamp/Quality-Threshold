package mining;

public class ClusteringRadiusException extends Exception {
	ClusteringRadiusException(int numTuples) {
			System.out.printf("%d tuples in one cluster!",numTuples);
			System.out.println("\n");
	}
}
