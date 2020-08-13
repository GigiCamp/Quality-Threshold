package database;

import java.sql.Connection;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




/**
 * TableSchema è una classe che modella lo schema di una tabella nel database relazionale.
 * 
 * @author pa_pe
 *
 */
public class TableSchema {
	
	
	/**
	 * Fornisce alla classe l'accesso alla base di dati.
	 * 
	 *  @see database.DbAccess
	 */
	DbAccess db;
	
	
	/**
	 * Column è una classe che modella l'elemento colonna presente all'interno di una tabella.
	 * 
	 * @author pa_pe
	 *
	 */
	public class Column{
		
		/**
		 * Attributo di classe che indica il nome della colonna.
		 */
		private String name;
		
		
		/**
		 * Attributo di classe che indica il tipo di dato contenuto al suo interno.
		 */
		private String type;
		
		
		/**
		 * Costruttore che inizializza un oggetto di Column assegnando un nome e un type.
		 * 
		 * @param name : nome da assegnare alla colonna.
		 * @param type : tipo dei dati contenuti nella colonna.
		 */
		Column(String name,String type){
			this.name=name;
			this.type=type;
		}
		
		
		/**
		 * Restituisce il nome della colonna.
		 * 
		 * @return {@link #name}
		 */
		public String getColumnName(){
			return name;
		}
		
		
		/**
		 * Restituisce true se il tipo dei datti contenuti nella colonna è di tipo "number", false altrimenti.
		 * 
		 * @return un valore booleano
		 */
		public boolean isNumber(){
			return type.equals("number");
		}
		
		
		/** 
		 * Metodo toString. Restituisce una stringa contenente le informazioni relativa a una colonna.
		 * 
		 * @see java.lang.Object#toString()
		 */
		public String toString(){
			return name+":"+type;
		}
	}
	
	
	/**
	 * Rappresenta una List popolata da oggetti di tipo Column.
	 */
	List<Column> tableSchema=new ArrayList<Column>();
	
	/**
	 * Questo metodo mappa le colonne presenti all'interno della tabella specificata da tableName, con il loro relativo name e type, e 
	 * le inserisce all'interno di {@link #tableSchema}.
	 * 
	 * @param db : riferimento ad un oggetto istanza della classe {@link database.DbAccess}.
	 * @param tableName : nome della tabella.
	 * @throws SQLException : se SQL Server restituisce un avviso o un errore.
	 * @throws ClassNotFoundException : se non viene trovata nessuna definizione per la classe con il nome specificato.
	 * @throws DatabaseConnectionException : se fallisce la connessione al database.
	 * @throws InstantiationException : se il class object specificato non può essere istanziato.
	 * @throws IllegalAccessException : se si tenta di accedere a membri o metodi la cui visibilità non lo concede.
	 */
	public TableSchema(DbAccess db, String tableName) throws SQLException, ClassNotFoundException, DatabaseConnectionException, InstantiationException, IllegalAccessException{
		this.db=db;
		HashMap<String,String> mapSQL_JAVATypes=new HashMap<String, String>();
		//http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
		mapSQL_JAVATypes.put("CHAR","string");
		mapSQL_JAVATypes.put("VARCHAR","string");
		mapSQL_JAVATypes.put("LONGVARCHAR","string");
		mapSQL_JAVATypes.put("BIT","string");
		mapSQL_JAVATypes.put("SHORT","number");
		mapSQL_JAVATypes.put("INT","number");
		mapSQL_JAVATypes.put("LONG","number");
		mapSQL_JAVATypes.put("FLOAT","number");
		mapSQL_JAVATypes.put("DOUBLE","number");
		
		
		
		db.initConnection();
		 Connection con=db.getConnection();
		 DatabaseMetaData meta = con.getMetaData();
	     ResultSet res = meta.getColumns(null, null, tableName, null);
		   
	     while (res.next()) {
	         
	         if(mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
	        		 tableSchema.add(new Column(
	        				 res.getString("COLUMN_NAME"),
	        				 mapSQL_JAVATypes.get(res.getString("TYPE_NAME")))
	        				 );
	
	         
	         
	      }
	      res.close();
	
	
	    
	    }
	  
	
		/**
		 * Restituisce il numero di attributi presenti nella tabella, che sono stati racchiusi all'interno di {@link #TableSchema}.
		 * 
		 * @return un valore intero, che rappresenta la dimensione di {@link #tableSchema}
		 */
		public int getNumberOfAttributes(){
			return tableSchema.size();
		}
		
		/**
		 * Restituisce un oggetto di tipo Column contenuto all'interno di {@link #tableSchema} nella posizione specificata da index.
		 * 
		 * @param index : indice che fa riferimento a {@link #tableSchema}.
		 * @return un oggetto di tipo Column
		 */
		public Column getColumn(int index){
			return tableSchema.get(index);
		}

		
	}

		     


