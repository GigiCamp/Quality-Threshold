package database;

/**
 * NoValueException è una classe che estende Exception e modella l'assenza di un valore all'interno di un resultset.
 * 
 * @author pa_pe
 *
 */
public class NoValueException extends Exception {
	NoValueException(String msg) {
		System.out.println(msg);
	}
}
