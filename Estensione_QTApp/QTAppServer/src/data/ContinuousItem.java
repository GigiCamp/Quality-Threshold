package data;

/**
 *	ContinuousItem è una classe concreta che estende la classe Item e modella 
 *	una coppia (attributo continuo - valore numerico).
 *
 * @author pa_pe
 */
public class ContinuousItem extends Item {

	/**
	 * Questo costruttore richiama il costruttore della classe base inizializzando l'attributo coninvolto
	 * nell'item di tipo {@link ContinuousAttribute} assegnandoli il valore specificato.
	 * 
	 * @param attribute : ContinuousAttribute coinvolto nell'item.
	 * @param value : valore da assegnare all'item.
	 */
	ContinuousItem(Attribute attribute, Double value) {
		super(attribute, value);
	}
	
	/** 
	 * Determina la distanza (in valore assoluto) tra il valore
	 * scalato memorizzato nello item corrente {@link Item#getValue()} e quello scalato
	 * associalto al parametro a.<br>
	 * Per ottenere valori scalati si utilizza {@link ContinuousAttribute#getScaledValue(double)}
	 * 
	 * @see data.Item#distance(java.lang.Object)
	 */
	public double distance(Object a) {
		ContinuousAttribute b=(ContinuousAttribute) this.getAttribute();
		ContinuousItem c=(ContinuousItem) a;
		double i=b.getScaledValue((double)this.getValue());
		double j=b.getScaledValue((double)c.getValue());
		return Math.abs(i-j);
	}	
}

