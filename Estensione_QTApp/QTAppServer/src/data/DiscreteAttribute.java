package data;

import java.util.Iterator;
import java.util.TreeSet;

/**
 * DiscreteAttribute è una classe concreta che estende la classe Attribute e rappresenta un attributo discreto(categorico).
 *
 * @author pa_pe
 */
public class DiscreteAttribute extends Attribute implements Iterable<String> {
	
	/**
	 * TreeSet di oggetti String, uno per ciascun valore del dominio discreto.
	 * I valori del dominio sono univoci e sono ordinati seguendo un ordine lessicografico.
	 */
	private TreeSet<String> values;
	
	/**
	 * Il costruttore invoca anzitutto il costruttore della classe madre e inizializza 
	 * un attributo discreto specificando il nome dell'attributo, l'identificativo numerico 
	 * dell'attributo e il TreeSet di stringhe rappresentanti il dominio dell'attributo.
	 * 
	 * @param name : nome dell'attributo.
	 * @param index : identificativo dell'attributo.
	 * @param values : treeSet rappresentante il dominio di valori per l'attributo.
	 * 
	 * @see java.util.TreeSet
	 */
	public DiscreteAttribute(String name, int index, TreeSet<String> values) {
		super(name,index);
		this.values=values;
	}
	
	/**
	 * Restituisce il numero di elementi caratterizzanti il dominio dell'attributo presenti
	 * nel TreeSet {@link #values}.
	 * 
	 * @return {@link java.util.TreeSet#size()}
	 */
	public int getNumberOfDistinctValues() {
		return values.size();
	}

	/** 
	 * Ritorna l'iteratore del TreeSet {@link #values}
	 * 
	 * @return {@link java.util.TreeSet#iterator()}
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<String> iterator(){
		return values.iterator();
	}
}
