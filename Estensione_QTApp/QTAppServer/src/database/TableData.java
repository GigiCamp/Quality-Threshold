package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;



import database.TableSchema.Column;


/**
 * TableData è una classe che modella l'insieme di transazioni collezionate in una tabella. La singola transazione è modellata dalla classe Example.
 * 
 * @author pa_pe
 *
 */
public class TableData {

	/**
	 * Fornisce alla classe l'accesso alla base di dati.
	 * 
	 *  @see database.DbAccess
	 */
	DbAccess db;
	

	
	/**
	 * Il costruttore della classe si occupa di inizializzare il membro {@link #db}
	 * in modo da rendere disponibile l'accesso alla base di dati all'intera classe.
	 * 
	 * @param db : riferimento ad un oggetto istanza della classe {@link database.DbAccess}
	 */
	public TableData(DbAccess db) {
		this.db=db;
	}

	/**
	 * Questo metodo ricava lo schema della tabella con nome table. Esegue una interrogazione per estrarre le tuple distinte
	 * da tale tabella. Per ogni tupla del resultset, si crea un oggetto, istanza della classe Example, il cui riferimento va incluso 
	 * nella lista da restituire.
	 * 
	 * @param table : nome della tabella nel database.
	 * @return lista di transazioni distinte memorizzate nella tabella.
	 * @throws SQLException : se SQL Server restituisce un avviso o un errore.
	 * @throws EmptySetException : se viene restituito un resultset vuoto.
	 * @throws ClassNotFoundException : se non viene trovata nessuna definizione per la classe con il nome specificato.
	 * @throws DatabaseConnectionException : se fallisce la connessione al database.
	 * @throws InstantiationException : se il class object specificato non può essere istanziato.
	 * @throws IllegalAccessException : se si tenta di accedere a membri o metodi la cui visibilità non lo concede.
	 */
	public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException, ClassNotFoundException, DatabaseConnectionException, InstantiationException, IllegalAccessException{
		LinkedList<Example> transSet = new LinkedList<Example>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		
		
		String query="select distinct ";
		
		for(int i=0;i<tSchema.getNumberOfAttributes();i++){
			Column c=tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		query += (" FROM "+table);
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		boolean empty=true;
		while (rs.next()) {
			empty=false;
			Example currentTuple=new Example();
			for(int i=0;i<tSchema.getNumberOfAttributes();i++)
				if(tSchema.getColumn(i).isNumber())
					currentTuple.add(rs.getDouble(i+1));
				else
					currentTuple.add(rs.getString(i+1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();
		if(empty) throw new EmptySetException();
		
		
		return transSet;

	}

	
	/**
	 * Questo metodo formula ed esegue una interrogazione SQL per estrarre i valori distinti ordinati di column e popolare un insieme da restituire.
	 * 
	 * @param table : nome della tabella.
	 * @param column : nome della colonna nella tabella.
	 * @return insieme di valori distinti ordinati in modalità ascendente che l'attributo identificato da nome column 
	 * assume nella tabella identificata dal nome table.
	 * @throws SQLException : se SQL Server restituisce un avviso o un errore.
	 * @throws ClassNotFoundException : se non viene trovata nessuna definizione per la classe con il nome specificato.
	 * @throws DatabaseConnectionException : se fallisce la connessione al database.
	 * @throws InstantiationException : se il class object specificato non può essere istanziato.
	 * @throws IllegalAccessException : se si tenta di accedere a membri o metodi la cui visibilità non lo concede.
	 */
	public  Set<Object>getDistinctColumnValues(String table,Column column) throws SQLException, ClassNotFoundException, DatabaseConnectionException, InstantiationException, IllegalAccessException{
		Set<Object> valueSet = new TreeSet<Object>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		
		
		String query="select distinct ";
		
		query+= column.getColumnName();
		
		query += (" FROM "+table);
		
		query += (" ORDER BY " +column.getColumnName());
		
		
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
				if(column.isNumber())
					valueSet.add(rs.getDouble(1));
				else
					valueSet.add(rs.getString(1));
			
		}
		rs.close();
		statement.close();
		
		return valueSet;

	}

	/**
	 * Questo metodo formula ed esegue una interrogazione SQL per estrarre il valore aggregato (valore minimo o valore massimo) cercato nella
	 * colonna di nome column della tabella di nome table.
	 * 
	 * @param table : nome di tabella.
	 * @param column : nome di colonna.
	 * @param aggregate : operatore SQL di aggregazione(min, max).
	 * @return aggregato cercato.
	 * @throws SQLException : se SQL Server restituisce un avviso o un errore.
	 * @throws NoValueException : se è assente un valore all'interno del resultset.
	 * @throws ClassNotFoundException : se non viene trovata nessuna definizione per la classe con il nome specificato.
	 * @throws DatabaseConnectionException : se fallisce la connessione al database.
	 * @throws InstantiationException : se il class object specificato non può essere istanziato.
	 * @throws IllegalAccessException : se si tenta di accedere a membri o metodi la cui visibilità non lo concede.
	 */
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException, ClassNotFoundException, DatabaseConnectionException, InstantiationException, IllegalAccessException{
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		Object value=null;
		String aggregateOp="";
		
		String query="select ";
		if(aggregate==QUERY_TYPE.MAX)
			aggregateOp+="max";
		else
			aggregateOp+="min";
		query+=aggregateOp+"("+column.getColumnName()+ ") FROM "+table;
		
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		if (rs.next()) {
				if(column.isNumber())
					value=rs.getFloat(1);
				else
					value=rs.getString(1);
			
		}
		rs.close();
		statement.close();
		if(value==null)
			throw new NoValueException("No " + aggregateOp+ " on "+ column.getColumnName());
			
		return value;

	}

	

	

}
