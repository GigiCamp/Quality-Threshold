package data;

/**
 * ContinuousAttribute è una classe concreta che estende la classe Attribute e modella un attributo
 * continuo(numerico). Tale classe include i metodi di "scaling" del dominio dell'attributo nell'intervallo [0,1] al fine da rendere
 * confrontabili attributi aventi domini differenti.
 *
 * @author pa_pe
 */
public class ContinuousAttribute extends Attribute {
	
	/**
	 * Estremo massimo dell'intervallo di valori (dominio) che l'attributo può realmente assumere.
	 */
	private double max;
	
	
	/**
	 * Estremo minimo dell'intervallo di valori (dominio) che l'attributo può realmente assumere.
	 */
	private double min;

	/**
	 * Questo costruttore invoca il costruttore della classe base Attribute e inizializza i membri aggiunti per estensione.
	 * 
	 * @param name : nome dell'attributo.
	 * @param index : identificativo dell'attributo.
	 * @param min : estremo inferiore dell'intervallo di valori (dominio) che l'attributo può assumere.
	 * @param max : estremo superiore dell'intervallo di valori (dominio) che l'attributo può assumere.
	 * 
	 * @see Attribute#name
	 * @see Attribute#index
	 */
	ContinuousAttribute(String name, int index, double min, double max) {
		super(name, index);
		this.min=min;
		this.max=max;
	}
	
	/**
	 * Questo metodo calcola e restituisce il valore normalizzato del parametro passato in input.
	 * La normalizzazione ha come codominio l'intervallo [0,1]. La normalizzazione di v è calcolata
	 * mediante la formula: v'=(v-min)/(max-min).
	 * 
	 * @param v : valore dell'attributo da normalizzare.
	 * @return il valore normalizzato di tipo double.
	 */
	public double getScaledValue(double v) {
		return (v-min)/(max-min);
	}
}


