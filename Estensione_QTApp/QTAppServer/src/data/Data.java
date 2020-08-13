package data;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.EmptySetException;
import database.Example;
import database.NoValueException;
import database.QUERY_TYPE;
import database.TableData;
import database.TableSchema;
import database.TableSchema.Column;

/**
 * La classe Data modella l'insieme di transazioni (o tuple) ottenute dalla base di dati.
 * La classe fa ampio uso delle classi presenti all'interno del package database, per leggere le
 * singole tuple appartenenti alla base di dati rendendole utilizzabili all'interno dell'intero 
 * sistema.
 * La gestione delle tuple avviene tramite i metodi della classe {@link database.TableData} in
 * particolare: {@link database.TableData#getDistinctTransazioni(String)}, 
 * {@link database.TableData#getAggregateColumnValue(String, database.TableSchema.Column, QUERY_TYPE)},
 * {@link database.TableData#getDistinctColumnValues(String, database.TableSchema.Column)},
 * sfruttando il risultato della classe {@link database.TableSchema}.
 * L'accesso alla base di dati avviene attraverso la classe {@link database.DbAccess}
 * 
 * Le tuple che caratterizzeranno la tabella data sono univoche, quindi duplicati presenti
 * all'interno della base di dati verranno considerati una sola volta (migliorando così le 
 * operazioni di scoperta dei cluster e rendendole più efficienti).<br>
 * Quindi la tabella Data rappresenta tutti i dati che verranno utilizzati all'interno del sistema,
 * non a caso prima di effettuare qualsiasi operazione da parte del server, viene innanzitutto 
 * istanziato un oggetto di questa classe e i dati prodotti vengono resi disponibili per tutti 
 * le attività del sistema (a meno che non sia un'attività di
 * scoperta di cluster da file, in questo caso la tabbella data non è istanziata e i cluster
 * sono ottenuto dal file).
 * Per ottenere correttamente i dati è necessario che all'atto dell'istanziazione della classe
 * sia specificato il nome della tabella, che coincide esattamente con quello della tabella contenuta
 * nella base di dati.
 * 
 * @see database.TableData
 * @see database.TableSchema
 * 
 * @author pa_pe
 *
 */
public class Data {
	
	private List<Example> data = new ArrayList<Example>();
	private int numberOfExamples;
	private List<Attribute> explanatorySet=new LinkedList<Attribute>();

	/**
	 * Questo metodo si occupa di caricare i dati di addestramento da una tabella della base di dati.
	 * 
	 * @param table : nome della tabella.
	 * @throws EmptyDatasetException : se viene restituito un dataset vuoto.
	 * @throws ClassNotFoundException : se non viene trovata nessuna definizione per la classe con il nome specificato.
	 * @throws DatabaseConnectionException : se fallisce la connessione al database.
	 * @throws SQLException : se SQL Server restituisce un avviso o un errore.
	 * @throws EmptySetException : se viene restituito un resultset vuoto.
	 * @throws InstantiationException : se il class object specificato non può essere istanziato.
	 * @throws IllegalAccessException : se si tenta di accedere a membri o metodi la cui visibilità non lo concede.
	 * @throws NoValueException : se è assente un valore all'interno del resultset.
	 */
	public Data(String table) throws EmptyDatasetException, ClassNotFoundException, DatabaseConnectionException, SQLException, EmptySetException, InstantiationException, IllegalAccessException, NoValueException  {

		DbAccess DataBase=new DbAccess();
		TableData t=new TableData(DataBase);
		data=t.getDistinctTransazioni(table);
		
		if(data.size()==0 || data==null) throw new EmptyDatasetException();

		TableSchema s=new TableSchema(DataBase, table);
		for(int i=0;i<s.getNumberOfAttributes();i++) {
			Column c=s.getColumn(i);
			if(c.isNumber()==false) {
				Set<Object> l= t.getDistinctColumnValues(table, c);
				TreeSet<String> ts=new TreeSet<String>();
				Iterator<Object> it=l.iterator();
				while(!it.hasNext()) 
					ts.add((String) it.next());
				explanatorySet.add(new DiscreteAttribute(c.getColumnName(), i, ts));
				}
				else {
					Double min=((Number) t.getAggregateColumnValue(table, c, QUERY_TYPE.MIN)).doubleValue();
					Double max=((Number) t.getAggregateColumnValue(table, c, QUERY_TYPE.MAX)).doubleValue();
					explanatorySet.add(new ContinuousAttribute(c.getColumnName(), i, min, max));
				}
			}
			numberOfExamples=data.size();
		}

