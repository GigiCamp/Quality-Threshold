package data;

/**
 * EmptyDatasetException è una classe che modella un'eccezione controllata da considerare qualora il dataset sia vuoto.
 * 
 * @author pa_pe
 *
 */
public class EmptyDatasetException extends Exception {

	/**
	 * Richiama il costruttore della classe madre per creare l'oggetto eccezione
	 * corrispondente.
	 */
	EmptyDatasetException() {
		System.out.println("Errore! Zero tuple nel DataSet!");
		System.exit(0);
	}
}
