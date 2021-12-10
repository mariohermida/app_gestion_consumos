package application.java.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class that manages all the events occurred in Principal.fxml
 */
public class ControllerPrincipal {

	@FXML
	void aplicacionesButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Aplicaciones.");
		openNewWindow("Aplicaciones", "Aplicaciones");
	}

	@FXML
	void usuariosButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Usuarios.");
		openNewWindow("Usuarios", "Usuarios");
	}

	@FXML
	void consumosButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Consumos.");
		openNewWindow("Consumos", "Consumos de usuarios");
	}

	@FXML
	void avanzadoButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Avanzado.");
	}

	@FXML
	void auditTrailButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Audit Trail.");
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

}
