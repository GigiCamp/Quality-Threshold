package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {
	private String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; 
	private final String DBMS = "jdbc:mysql"; 
	private final String SERVER="localhost"; //contiene l’identificativo del server su cui risiede la base di dati (per esempio localhost) 
	private final String DATABASE = "MapDB"; //contiene il nome della base di dati 
	private final String PORT="3306"; //La porta su cui il DBMS MySQL accetta le connessioni 
	private final String USER_ID = "MapUser"; //contiene il nome dell’utente per l’accesso alla base di dati 
	private final String PASSWORD = "map"; //contiene la password di autenticazione per l’utente identificato da  USER_ID 
	private final String SERVER_TIMEZONE = "?serverTimezone=UTC";
	private Connection conn; //gestisce una connessione 
	
	public void initConnection() throws DatabaseConnectionException, ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException{
		Class.forName(DRIVER_CLASS_NAME).newInstance();
		conn=DriverManager.getConnection(DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE +  "?verifyServerCertificate=false&serverTimezone=UTC&useSSL=true", USER_ID, PASSWORD);
	}
	
	public Connection getConnection() {
		return conn;
	}
	
	public void closeConnection() throws SQLException {
		if (conn != null && !conn.isClosed()) {
			conn.close();
		}	

	}
}
