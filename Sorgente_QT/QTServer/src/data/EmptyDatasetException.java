package data;

public class EmptyDatasetException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6978431767948793114L;

	EmptyDatasetException(int numTuples) {
		System.out.printf("%d tuples in Dataset!",numTuples);
		System.exit(0);
	}
}
