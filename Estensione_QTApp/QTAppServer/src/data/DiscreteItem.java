package data;

/**
 * DiscreteItem è una classe concreta che estende la classe Item e rappresenta una coppia (Attributo discreto - Valore discreto).
 * 
 * @author pa_pe
 */
public class DiscreteItem extends Item {

	/**
	 * Invoca il costruttore della classe madre inizializzando l'attributo coninvolto
	 * nell'item di tipo {@link DiscreteAttribute} assegnandoli il valore specificato.
	 * 
	 * @param attribute : discreteAttribute coinvolto nell'item corrente.
	 * @param value : valore da assegnare all'item. 
	 */
	DiscreteItem(DiscreteAttribute attribute, String value) {
		super(attribute, value);
	}
	
	/** 
	 * Implementa il metodo definito come astratto nella classe madre.
	 * Esso effettua un controllo tra stringhe.
	 * In particolare verrà restituito 0 se (getValue().equals(a)), se
	 * il valore dell'attributo corrente (di tipo String) è uguale al valore 
	 * specificato come parametro (verrà richiamato il {@link Object#toString()}
	 * dell'oggetto specificato). Se le due stringhe sono diverse viene restituito 1.
	 */
	public double distance(Object a) {
		DiscreteItem b=(DiscreteItem) a;
		if(getValue().equals(b.toString())) 
			return 0;
		else
			return 1;
	}
}