	/**
	 * Restituisce la cardinalità dell'insieme di transazioni, che corrisponde alla dimensione di data.
	 * 
	 * @return {@link #numberOfExamples}.
	 */
	public int getNumberOfExamples() {
		return numberOfExamples;
	}

	/**
	 * Restituisce la cardinalità dell'insieme di attributi.
	 * 
	 * @return la dimensione di {@link #explanatorySet}.
	 */
	public int getNumberOfExplanatoryAttributes() {
		return explanatorySet.size();
	}

	/**
	 * Restituisce l'explanatorySet, ovvero lo schema della tabella di dati.
	 * 
	 * @return {@link #explanatorySet}.
	 */
	public List<Attribute> getAttributeSchema() {
		return explanatorySet;
	}

	/**
	 * Restituisce l'oggetto in posizione i-esima all'interno dell'ArrayList {@link #data}.
	 * 
	 * @param index : indice in riferimento alla ArrayList memorizzata in data.
	 * @return oggetto in posizione i-esima.
	 */
	public Object getValue(int index) {
		return data.get(index);
	}

	/** 
	 * Metodo toString. Sovrascrive il metodo toString della classe Object e si occupa di 
	 * fornire una visualizzazione grafica della tabella.
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String output = new String();
		output=output+(getAttributeSchema());
		output+="\n";
		for(int j=0;j<getNumberOfExamples();j++) {
			output=output+(j+1)+":";
			for(int k=0;k<getNumberOfExplanatoryAttributes();k++) {
				output=output+" "+getValue(k)+" |";
			}
			output+="\n";
		}
		return output;
	}

	/**
	 * Restituisce un oggetto di tipo Tuple, caratterizzato da una coppia (DiscreteItem - int) o da una coppia 
	 * (ContinuousItem - int).
	 * 
	 * @param index : indice in riferimento alla ArrayList memorizzata in data.
	 * @return un oggetto di tuple che modella come sequenza di coppie (Attributo - valore) l'Example in i-esima posizione in data.
	 * 
	 * @see Tuple
	 */
	public Tuple getItemSet(int index) {
		Tuple tuple = new Tuple(explanatorySet.size());
		for (int i = 0; i < explanatorySet.size(); i++) {
			if(explanatorySet.get(i) instanceof DiscreteAttribute) {
				tuple.add(new DiscreteItem((DiscreteAttribute) explanatorySet.get(i), (String) data.get(index).get(i)), i);
			}
			if(explanatorySet.get(i) instanceof ContinuousAttribute) {
				tuple.add(new ContinuousItem((ContinuousAttribute) explanatorySet.get(i), (Double) data.get(index).get(i)), i);
			}
		}
			return tuple;
	}


	/**
	 * Questo main è utilizzato per testare la singola classe Data.
	 * 
	 * @throws ClassNotFoundException : se non viene trovata nessuna definizione per la classe con il nome specificato.
	 * @throws DatabaseConnectionException : se fallisce la connessione al database.
	 * @throws SQLException : se SQL Server restituisce un avviso o un errore.
	 * @throws EmptySetException : se viene restituito un resultset vuoto.
	 * @throws InstantiationException : se il class object specificato non può essere istanziato.
	 * @throws IllegalAccessException : se si tenta di accedere a membri o metodi la cui visibilità non lo concede.
	 * @throws NoValueException : se è assente un valore all'interno del resultset.
	 */
	public static void main(String args[]) throws ClassNotFoundException, DatabaseConnectionException, SQLException, EmptySetException, InstantiationException, IllegalAccessException, NoValueException {
		Data trainingSet;
		try {
			trainingSet = new Data("playtennis");
			System.out.println(trainingSet);
		} catch (EmptyDatasetException e) {}
	}

}
