package client;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

public class ClientApp extends Application {

	//Label
	Label labelSys;
	Label labelAddress;
	Label labelPort;
	Label labelTable;
	Label labelRadius;
	Label labelChoice;
	Label labelHello;
	Label labelSave;
	Label labelResult;
	Label labelDate;

	//Button
	Button buttonClear;
	Button buttonCompute;
	Button buttonRepeat;
	Button buttonInfo;
	Button buttonSave;
	Button buttonImg;

	//Text
	TextField textAddress;
	TextField textPort;
	TextField textTable;
	TextField textRadius;
	TextArea textArea;

	//ChoiceBox
	ChoiceBox<Object> textChoice;

	//Image
	Image img;

	//Locale
	Locale enLocale;
	Locale itLocale;
	Locale currentLocale;

	//ResourceBundle
	ResourceBundle messages;


	@Override
	public void start(Stage primaryStage) throws IOException {

		enLocale = new Locale("en","EN");
		itLocale = new Locale("it","IT");

		currentLocale = new Locale(new String(), new String());

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Language Selection");
		alert.setHeaderText("Select a language, please");
		alert.setContentText("Choose your option.");

		ButtonType buttonTypeOne = new ButtonType("Italiano/Italian");
		ButtonType buttonTypeTwo = new ButtonType("Inglese/English");
		ButtonType buttonTypeCancel = new ButtonType("Esci/Exit", ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

		Optional<ButtonType> result2 = alert.showAndWait();
		if (result2.get() == buttonTypeOne){
			currentLocale =itLocale;
		} else if (result2.get() == buttonTypeTwo) {
			currentLocale = enLocale;
		} else {
			System.exit(0);
		}

		messages = ResourceBundle.getBundle("Properties.MessagesBundle", currentLocale);

		//Labels
		labelSys = new Label(System.getProperty("os.arch") + "/" + System.getProperty("os.name"));
		labelAddress = new Label(messages.getString("address"));
		labelPort = new Label(messages.getString("port"));
		labelTable = new Label(messages.getString("table"));
		labelRadius = new Label(messages.getString("radius"));
		labelChoice = new Label(messages.getString("choice"));
		labelHello = new Label();
		labelSave = new Label();
		labelResult = new Label();
		labelDate = new Label();

		//Text
		textAddress = new TextField();
		textPort = new TextField();
		textTable = new TextField();
		textRadius = new TextField();
		textChoice = new ChoiceBox<Object>(FXCollections.observableArrayList(messages.getString("first"), new Separator(), messages.getString("second")));
		textArea = new TextArea();
		textArea.setEditable(false);

		//Button
		buttonClear = new Button(messages.getString("clear"));
		buttonCompute = new Button(messages.getString("compute"));
		buttonRepeat = new Button(messages.getString("repeat"));	
		buttonInfo = new Button(messages.getString("info"));
		buttonSave = new Button(messages.getString("save"));
		buttonImg = new Button(messages.getString("img"));
		buttonCompute.setId("button");
		buttonRepeat.setId("button");
		buttonClear.setId("button");
		buttonInfo.setId("button");
		buttonImg.setId("button");
		buttonSave.setId("button");
		buttonSave.setVisible(false);

		//HBox
		HBox buttonbox = new HBox();
		buttonbox.setSpacing(5);
		HBox.setHgrow(buttonClear, Priority.ALWAYS);
		buttonClear.setMaxWidth(Double.MAX_VALUE);
		buttonbox.getChildren().addAll(buttonClear);

		//HBox1
		HBox buttonbox1 = new HBox();
		buttonbox1.setSpacing(5);
		HBox.setHgrow(buttonCompute, Priority.ALWAYS);
		HBox.setHgrow(buttonRepeat, Priority.ALWAYS);
		buttonCompute.setMaxWidth(Double.MAX_VALUE);
		buttonRepeat.setMaxWidth(Double.MAX_VALUE);
		buttonbox1.getChildren().addAll(buttonCompute, buttonRepeat);

		//buttonCompute
		buttonCompute.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				MainTest runnableClient = null;
				try {
					runnableClient = new MainTest(textAddress.getText(), 
							Integer.parseInt(textPort.getText()));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				new Thread(runnableClient).start();
			}
		});

