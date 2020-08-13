package mining;
import java.io.Serializable;
import java.util.Iterator;

import java.util.Set;
import java.util.TreeSet;

import data.Data;


/**
 * ClusterSet è una classe che rappresenta un insieme di cluster(determinati da QT).

 * @author pa_pe
 * 
 */
public class ClusterSet implements Iterable<Cluster>, Serializable{
	
	/**
	 * Attributo che rappresenta un Set di Cluster e rappresenta il ClusterSet.
	 */
	private Set<Cluster> C= new TreeSet<Cluster>();
	
	/**
	 * Costruttore di default della classe ClusterSet.
	 */
	ClusterSet() {}
	
	/**
	 * Questo metodo invoca il metodo add di Set.
	 * 
	 * @param c : cluster da aggiungere al Set che contiene oggetti di tipo Cluster.
	 */
	public void add(Cluster c) {
		C.add(c);
	}
	
	/** Metodo toString. Il metodo si occupa di restituire una stringa formata dalla
	 * concatenzione delle diverse stringhe, ottenute richiamando il metodo
	 * {@link Cluster#toString()} su ciascun Cluster appartenente al vettore
	 * {@link #C}.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String s="";
		Iterator<Cluster> it=C.iterator();
		int i=0;
		while(it.hasNext()) {
			Cluster a=it.next();
			s+=i+":Centroid="+a.getCentroid() +"\n";
			i++;
		}
		
		return s;
	}
	
	/**
	 * Metodo toString. Il metodo si occupa di restituire una stringa formata dalla
	 * concatenzione delle diverse stringhe, ottenute richiamando il metodo
	 * {@link Cluster#toString(Data)} su ciascun Cluster appartenente al vettore
	 * {@link #C}.
	 * 
	 * @param data : la tabella rappresentata da {@link data.Data}.
	 */
	public String toString(Data data) {
		String str="";
		Iterator<Cluster> it=C.iterator();
		int i=0;
		while(it.hasNext()){
			Cluster a=it.next(); 
			str += i + 1 + ":";
			str+=a.toString(data)+"\n"; 
			i++;
			str+="\n";
		}
		return str;
	}
	
	/** 
	 * Restituisce un iteratore per il ClusterSet.
	 *  
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Cluster> iterator(){
		return C.iterator();
	}
	
}
	
	
	