package application.java.controllers;

import java.io.IOException;

import application.java.model.Usuario_interno;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

/**
 * Class that manages all the events occurred in Principal.fxml
 */
public class ControllerPrincipal {

	@FXML
	AnchorPane rootPane;

	// User that has just logged into the system
	private Usuario_interno usuarioSession;

	@FXML
	void administracionUsuarios(ActionEvent event) {
		openNewWindow("Administracion");
	}

	@FXML
	void software(ActionEvent event) {
		openNewWindow("Software");
	}

	public void setUsuarioSession(Usuario_interno usuarioSession) {
		this.usuarioSession = usuarioSession;
	}

	/**
	 * Shows a new view according to the .fxml file called fileName
	 * 
	 * @param fileName
	 */
	private void openNewWindow(String fileName) {
		AnchorPane pane = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/view/" + fileName + ".fxml"));
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// If the GUI is redirected to Software window, the logged user will be sent
		if (fileName.equals("Software")) {
			ControllerSoftware controller = loader.getController();
			controller.setUsuarioSession(usuarioSession);
		} else if (fileName.equals("Administracion")) {
			ControllerAdministracion controller = loader.getController();
			controller.setUsuarioSession(usuarioSession);
		}
		rootPane.getChildren().setAll(pane);
	}

}