package database;

/**
 * DatabaseConnectionException è una classe che estende Exception e modella il fallimento nella connessione al database.
 * 
 * @author pa_pe
 *
 */
public class DatabaseConnectionException extends Exception {
	
	
	/**
	 * Richiama il costruttore della classe base.
	 */
	DatabaseConnectionException() {
		System.out.println("Errore! Connessione al DB fallita!");
	}
}
