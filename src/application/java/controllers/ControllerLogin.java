package application.java.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * Class that manages all the events occurred in Login.fxml
 */
public class ControllerLogin {

	@FXML
	private TextField textFieldUsuario;

	@FXML
	private PasswordField passwordFieldContrasenya;

	@FXML
	void acceder(ActionEvent event) {
		if (textFieldUsuario.getText().equals("root") && passwordFieldContrasenya.getText().equals("sica")) {
			openNewWindow("Importacion", "Importación de datos");
		} else {
			showError("Credenciales incorrectas");
		}
	}

	/**
	 * Shows a new window according to the .fxml file called fileName
	 * 
	 * @param fileName
	 * @param titleName
	 */
	private void openNewWindow(String fileName, String titleName) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/application/resources/view/" + fileName + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set stage settings
		Stage mystage = new Stage();
		mystage.setTitle(titleName);
		mystage.setScene(new Scene(root));
		mystage.setResizable(false);
		mystage.show();
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}