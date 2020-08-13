package mining;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import data.Data; 
import data.Tuple;

/**
 * La classe QTMiner include l'implementazione dell'algoritmo QT e si occupa di effettuare la scoperta
 * dei cluster. La classe implementa l'interfaccia {@link java.io.Serializable} in modo
 * da permettere il salvataggio su file degli oggetti che essa manipola (ovviamente tutte le
 * classi utilizzate implementano l'interfaccia).
 * 
 * @author pa_pe
 */
public class QTMiner {

	/**
	 * L'attributo rappresenta l'insieme dei cluster che saranno risultato
	 * dell'attività di scoperta oppure saranno il risultato di una lettura
	 * di tale attività da file.
	 */
	private ClusterSet C;
	private double radius;

	/**
	 * Il costruttore si occupa di inizializzare il ClusterSet in base
	 * al radius passato in input.
	 * 
	 * @param radius : elemento che influenzerà il numero di Clusters presenti nel ClusterSet.
	 * @see Cluster 
	 * @see ClusterSet
	 */
	public QTMiner(double radius) {
		C=new ClusterSet();
		this.radius=radius;
	}
	
	/**
	 * Metodo per la de-serializzazione di C. Apre il file identificato da fileName, legge l'oggetto ivi memorizzato e lo assegna a C.
	 * 
	 * @param fileName : percorso + nome file.
	 * @throws FileNotFoundException : se il file con il fileName specificato non viene trovato.
	 * @throws IOException : se falliscono o sono interrotte operazioni di I/O.
	 * @throws ClassNotFoundException : se la classe ClusterSet nel cast non viene trovata.
	 */
	public QTMiner(String fileName) throws FileNotFoundException, IOException, ClassNotFoundException {
		FileInputStream in=new FileInputStream(fileName);
		ObjectInputStream s=new ObjectInputStream(in);
		C=(ClusterSet)s.readObject();
		s.close();
	}
	
	/**
	 * Metodo per la serializzazione di C. Apre il file identificato da fileName e salva l'oggetto riferito da C in tale file.
	 * 
	 * @param fileName : percorso + nome file.
	 * @throws FileNotFoundException : se il file con il fileName specificato non viene trovato.
	 * @throws IOException : se falliscono o sono interrotte operazioni di I/O.
	 */
	public void salva(String fileName) throws FileNotFoundException, IOException {
		FileOutputStream out=new FileOutputStream(fileName);
		ObjectOutputStream s=new ObjectOutputStream(out);
		s.writeObject(C);
		s.close();
	}

	/**
	 * Restituisce il ClusterSet.
	 * 
	 * @return C
	 */
	public ClusterSet getC() {
		return C;
	}
	
	/**
	 * Metodo toString che richiama il metodo {@link ClusterSet#toString()} del ClusterSet C.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return C.toString();
	}
	
	/**
	 * Metodo toString che richiama il metodo {@link ClusterSet#toString(Data)} del ClusterSet C.
	 * 
	 * @param data : la tabella rappresentata da {@link data.Data}.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(Data data) {
		return C.toString(data);
	}

	/**
	 * Il metodo esegue l'algoritmo eseguendo i seguenti passi:<br>
	 * 1. Scelta casuale di centroidi per k clusters assegnandoli al ClusterSet.<br>
	 * 2. Assegnazione di ciascuna riga della matrice in data al cluster avente 
	 * 	  centroide più vicino all'esempio.
	 * 3. Calcolo dei nuovi centroidi per ciascun cluster.<br>
	 * 4. Ripete i passi 2 e 3 finché due iterazioni consecuitive non restituiscono 
	 * 	  centroidi uguali.<br>
	 * 
	 * @param data : la tabella {@link data.Data} che costituisce l'insieme di dati
	 * 				su cui eseguire l'algoritmo.
	 * @return numero di cluster scoperti.
	 * @throws ClusteringRadiusException : se le tuple sono tutte raggruppate in un unico cluster.
	 */
	public int compute(Data data) throws ClusteringRadiusException  {
		int numClusters=0;
		boolean isClustered[]=new boolean[data.getNumberOfExamples()];
		for(int i=0;i<isClustered.length;i++) {
			isClustered[i]=false;
		}

		int countClustered=0;
		while(countClustered!=data.getNumberOfExamples())
		{
			Cluster c=buildCandidateCluster(data, isClustered); 
			C.add(c);
			numClusters++;

			Iterator<Integer> clusteredTupleId=c.iterator(); 
			while(clusteredTupleId.hasNext()){ 
				isClustered[clusteredTupleId.next()]=true; 
			}
			countClustered+=c.getSize(); 
		}
		if(numClusters==1) throw new ClusteringRadiusException();
		else
			return numClusters;
	}

	/**
	 * Questo metodo costruisce un cluster per ciascuna tupla di data non ancora clusterizzata in un cluster di C.
	 * 
	 * @param data : la tabella {@link data.Data} che costituisce l'insieme di dati
	 * 				su cui eseguire l'algoritmo..
	 * @param isClustered : informazione booleana sullo stato di clusterizzazione di una tupla.
	 * @return il cluster candidato più popoloso.
	 */
	Cluster buildCandidateCluster(Data data, boolean isClustered[]) {
		Cluster candidate=null;
		int size;
		int max_size=0;

		for(int i=0;i<data.getNumberOfExamples();i++) {
			size=0;
			if(isClustered[i]==false) {
				Tuple a=data.getItemSet(i);
				Cluster c=new Cluster(a);
				for(int j=0;j<data.getNumberOfExamples();j++) {
					if(isClustered[j]==false) {
						Tuple b=data.getItemSet(j);
						if(a.getDistance(b)<=radius) {
							c.addData(j);
							size++;
						}
					}
				}
				if(size>max_size) {
					max_size=size;
					candidate=c;
				}
			}

		}
		return candidate;
	}
}
