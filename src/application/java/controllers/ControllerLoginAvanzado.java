package application.java.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import application.java.model.Usuario_interno;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;

/**
 * Class that manages all the events occurred in LoginAvanzado.fxml
 */
public class ControllerLoginAvanzado {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField textFieldUsuario;

	@FXML
	private PasswordField passwordFieldContrasenya;

	// User that has just logged into the system
	private Usuario_interno usuarioSession;

	@FXML
	void acceder(ActionEvent event) {
		// Credentials are retrieved from file
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(new File("C:\\Users\\SIC-LN-34\\Desktop\\M\\credentials.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (textFieldUsuario.getText().equals(properties.getProperty("user"))
				&& passwordFieldContrasenya.getText().equals(properties.getProperty("pass"))) {
			openNewWindow("Importacion");
		} else {
			showError("Credenciales incorrectas");
		}
	}

	@FXML
	void atras(ActionEvent event) {
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
		if (fileName.equals("Software")) {
			ControllerSoftware controller = loader.getController();
			controller.setUsuarioSession(usuarioSession);
		} else if (fileName.equals("Importacion")) {
			ControllerImportacion controller = loader.getController();
			controller.setUsuarioSession(usuarioSession);
		}
		rootPane.getChildren().setAll(pane);
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}