package database;

public class NoValueException extends Exception {
	NoValueException(String msg) {
		System.out.printf(msg);
	}
}
