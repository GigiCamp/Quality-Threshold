package mining;
import java.io.Serializable;
import java.util.HashSet;

import java.util.Iterator;
import java.util.Set;

import data.Data;
import data.Tuple;


/**
 * La classe si occupa di modellare un singolo Cluster.
 * Ciascun cluster è rappresentato da una tupla (Centroide) e da un insieme
 * di interi rappresentanti le righe della tabella {@link data.Data} appartenenti al
 * cluster.
 * Il centroide è modellato usando la classe {@link data.Tuple}, mentre l'insieme
 * delle tuple appartenenti al cluster è modellato usando un insieme {@link java.util.Set}.
 * 
 * @author pa_pe
 * 
 *
 */
class Cluster implements Iterable<Integer>, Comparable<Cluster>, Serializable{
	
	/**
	 *  Attributo rappresentante la tupla centroide del Cluster.
	 */
	private Tuple centroid;
	
	
	/**
	 * Insieme delle righe della tabella Data appartenenti al Cluster.
	 */
	private Set<Integer> clusteredData;
	
	/*Cluster(){
		
	}*/

	/**
	 * Questo costruttore inizializza gli attributi centroid, con il parametro in input, e clusteredData.
	 * 
	 * @param centroid : tupla rappresentante il centroide del Cluster.
	 */
	Cluster(Tuple centroid){
		this.centroid=centroid;
		clusteredData=new HashSet<Integer>();
	}
		
	/**
	 * Restituisce il centroide per questo cluster.
	 */
	Tuple getCentroid(){
		return centroid;
	}
	
	/**
	 * Aggiunge un elemento all'insieme {@link #clusteredData}.
	 * L'aggiunta avviene solo nel caso in cui tale elemento identificato da id,
	 * non + presente nell'insieme.
	 * Ciò è vero solo nel caso in cui la tupla identificata da id ha cambiato cluster.
	 * Quindi l'aggiunta all'insieme {@link #clusteredData} si verifica solo in questo caso.
	 * 
	 * @param id : dentificativo rappresentante la riga della tabella {@link data.Data}
	 * 			 appertenente al cluster.
	 */
	boolean addData(int id){
		return clusteredData.add(id);
		
	}
	
	/**
	 * Il metodo verifica verifica se una transazione è clusterizzata 
	 * 
	 * Restituisce vero se la transazione è stata già clusterizzata, falso altrimenti.
	 * 
	 * @param id : identificativo della tupla da ricercare.
	 */
	boolean contain(int id){
		return clusteredData.contains(id);
	}
	
	/**
	 * Il metodo elimina la tupla identificata da id dall'insieme {@link #clusteredData} nel
	 * momento in cui questa cambia cluster.
	 * 
	 * @param id : identificativo della tupla da rimuovere.
	 */
	void removeTuple(int id){
		clusteredData.remove(id);
		
	}
	
	/**
	 * Ritorna la dimensione dell'insieme contenente le righe della tabella {@link data.Data}
	 * appartenenti a questo Cluster.
	 */
	int  getSize(){
		return clusteredData.size();
	}
	
	/** Metodo toString. Il metodo si occupa di creare (e ritornare) una stringa contenente il centroide 
	 * di questo cluster.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i);
		str+=")";
		return str;
		
	}
	
	/**
	 * Il metodo si occupa di creare una stringa contenente il centroide di questo cluster.
	 * E per questo cluster si occupa inoltre di mostrare tutte le tuple in esso clusterizzate e
	 * la distanza massima.
	 * 
	 * @param data : insieme delle transazioni(o tuple) della tabella.
	 * @return una rappresentazione visiva delle tuple contenute nel cluster, con il rispettivo centroide e la distanza media tra le tuple.
	 */
	public String toString(Data data){
		String str="Centroid=(";
		for(int i=0;i<centroid.getLength();i++)
			str+=centroid.get(i)+ " ";
		str+=")\nExamples:\n";
		Set<Integer> array=clusteredData;
		Iterator<Integer> it=array.iterator();
		while(it.hasNext()){
			Integer a=it.next();
			str+="[";
			for(int j=0;j<data.getNumberOfExplanatoryAttributes();j++)
				str+=data.getValue(j)+" ";
			str+="] dist="+getCentroid().getDistance(data.getItemSet(a))+"\n";
			
		}
		str+="\nAvgDistance="+getCentroid().avgDistance(data, array);
		return str;
	}

	/** 
	 * Questo metodo si occupa di confrontare la dimensione del cluter corrente con la dimensione del clister specificato dal parametro di input.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Cluster P) {
		if(this.getSize()>P.getSize()){
			return 1;
		}else {
			return -1;
		}
	}
	
	/** 
	 * Questo metodo restituisce un iteratore per l'insieme {@link #clusteredData}.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<Integer> iterator(){
		return clusteredData.iterator();
	}
}
