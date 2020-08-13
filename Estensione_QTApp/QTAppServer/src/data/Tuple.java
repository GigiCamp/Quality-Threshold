package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;

/**
 * Tuple è una classe che rappresenta una tupla come sequenza di coppie (attributo - valore).
 * 
 * @author pa_pe
 * 
 */
public class Tuple implements Serializable {
	
	/**
	 * Array che rappresenta la sequenza le coppie (attributo - valore).
	 */
	private Item [] tuple;
	
	/**
	 * Questo costruttore inizializza la tupla specificando la dimensione dell'array {@link #Tuple}.
	 * 
	 * @param size : dimensione da assegnare all'array {@link #tuple}.
	 */
	public Tuple(int size) {
		tuple=new Item[size];
	}
	
	/**
	 * Ritorna la dimensione dell'array {@link #tuple}.
	 * 
	 * @return un int rappresentante la dimensione dell'array
	 */
	public int getLength() {
		return tuple.length;
	}
	
	/**
	 * Ritorna un {@link Item} appartenente alla tupla.
	 * 
	 * @param i : parametro intero rappresentante l'indice dell'array {@link #tuple}
	 * 			rappresentante la tupla.
	 * 
	 * @return restituisce l'{@link Item} all'indice specificato.
	 */
	public Item get(int i) {
		return tuple[i];
	}
	
	/**
	 * Aggiunge l'{@link Item} specificato come parametro
	 * all'array {@link #tuple} all'indice i. 
	 * 
	 * @param c : {@link Item} da aggiungere alla tupla.
	 * @param i	: rappresenta l'indice dell'array {@link #tuple} in cui aggiungere l'Item specificato.
	 */
	public void add(Item c, int i) {
		tuple[i]=c;
	}
	
	/** 
	 * Metodo toString. Sovrascrive il metodo toString della classe Object e si occupa di 
	 * fornire una visualizzazione grafica della tupla.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String output="";
		output="(";
		for(int i=0;i<tuple.length;i++) {
			output+=tuple[i].toString();
		}
		output+=")";
		return output;
	}
	
	/**
	 * Questo metodo determina la distanza tra la tupla riferita da obj e la tupla corrente(riferita da this).
	 * La distanza è ottenuta come la somma delle distanze tra gli Item in posizioni eguali nelle due tuple.
	 * 
	 * @param obj : tupla da confrontare con la tupla corrente.
	 * @return distanza tra le due tuple di tipo double.
	 */
	public double getDistance(Tuple obj) {
		double dist=0;
		for(int i=0;i<this.getLength();i++) 
		{
			dist+=this.get(i).distance(obj.get(i));
		}
		return dist;
	}
	
	/**
	 * Restituisce la media delle distanze tra la tupla corrente e quelle ottenibili dalle righe 
	 * della tabella riferita da data aventi indice in clusteredData.
	 * 
	 * @param data : oggetto istanza della classe Data di cui è necessario 
	 * 							considerare alcune tuple.
	 * @param clusteredData : indici delle tuple all'interno della tabella riferita da data. 
	 * 
	 * @return la media delle distanze di tipo double.
	 */
	public double avgDistance(Data data, Set<Integer> clusteredData) {
		double p=0.0,sumD=0.0; 
		Iterator<Integer> it=clusteredData.iterator();
		while(it.hasNext()){
			Integer a=it.next();
			double d= getDistance(data.getItemSet(a)); 
			sumD+=d; 
		}
		p=sumD/clusteredData.size(); 
		return p;
	}
	
}
