package data;

import java.io.Serializable;

/**
 * Attribute è una classe astratta che modella la entità attributo.
 * 
 * @author pa_pe
 *
 */
public abstract class Attribute implements Serializable {
	
	/**
	 *  Nome simbolico dell'attributo
	 * 
	 *  @see java.lang.String
	 */
	private String name = new String();
	
	
	/**
	 * Identificativo numerico dell'attributo
	 */
	private int index;
	
	/**
	 * Inizializza un attributo assegnandoli un nome e un indice.
	 * 
	 * @param name : che indica il nome simbolico dell'attributo.
	 * @param index : che indica l'identificatifo numerico dell'attributo.
	 */
	public Attribute(String name, int index) {
		this.name=name;
		this.index=index;
	}
	
	/**
	 * Ritorna un oggetto istanza della classe String corrispondente
	 * al nome dell'attributo.
	 *  
	 * @return {@link #name}
	 * 
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Ritorna un intero corrispondente all'identificativo dell'attributo
	 * 
	 * @return {@link #index}
	 */
	public int getIndex() {
		return index;
	}
	
	/**
	 * Metodo toString. Il metodo ritorna semplicemente il nome dell'attributo.
	 * 
	 * @return {@link #name}
	 */
	public String toString() {
		return this.name;
	}
	
}




