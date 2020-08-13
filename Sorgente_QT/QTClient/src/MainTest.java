import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import keyboardinput.Keyboard;



public class MainTest {

	/**
	 * @param args
	 */
	private ObjectOutputStream out;
	private ObjectInputStream in ; // stream con richieste del client

	public MainTest(String ip, int port) throws IOException{
		
		InetAddress addr=null;
		
		try {
			addr = InetAddress.getByName(ip); //ip
			System.out.println("addr = " + addr);
		}catch(IOException e) {
			System.out.println("Errore - IP non valido!");
			System.exit(0);
		}
		
		try {
			Socket socket = new Socket(addr, port); //Port
			System.out.println(socket);
			
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());	 // stream con richieste del client
		}catch(IOException e) {
			System.out.println("Errore - Nessun server in ascolto sulla porta specificata!");
			System.exit(0);
		}
}

	private int menu(){
		int answer;

		do{
			System.out.println("(1) Load clusters from file");
			System.out.println("(2) Load data from db");
			System.out.print("(1/2):");
			answer=Keyboard.readInt();
		}
		while(answer<=0 || answer>2);
		return answer;

	}

	private String learningFromFile() throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(3);

		System.out.print("Table Name:");
		String tabName=Keyboard.readString();
		out.writeObject(tabName);
		double r=1.0;
		do{
			System.out.print("Radius:");
			r=Keyboard.readDouble();
		} while(r<=0);
		out.writeObject(r);
		String result = (String)in.readObject();
		if(result.equals("OK"))
			return (String)in.readObject();
		else throw new ServerException(result);

	}
	private void storeTableFromDb() throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(0);
		System.out.print("Table name:");
		String tabName=Keyboard.readString();
		out.writeObject(tabName);
		String result = (String)in.readObject();
		if(!result.equals("OK"))
			throw new ServerException(result);

	}
	private String learningFromDbTable() throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(1);
		double r=1.0;
		do{
			System.out.print("Radius:");
			r=Keyboard.readDouble();
		} while(r<=0);
		out.writeObject(r);
		String result = (String)in.readObject();
		if(result.equals("OK")){
			System.out.println("Number of Clusters:"+in.readObject());
			return (String)in.readObject();
		}
		else throw new ServerException(result);


	}

	private void storeClusterInFile() throws SocketException,ServerException,IOException,ClassNotFoundException{
		out.writeObject(2);


		String result = (String)in.readObject();
		if(!result.equals("OK"))
			throw new ServerException(result);

	}
	public static void main(String[] args) {
		System.out.print("Inserire l'IP: ");
		String ip=Keyboard.readString();
		System.out.print("Inserire la porta: ");
		int port=new Integer(Keyboard.readString()).intValue();
		MainTest main=null;
		try{
			main=new MainTest(ip,port);
		}
		catch (IOException e){
			System.out.println(e);
			return;
		}


		do{
			int menuAnswer=main.menu();
			switch(menuAnswer)
			{
			case 1:
				try {
					String kmeans=main.learningFromFile();
					System.out.println(kmeans);
				}
				catch (SocketException e) {
					System.out.println(e);
					return;
				}
				catch (FileNotFoundException e) {
					System.out.println(e);
					return ;
				} catch (IOException e) {
					System.out.println(e);
					return;
				} catch (ClassNotFoundException e) {
					System.out.println(e);
					return;
				}
				catch (ServerException e) {
					System.out.println(e.getMessage());
				}
				break;
			case 2: // learning from db

				while(true){
					try{
						main.storeTableFromDb();
						break; //esce fuori dal while
					}

					catch (SocketException e) {
						System.out.println(e);
						return;
					}
					catch (FileNotFoundException e) {
						System.out.println(e);
						return;

					} catch (IOException e) {
						System.out.println(e);
						return;
					} catch (ClassNotFoundException e) {
						System.out.println(e);
						return;
					}
					catch (ServerException e) {
						System.out.println(e.getMessage());
					}
				} //end while [viene fuori dal while con un db (in alternativa il programma termina)

				char answer='y';//itera per learning al variare di k
				do{
					try
					{
						String clusterSet=main.learningFromDbTable();
						System.out.println(clusterSet);

						main.storeClusterInFile();

					}
					catch (SocketException e) {
						System.out.println(e);
						return;
					}
					catch (FileNotFoundException e) {
						System.out.println(e);
						return;
					} 
					catch (ClassNotFoundException e) {
						System.out.println(e);
						return;
					}catch (IOException e) {
						System.out.println(e);
						return;
					}
					catch (ServerException e) {
						System.out.println(e.getMessage());
					}
					System.out.print("Would you repeat?(y/n)");
					answer=Keyboard.readChar();
				}
				while(Character.toLowerCase(answer)=='y');
				break; //fine case 2
			default:
				System.out.println("Invalid option!");
			}

			System.out.print("would you choose a new operation from menu?(y/n)");
			if(Keyboard.readChar()!='y')
				break;
		}
		while(true);
	}
}



