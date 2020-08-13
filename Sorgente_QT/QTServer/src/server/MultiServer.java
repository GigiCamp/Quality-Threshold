package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {
	private int port;

	public static void main(String[] args) throws IOException {
		MultiServer ms=new MultiServer(Integer.parseInt(args[0]));
		ms.run();
	}


	public MultiServer(int port) throws IOException {
		this.port=port;
		run();
	}

	public void run() {

		ServerSocket s = null;

		try{

			s = new ServerSocket(this.port);

			try {
				System.out.println("---- Server Avviato Correttamente! ---- \n Server in attesa di richieste...");


				while(true) {
					Socket socket = s.accept();
					try{
						new ServerOneClient(socket);
					} catch(IOException e) {

					}

				}
			} catch (IOException e){
				System.out.println("Errore nell'attesa di connessioni da parte dei client");
			} finally {
				try {
					s.close();
				} catch (IOException e) {
					System.out.println("\nErrore! - Impossibile chiudere il Server sulla porta: " + this.port);
				}
			}
		} catch (IOException e){
			System.out.println("Errore! - Impossibile creare il Server sulla porta: " + this.port);
		}
	}
}


