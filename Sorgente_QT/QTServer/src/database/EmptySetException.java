package database;

public class EmptySetException extends Exception {
	EmptySetException() {
		System.out.printf("Set vuoto!");
	}
}
