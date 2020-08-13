package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import data.Data;
import data.EmptyDatasetException;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import javafx.application.Platform;
import mining.ClusteringRadiusException;
import mining.QTMiner;

public class ServerOneClient extends Thread {
	private Socket socket; 
	private ObjectInputStream in;  
	private ObjectOutputStream out; 
	private QTMiner kmeans; 
	private Data data;

	static private int cntClient = 0;
	private String operaz;
	private boolean flagOp = false;

	public ServerOneClient(Socket s) throws IOException{
		this.socket = s;
		this.in = new ObjectInputStream(socket.getInputStream());
		this.out = new ObjectOutputStream(socket.getOutputStream());
		super.start();
	}

	public void run(){
		String dbTable = "";
		Double iterfromtable = 0.0;
		int numIter = 0;

		try {
			while(true){
				Object input = in.readObject();
				int sceltaClient = (Integer)input;

				switch(sceltaClient)
				{
				case 0:
					if(this.operaz == null)
						this.operaz = "Connessione al Database";
					else
						this.operaz = this.operaz + "Connessione al Database";
					dbTable = (String)in.readObject();
					try{
						this.data = new Data(dbTable);
					} 
					catch(SQLException e){
						this.out.writeObject("Errore! - Nome Tabella Errato!");
						break;
					}
					catch (EmptySetException e) {
						this.out.writeObject(e.getMessage());
						break;
					}
					catch (DatabaseConnectionException e) {
						this.out.writeObject(e.getMessage());
						break;
					}
					catch (NoValueException e) {
						this.out.writeObject(e.getMessage());
						break;
					} catch (InstantiationException e) {
						this.out.writeObject(e.getMessage());
						break;
					} catch (IllegalAccessException e) {
						this.out.writeObject(e.getMessage());
						break;
					} catch (EmptyDatasetException e) {
						this.out.writeObject(e.getMessage());
						break;
					}

					this.flagOp = true;
					this.out.writeObject("OK");

					break;

				case 1:
					iterfromtable = (Double)in.readObject();
					this.operaz = this.operaz + "-" + "Esecuzione KMeans (k = " + iterfromtable + ")";
					this.kmeans = new QTMiner(iterfromtable);

					try{
						numIter = this.kmeans.compute(this.data);
					}catch (ClusteringRadiusException e) {
						this.out.writeObject("Errore! - E' presente un solo cluster!");
						break;
					}

					this.out.writeObject("OK");
					this.out.writeObject(numIter);
					this.out.writeObject(kmeans.getC().toString(this.data));

					break;

				case 2:
					this.operaz = this.operaz + "-" + "Salvataggio File";
					try{
						this.kmeans.salva(dbTable + iterfromtable);
					}
					catch(IOException e){
						this.out.writeObject("Errore! - Salvataggio del file non riuscito!");
						break;
					}

					this.out.writeObject("OK");
					break;

				case 3:
					String filetableName = (String)this.in.readObject();
					String iterate = Double.toString((double)this.in.readObject());


					if(this.operaz == null)
						this.operaz = "Esecuzione KMeans da File (k = " + iterate + ")";
					else
						this.operaz = this.operaz + "-" + "Esecuzione KMeans da File (k = " + iterate + ")";


					try{
						kmeans = new QTMiner(filetableName + iterate);
					}
					catch(FileNotFoundException e){
						this.out.writeObject("Errore! - File Non Trovato!");
						break;
					}

					this.out.writeObject("OK");
					this.out.writeObject(kmeans.getC().toString());

					break;
					}
			}
		} catch (IOException e) {
			if(!this.flagOp){
				System.out.println("Nessuna Operazione Effettuata");
			}

		} catch (ClassNotFoundException e) {

		} finally{
			try {
				this.socket.close();
			} catch (IOException e) {

			}
		}	
	}
}


