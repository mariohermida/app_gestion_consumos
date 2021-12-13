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
import javafx.stage.Stage;

/**
 * Class that manages all the events occurred in Login.fxml
 */
public class ControllerLogin {

	@FXML
	private TextField textFieldUsuario;

	@FXML
	private TextField textFieldContrasenya;

	@FXML
	void acceder(ActionEvent event) {
		System.out.println("Se ha presionado el botón: acceder.");

		if (textFieldUsuario.getText().equals("user") && textFieldContrasenya.getText().equals("pass")) {
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
			root = FXMLLoader.load(getClass().getResource("../../resources/view/" + fileName + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set stage settings
		Scene scene = new Scene(root);
		Stage mystage = new Stage();
		mystage.setTitle(titleName);
		mystage.setScene(scene);
		mystage.setResizable(false);
		mystage.show();
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}
