package database;

/**
 * EmptySetException è una classe che estende Exception e modella la restituzione di un resultset vuoto.
 * 
 * @author pa_pe
 *
 */
public class EmptySetException extends Exception {
	EmptySetException() {
		System.out.println("Errore! Set vuoto!");
	}
}
