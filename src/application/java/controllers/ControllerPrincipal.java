package application.java.controllers;

import java.io.IOException;

import application.java.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Class that manages all the events occurred in Principal.fxml
 */
public class ControllerPrincipal {

	@FXML
	void aplicacionesButton(ActionEvent event) {
		openNewWindow("Aplicaciones", "Aplicaciones");
	}

	@FXML
	void usuariosButton(ActionEvent event) {
		openNewWindow("Usuarios", "Usuarios");
	}

	@FXML
	void consumosButton(ActionEvent event) {
		openNewWindow("Consumos", "Consumos de usuarios");
	}

	@FXML
	void avanzadoButton(ActionEvent event) {
		openNewWindow("Login", "Login - Importación");
	}

	@FXML
	void auditTrailButton(ActionEvent event) {
		openNewWindow("AuditTrail", "Audit Trail");
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

		// Establish the mainStage as the parent window
		mystage.initModality(Modality.WINDOW_MODAL);
		mystage.initOwner(Main.mainStage);

		mystage.show();
	}

}