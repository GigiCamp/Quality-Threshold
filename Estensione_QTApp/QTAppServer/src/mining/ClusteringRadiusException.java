package mining;

/**
 * ClusteringRadiusException è una classe che modella un'eccezione controllata da considerare qualora l'algoritmo di clustering generi un solo 
 * cluster.
 * 
 * @author pa_pe
 *
 */
public class ClusteringRadiusException extends Exception {
	ClusteringRadiusException() {
		       System.out.println("Errore! E' presente un solo cluster!");
	}
}
