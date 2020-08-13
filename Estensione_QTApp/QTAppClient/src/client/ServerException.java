package client;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * ServerException è una classe che modella un'eccezione generata dal server e che si è propagata fino al client.
 * @author pa_pe
 *
 */
public class ServerException extends Exception {
	ServerException(String result){
		Platform.runLater(() -> {
	        Alert dialog = new Alert(AlertType.ERROR, "Error", ButtonType.OK);
	        dialog.setHeaderText("ServerException");
	        dialog.setContentText(result);
	        dialog.show();
	    });
	}
}
