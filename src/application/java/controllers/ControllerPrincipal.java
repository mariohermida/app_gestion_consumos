package application.java.controllers;

import java.io.IOException;

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

	@FXML
	void administracionUsuarios(ActionEvent event) {
		openNewWindow("Administracion");
	}

	@FXML
	void software(ActionEvent event) {
		openNewWindow("Software");
	}

	/**
	 * Shows a new view according to the .fxml file called fileName
	 * 
	 * @param fileName
	 */
	private void openNewWindow(String fileName) {
		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource("/application/resources/view/" + fileName + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		rootPane.getChildren().setAll(pane);
	}

}