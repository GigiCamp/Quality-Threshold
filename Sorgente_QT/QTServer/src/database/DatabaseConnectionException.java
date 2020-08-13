package database;

public class DatabaseConnectionException extends Exception {
	DatabaseConnectionException() {
		System.out.printf("Connessione al Database fallita!");
	}
}
