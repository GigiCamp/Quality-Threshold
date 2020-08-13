package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DbAccess è una classe che realizza l'accesso alla base di dati. La classe si occupa di effettuare una 
 * connessione alla base di dati sfruttando i driver di connessione mysql.
 * 
 * @author pa_pe
 *
 */
public class DbAccess {
	/**
	 * Nome del driver utilizzato per la connessione al database.
	 */
	private String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; 
	
	
	/**
	 * Tipo di DBMS.
	 */
	private final String DBMS = "jdbc:mysql"; 
	
	
	/**
	 * Indirizzo necessario per la connessione al database.
	 */
	private final String SERVER="localhost"; //contiene l’identificativo del server su cui risiede la base di dati (per esempio localhost) 
	
	
	/**
	 * Nome del database.
	 */
	private final String DATABASE = "MapDB"; //contiene il nome della base di dati 
	
	
	/**
	 * Porta sulla quale è disponibile il servizio mysql.
	 */
	private final String PORT="3306"; //La porta su cui il DBMS MySQL accetta le connessioni 
	
	
	/**
	 *  User id necessario per la connessione.
	 */
	private final String USER_ID = "MapUser"; //contiene il nome dell’utente per l’accesso alla base di dati 
	
	
	/**
	 * Password necessaria per la connessione.
	 */
	private final String PASSWORD = "map"; //contiene la password di autenticazione per l’utente identificato da  USER_ID 
	
	
	/**
	 * Oggetto istanza della classe Connection.
	 * 
	 * @see java.sql.Connection
	 */
	private Connection conn; //gestisce una connessione 
	
	/**
	 * Questo metodo impartisce al class loader l'ordine di caricare il driver mysql, inizializza la connessione riferita da conn. 
	 * 
	 * @throws DatabaseConnectionException : se fallisce la connessione al database.
	 * @throws ClassNotFoundException : se non viene trovata nessuna definizione per la classe con il nome specificato.
	 * @throws SQLException : se SQL Server restituisce un avviso o un errore.
	 * @throws InstantiationException : se il class object specificato non può essere istanziato.
	 * @throws IllegalAccessException : se si tenta di accedere a membri o metodi la cui visibilità non lo concede.
	 */
	public void initConnection() throws DatabaseConnectionException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
		Class.forName(DRIVER_CLASS_NAME).newInstance();
		conn=DriverManager.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE +  "?verifyServerCertificate=false&serverTimezone=UTC&useSSL=true", USER_ID, PASSWORD);
	}
	
	/**
	 * Restituisce l'attributo di classe {@link #conn}.
	 * 
	 * @return conn
	 */
	public Connection getConnection() {
		return conn;
	}
	
	/**
	 * Questo metodo chiude la connessione {@link #conn}.
	 * 
	 * @throws SQLException : se SQL Server restituisce un avviso o un errore.
	 */
	public void closeConnection() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.close();
		}	

	}
}
