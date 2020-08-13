package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * La classe MultiServer modella un Thread in grado di ascoltare differenti richieste per
 * differenti client andando quindi a realizzare un server che ascolta più richieste.
 * Per ogni richiesta viene creato un thread a parte. Sarà tale thread ad ascoltare
 * le richieste per un determinato client.
 * 
 * @author pa_pe
 * 
 * @see java.lang.Thread
 *
 */
public class MultiServer extends Thread{
	
	/**
	 * Porta su cui il server è in ascolto.
	 */
	private int port;
	
	public static void main(String[] args) throws IOException {
		int port = Integer.parseInt(args[0]);
		if (port < 1024 || port > 65535) {
			System.err.println("Porta non valida!");
			return;
		}
		new MultiServer(port);
	}

	/**
	 * Costruttore di classe. Inizializza la porta ed invoca run().
	 * 
	 * @param port Porta su cui il server è in ascolto
	 * @throws IOException : se falliscono o sono interrotte operazioni di I/O.
	 */
	public MultiServer(int port) throws IOException{
		this.port = port;
		this.run();
	}

	/** 
	 * Istanzia un oggetto istanza della classe ServerSocket che pone in attesa di crichiesta di connessioni da parte del client. 
	 * Ad ogni nuova richiesta connessione si istanzia ServerOneClient. 
	 * 
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		ServerSocket serverSocket = null;
		try{
			Socket socket = null;
			serverSocket = new ServerSocket(port);

			try {
				System.out.println("---- Server Avviato Correttamente! ---- \n Server in attesa di richieste...");
				while(true) {
					socket = serverSocket.accept();
					try{
						Thread acceptedThread = new Thread(new ServerOneClient(socket));
						System.out.println(socket);
						acceptedThread.setDaemon(true); //termina il thread quando il programma termina
						acceptedThread.start();
					} catch(IOException e) {
						e.printStackTrace();
						socket.close();
					}

				}
			} catch (IOException e){
				System.out.println("Errore nell'attesa di connessioni da parte dei client!\n");
			} finally {
				try {
					serverSocket.close();
				} catch (IOException e) {
					System.out.println("\nErrore! - Impossibile chiudere il Server sulla porta: " + this.port);
				}
			}
		} catch (IOException e){
			System.out.println("Errore! - Impossibile creare il Server sulla porta: " + this.port);
		}
	}

}


