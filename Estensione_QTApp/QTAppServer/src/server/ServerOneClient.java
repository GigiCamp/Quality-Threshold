package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import data.Data;
import data.EmptyDatasetException;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import mining.ClusteringRadiusException;
import mining.QTMiner;

/**
 * La classe modella un Thread in grado di ascoltare le richieste da un singolo client.
 * E' istanziata da {@link MultiServer} non appena si verifica un connessione.
 * La classe stabilisce con il client un protocollo di comunicazione che si basa su uno scambio di
 * messaggi (oggetti istanza della classe String).<br>
 * Infatti per ogni operazione che va a buon fine la classe scrive sullo stream di output il messaggio
 * "OK" altrimenti un messaggio relativo all'eccezione.<br>
 * La classe si occupa di effettuare particolari operazioni in base alle richieste da parte del client.
 * Per la gestione delle richieste si veda il metodo {@link #run}
 * 
 * @author pa_pe
 *
 */
public class ServerOneClient extends Thread {
	private Socket socket; 
	private ObjectInputStream in;  
	private ObjectOutputStream out; 
	private QTMiner kmeans; 
	private Data data;

	private String operaz;
	private boolean flag = false;

	/**
	 * Questo metodo inizializza gli attributi socket, in e out. Avvia il thread.
	 * 
	 * @param s : oggetto di tipo Socket.
	 * @throws IOException : se falliscono o sono interrotte operazioni di I/O.
	 */
	public ServerOneClient(Socket s) throws IOException{
		this.socket = s;
	}

	/**
	 * Sovrascrive il metodo run della classe Thread.
	 * Il metodo run si occupa di gestire le richieste da parte del client che sono suddivise in
	 * due categorie:
	 * - Una di scoperta dei cluster sul database
	 * - Una di lettura di un risultato precedente di scoperta da file.
	 * Nel primo caso il client invia il comando 0:<br>
	 * Il metodo letto tale comando effettuerà la creazione dell'oggetto {@link data.Data} 
	 * e ci sarà la modifica del flag {@link #flag} a true.<br>
	 * Successivamente il client invia il comando 1:<br>
	 * Il metodo letto tale comando effettuerà l'
	 * attività di scoperta che avviene tramite il metodo {@link mining.QTMiner#compute(Data)}
	 * chiamata in questo caso.
	 * Successivamente il client invia il comando 2:<br>
	 * in questo caso il metodo letto tale comando effetua il salvataggio della scoperta su file.<br>
	 * Nel caso una di queste operazioni non va a buon fine, il server si occuperà
	 * di scrivere sullo stream di output il relativo messaggio di errore, viceversa per ogni
	 * operazione che va a buon fine, il server scrive sullo stream di output il messaggio
	 * "OK".<br><br>
	 * Nel caso il cui la richiesta + una lettura di un risultato precendente di scoperta su file
	 * , il client invia il comando 3. In questo caso verrà inizializzato l'attributo {@link #kmeans}
	 * attraverso il costruttore.<br>
	 * Per ciascun comando verrà aggiunta una nuova operazione all'attributo {@link #operaz} 
	 * e nel caso in cui non è stata effettuata alcuna operazione (e quindi il flag {@link #flag}
	 * risulterà essere <i>false</i>) e il client si disconnette, allora l'attributo 
	 * conterrà la stringa "Nessuna Operazione Effettuata".
	 * 
	 * @see java.lang.Thread#run()
	 *
	 */
	public void run(){
		String dbTable = "";
		Double iterfromtable = 0.0;
		int numIter = 0;

		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
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
						this.out.writeObject("Errore! - Nome Tabella Errato!" + "\nError! - You have entered an invalid table name!");
						break;
					}
					catch (EmptySetException e) {
						this.out.writeObject("Errore! - E' stato restituito un resultset vuoto!" + "\nError! - Empty resulset!");
						break;
					}
					catch (DatabaseConnectionException e) {
						this.out.writeObject("Errore! - Connessione al DB fallita!" + "\nError! - The connection to the DB has failed!");
						break;
					}
					catch (NoValueException e) {
						this.out.writeObject("Errore! - E' assente un valore all'interno del resultset!" + "\nError! - A value in the resulset is absent!");
						break;
					} catch (InstantiationException e) {
						this.out.writeObject("Errore! - Si è verificata una InstantiationException!" + "\nError! - InstantiationException!");
						break;
					} catch (IllegalAccessException e) {
						this.out.writeObject("Errore! - Si è verificata una IllegalAccessException!" + "\nError! - IllegalAccessException!");
						break;
					} catch (EmptyDatasetException e) {
						this.out.writeObject("Errore! - Il Dataset è vuoto!" + "\nError! - Empty Dataset!");
						break;
					}

					this.flag = true;
					this.out.writeObject("OK");

					break;

				case 1:
					iterfromtable = (Double)in.readObject();
					this.operaz = this.operaz + "-" + "Esecuzione KMeans (k = " + iterfromtable + ")";
					this.kmeans = new QTMiner(iterfromtable);

					try{
						numIter = this.kmeans.compute(this.data);
					}catch (ClusteringRadiusException e) {
						this.out.writeObject("Errore! E' presente un solo cluster!" + "\nError! There is only one cluster!");
						break;
					}

					this.out.writeObject("OK");
					this.out.writeObject(kmeans.getC().toString(this.data));

					break;

				case 2:
					this.operaz = this.operaz + "-" + "Salvataggio File";
					try{
						if(!(new File(dbTable + iterfromtable).exists())) {
							this.kmeans.salva(dbTable + iterfromtable);
							this.out.writeObject("OK");
							break;
						}else {
							this.out.writeObject("NO");
							break;
						}
					}
					catch(IOException e){
						this.out.writeObject("Errore! - Salvataggio del file non riuscito!" + "\nError! - File saving failed!");
						break;
					}

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
						this.out.writeObject("Errore! - File Non Trovato!" + "\nError! - File not found!");
						break;
					}

					this.out.writeObject("OK");
					this.out.writeObject(kmeans.getC().toString());

					break;

				case 4:
					this.operaz = this.operaz + "-" + "Salvataggio File";
					try{
						this.kmeans.salva(dbTable + iterfromtable);
					}
					catch(IOException e){
						this.out.writeObject("Errore! - Salvataggio del file non riuscito!" + "\nError! - File saving failed!");
						break;
					}

					this.out.writeObject("OK");
					break;

				}
			}
		} catch (IOException e) {
			if(!this.flag){
				System.out.println("Nessuna Operazione Effettuata");
			}

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		} finally{
			try {
				this.socket.close();
				System.out.println("Socket " + socket.getInetAddress() + " chiusa con successo!");
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}	
	}
}