		//buttonClear
		buttonClear.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent event) {
				textPort.setText("");
				textAddress.setText("");
			}
		});

		//buttonRepeat
		buttonRepeat.setOnAction(new EventHandler<ActionEvent>() {


			@Override
			public void handle(ActionEvent event) {
				textTable.setText("");
				textRadius.setText("");
			}
		});

		buttonInfo.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText(messages.getString("info_header"));
				alert.setContentText(messages.getString("info_text"));
				alert.showAndWait();
			}
		});

		Group root = new Group();
		Scene scene = new Scene(root, 600, 250, Color.WHITE);
		scene.getStylesheets().add("CSS/QTAppClient.css");

		TabPane tabPane = new TabPane();
		tabPane.setSide(Side.TOP);
		tabPane.setId("tabPane");

		BorderPane borderPane = new BorderPane();
		borderPane.setId("borderPane"); 
		Image img = new Image("Images/icona-omino-png-1.png");
		ImageView imgView = new ImageView(img);
		Circle circle = new Circle(0, 0, 40);
		ImagePattern pattern = new ImagePattern(new Image("Images/icona-omino-png-1.png", 280, 180, false, false));
		circle.setFill(pattern);
		circle.setEffect(new InnerShadow(10, Color.BLACK));  // Shadow

		//Clock
		Platform.runLater(()->{
			DateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss" );
			final Timeline timeline = new Timeline();
			timeline.getKeyFrames().addAll(
					new KeyFrame(Duration.ZERO, e -> {
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
						labelDate.setText(LocalDateTime.now().format(formatter));
					}), new KeyFrame(Duration.seconds(1))
					);
			timeline.setCycleCount( Animation.INDEFINITE );
			timeline.play();
		});

		//VBox
		VBox vbox = new VBox();
		vbox.getChildren().addAll(circle, buttonImg, labelHello, buttonInfo);
		vbox.setAlignment(Pos.TOP_CENTER);
		vbox.setTranslateX(7);
		vbox.setTranslateY(20);
		vbox.setPadding(new Insets(5, 5, 5, 5));
		vbox.setSpacing(20);
		borderPane.setLeft(vbox);

		//buttonImg
		buttonImg.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("PNG Files", "*.png")
						,new FileChooser.ExtensionFilter("JPEG Files", "*.jpeg")
						);

				fileChooser.setTitle("Open Resource File");
				File selectedFile = fileChooser.showOpenDialog(primaryStage);
				if(selectedFile!=null) {
					Image image = new Image(selectedFile.toURI().toString());
					ImagePattern pattern = new ImagePattern(image);
					circle.setFill(pattern);
				}

			}
		});

		//Tab
		Tab tab = new Tab();
		tab.setId("tab");
		tab.setText(messages.getString("connect"));
		tab.setClosable(false);
		VBox hbox = new VBox();
		hbox.getChildren().addAll(labelDate, labelSys, labelAddress, textAddress, labelPort, textPort, buttonbox);
		hbox.setAlignment(Pos.TOP_LEFT);
		hbox.setPadding(new Insets(5, 5, 5, 5));
		hbox.setSpacing(5);
		tab.setContent(hbox);
		tabPane.getTabs().add(tab);

		//Tab1
		Tab tab1 = new Tab();
		tab1.setId("tab");
		tab1.setClosable(false);
		tab1.setText(messages.getString("options"));
		VBox hbox1 = new VBox();
		hbox1.getChildren().addAll(labelChoice, textChoice, labelTable, textTable, labelRadius, textRadius, buttonbox1);
		hbox1.setAlignment(Pos.TOP_LEFT);
		hbox1.setPadding(new Insets(5, 5, 5, 5));
		hbox1.setSpacing(5);
		tab1.setContent(hbox1);
		tabPane.getTabs().add(tab1);

		//Tab2
		Tab tab2 = new Tab();
		tab2.setId("tab");
		tab2.setClosable(false);
		tab2.setText(messages.getString("result"));
		VBox hbox2 = new VBox();
		hbox2.getChildren().addAll(labelResult, textArea, buttonSave, labelSave);
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setPadding(new Insets(5, 5, 5, 5));
		hbox2.setSpacing(5);
		tab2.setContent(hbox2);
		tabPane.getTabs().add(tab2);


		borderPane.prefHeightProperty().bind(scene.heightProperty());
		borderPane.prefWidthProperty().bind(scene.widthProperty());
		borderPane.setRight(tabPane);
		root.getChildren().add(borderPane);
		primaryStage.setTitle("QTMiner Client");
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);

		///INIZIO DEFINIZIONE FINESTRA DI LOGIN///

		// Crea il dialog personalizzato
		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.setTitle("QTMiner Client Login");
		dialog.setHeaderText(messages.getString("login_header"));

		// Inizializza il tipo dei bottoni
		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		ButtonType cancelButtonType = new ButtonType(messages.getString("exit"), ButtonData.CANCEL_CLOSE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, cancelButtonType);

		// Crea i labels e i fields relativi all'username e alla password
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));
		grid.setId("borderPane");

		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		// Attiva/Disattiva il bottone di Login a seconda dell'inserimento di un username
		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(true);

		// Svolge alcune verifiche
		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		// Richiede di default il focus sul field dell'username
		Platform.runLater(() -> username.requestFocus());

		//Converte il risultato in una coppia username-password quando il bottone di Login viene cliccato
		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		if(!result.isPresent()) {
			System.exit(0);
		}else {
			labelHello.setText(messages.getString("hello") + username.getText());
			primaryStage.show();
		}
		///FINE DEFINIZIONE FINESTRA DI LOGIN///
	}

	public static void main(String[] args) {
		launch(args);
	}

	public class MainTest implements Runnable{

		private ObjectOutputStream out;
		private ObjectInputStream in;
		private MainTest main;
		private String tabName;
		private double r;

		public MainTest(String ip, int port) throws IOException{

			InetAddress addr=null;

			try {
				addr = InetAddress.getByName(ip); //ip

			}catch(IOException e) {
				Platform.runLater(() -> {
					Alert dialog = new Alert(AlertType.ERROR, "Error", ButtonType.OK);
					dialog.setHeaderText("UnknowHost Exception");
					dialog.setContentText(messages.getString("host_exception"));
					dialog.show();
				});
				return;
			}

			try {
				Socket socket = new Socket(addr, port); //Port

				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());

			}catch(IOException e) {
				Platform.runLater(() -> {
					Alert dialog = new Alert(AlertType.ERROR, "Error", ButtonType.OK);
					dialog.setHeaderText("Connection Exception");
					dialog.setContentText(messages.getString("conn_exception") + String.valueOf(port));
					dialog.show();
				});
				return;
			}

		}

		private int menu(){
			
			int answer;

			answer=Integer.parseInt(textChoice.getSelectionModel().getSelectedItem().toString().substring(0, 1));

			return answer;

		}

		private String learningFromFile() throws SocketException,ServerException,IOException,ClassNotFoundException{
			out.writeObject(3);

			tabName=textTable.getText();
			out.writeObject(tabName);
			r=1.0;

			if(!textRadius.getText().isEmpty()) {
				r=Double.parseDouble(textRadius.getText());
			}else {
				Platform.runLater(() -> {
					Alert dialog = new Alert(AlertType.ERROR, "Error", ButtonType.OK);
					dialog.setHeaderText(messages.getString("title_radius"));
					dialog.setContentText(messages.getString("content_radius"));
					dialog.show();
				});
			}

			out.writeObject(r);
			String result = (String)in.readObject();
			if(result.equals("OK")) {
				Platform.runLater(()->labelResult.setText(messages.getString("result_label") + tabName + String.valueOf(r)));
				return (String)in.readObject();
			}else { 
				throw new ServerException(result);
			}

		}
		
		private void storeTableFromDb() throws SocketException,ServerException,IOException,ClassNotFoundException{
			out.writeObject(0);
			tabName=textTable.getText();
			out.writeObject(tabName);
			String result = (String)in.readObject();
			if(!result.equals("OK"))
				throw new ServerException(result);

		}
	
		private String learningFromDbTable() throws SocketException,ServerException,IOException,ClassNotFoundException{
			out.writeObject(1);
			r=1.0;

			if(!textRadius.getText().isEmpty()) {
				r=Double.parseDouble(textRadius.getText());
			}else {
				Platform.runLater(() -> {
					Alert dialog = new Alert(AlertType.ERROR, "Error", ButtonType.OK);
					dialog.setHeaderText(messages.getString("title_radius"));
					dialog.setContentText(messages.getString("content_radius"));
					dialog.show();
				});
			}

			if(r<=0) {
				Platform.runLater(() -> {
					Alert dialog = new Alert(AlertType.ERROR, "Error", ButtonType.OK);
					dialog.setHeaderText(messages.getString("title_radius"));
					dialog.setContentText(messages.getString("positive_radius"));
					dialog.show();
				});
			}else{
				out.writeObject(r);
			}

			String result = (String)in.readObject();
			if(result.equals("OK")){
				Platform.runLater(()->labelResult.setText(messages.getString("result_radius") + String.valueOf(r)));
				return (String)in.readObject();
			}else { 
				throw new ServerException(result);
			}
		}

		private void storeClusterInFile() throws SocketException,ServerException,IOException,ClassNotFoundException{
			out.writeObject(2);

			String result = (String)in.readObject();
			if(result.equals(messages.getString("file_error"))) {
				throw new ServerException(result);
			}else if(result.equals("NO")) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle(messages.getString("file_title"));
				alert.setHeaderText(messages.getString("file_header"));
				alert.setContentText(messages.getString("file_over"));

				Optional<ButtonType> result1 = alert.showAndWait();
				if (result1.get() == ButtonType.OK){
					out.writeObject(4);

					String result3 = (String)in.readObject();
					if(result3.equals("Errore! - Salvataggio del file non riuscito!")) {
						throw new ServerException(messages.getString("file_error"));
					}else{
						labelSave.setText(messages.getString("file_result"));
						alert.close();
					}

				}

			}else if(result.equals("OK")) {
				labelSave.setText(messages.getString("file_save"));
			}
		}

		@Override
		public void run() {

			String ip=textAddress.getText();
			int port=new Integer(textPort.getText()).intValue();
			main=null;
			buttonSave.setVisible(false);
			textArea.setText("");
			labelResult.setText("");
			labelSave.setText("");
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
						textArea.setText(kmeans);
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
						return;
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
							return;
						}
					} //end while [viene fuori dal while con un db (in alternativa il programma termina)

					char answer='y';//itera per learning al variare di k
					do{
						try
						{
							String clusterSet=main.learningFromDbTable();
							textArea.setText(clusterSet);
							buttonSave.setVisible(true);
							buttonSave.setOnAction(new EventHandler<ActionEvent>() {

								public void handle(ActionEvent event) {
									try {

										main.storeClusterInFile();
									} catch (ClassNotFoundException | ServerException | IOException e) {
										e.printStackTrace();
									}
								}
							});
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
							return;
						}
						answer='n';
					}
					while(Character.toLowerCase(answer)=='y');
					break; //fine case 2
				}
				break; //si esce dal do
			}while(true);
		}
	}
}



