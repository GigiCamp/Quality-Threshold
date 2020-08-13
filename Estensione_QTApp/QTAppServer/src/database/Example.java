package database;

import java.util.ArrayList;
import java.util.List;



/**
 * Example è una classe che modella una transazione letta dalla base di dati.
 * 
 * @author pa_pe
 *
 */
public class Example implements Comparable<Example>{
	
	/**
	 * Attributo di classe classe che rappresenta la tupla letta dalla base di dati.
	 */
	private List<Object> example=new ArrayList<Object>();

	/**
	 * Aggiunge l'oggetto specificato come parametro alla lista {@link #example}.
	 * 
	 * @param o : oggetto da aggiungere alla transazione.
	 */
	public void add(Object o){
		example.add(o);
	}
	
	/**
	 * Restituisce l'oggetto presente nella tupla all'indice specificato come paramentro.
	 * 
	 * @param i : indice che verrà utilizzato per considerare un elemento
	 * 				della transazione rappresentata dalla lista {@link #example}.
	 * 
	 * @return un oggetto istanza della classe Object appartenente alla transazione
	 * 				letta dalla base di dati.
	 */
	public Object get(int i){
		return example.get(i);
	}
	/** 
	 * Metodo implementato dall'interfaccia Comparable.
	 * Questo metodo confronta due transazioni lette dal database.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Example ex) {
		
		int i=0;
		for(Object o:ex.example){
			if(!o.equals(this.example.get(i)))
				return ((Comparable<Object>)o).compareTo(example.get(i));
			i++;
		}
		return 0;
	}
	/** 
	 * Metodo toString. Sovrascrive il metodo toString della classe Object e si occupa di
	 * ritornare {@link #example} in formato stringa.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String str="";
		for(Object o:example)
			str+=o.toString()+ " ";
		return str;
	}
	
}