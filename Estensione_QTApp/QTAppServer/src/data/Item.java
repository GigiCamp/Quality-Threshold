package data;

import java.io.Serializable;

/**
 * Item è una classe astratta che modella un generico item coppia (attributo - valore).
 * 
 * @author pa_pe
 */
public abstract class Item implements Serializable {

	/**
	 * Attributo coinvolto nell'item.
	 */
	private Attribute attribute;
	
	
	/**
	 * Valore assegnato all'attributo.
	 */
	private Object value;
	
	/**
	 * Questo costruttore inizializza i valori dei membri attributi con i parametri in input.
	 * 
	 * @param attribute : attributo coinvolto nell'item.
	 * @param value : valore assegnato all'attributo.
	 */
	public Item(Attribute attribute, Object value) {
		this.attribute=attribute;
		this.value=value;
	}
	
	/**
	 * Restituisce attribute.
	 * 
	 * @return {@link #attribute}
	 */
	public Attribute getAttribute() {
		return this.attribute;
	}
	
	/**
	 * Restituisce value.
	 * 
	 * @return {@link #value}
	 */
	public Object getValue() {
		return this.value;
	}
	
	/** 
	 * Sovrascrive il metodo toString della classe Object e si occupa di
	 * ritornare il valore per l'attributo in formato stringa.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.value.toString();
	}
	
	/**
	 * Questo è un metodo astratto e la sua implementazione sarà diversa per item discreto e item continuo.
	 * 
	 * @param a : oggetto che corrisponde a un DiscreteAttribute o a un ContinuousAttribute.
	 * @return un valore di tipo double.
	 */
	public abstract double distance(Object a);
		
}
