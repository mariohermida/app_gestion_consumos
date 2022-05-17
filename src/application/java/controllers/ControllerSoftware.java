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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Class that manages all the events occurred in Principal.fxml
 */
public class ControllerSoftware {

	// Shortcut for opening advanced functionality
	private KeyCombination shortcut = new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN,
			KeyCombination.SHIFT_DOWN);

	@FXML
	private AnchorPane rootPane;

	@FXML
	private Label welcomeLabel;

	@FXML
	private Button entity1Button;

	@FXML
	private Button entity2Button;

	@FXML
	private Button entity3Button;

	@FXML
	private Button avanzadoButton;

	// User that has just logged into the system
	private Usuario_interno usuarioSession;

	// Object for retrieving the values stored in file
	private Properties properties = new Properties();

	@FXML
	public void initialize() {
		// Title name for buttons are retrieved from file
		try {
			properties.load(new FileInputStream(new File("src/application/resources/conf/titles.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		welcomeLabel.setText(properties.getProperty("welcome"));
		entity1Button.setText(properties.getProperty("entity1"));
		entity2Button.setText(properties.getProperty("entity2"));
		entity3Button.setText(properties.getProperty("entity3"));
	}

	@FXML
	void entity1(ActionEvent event) {
		openNewWindow("Entity1");
	}

	@FXML
	void entity2(ActionEvent event) {
		openNewWindow("Entity2");
	}

	@FXML
	void entity3(ActionEvent event) {
		openNewWindow("Entity3");
	}

	@FXML
	void avanzado(KeyEvent event) {
		if (shortcut.match(event)) {
			openNewWindow("LoginAvanzado");
		}
	}

	@FXML
	void auditTrail(ActionEvent event) {
		openNewWindow("AuditTrail");
	}

	@FXML
	void atras(ActionEvent event) {
		openNewWindow("Principal");
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
		FXMLLoader loader = null;
		try {
			if (usuarioSession.getPermiso() == 0 || usuarioSession.getPermiso() == 1) {
				loader = new FXMLLoader(getClass().getResource("/application/resources/view/" + fileName + ".fxml"));
				pane = loader.load();
			} else if (usuarioSession.getPermiso() == 2) {
				if (fileName.equals("Entity2")) {
					loader = new FXMLLoader(getClass().getResource("/application/resources/view/Entity2.fxml"));
					pane = loader.load();
				} else if (fileName.equals("Entity3")) {
					loader = new FXMLLoader(getClass().getResource("/application/resources/view/Entity3.fxml"));
					pane = loader.load();
				} else {
					pane = null;
				}
			} else if (usuarioSession.getPermiso() == 3) {
				if (fileName.equals("Entity2")) {
					loader = new FXMLLoader(getClass().getResource("/application/resources/view/Entity2.fxml"));
					pane = loader.load();
				} else {
					pane = null;
				}
			} else {
				pane = null;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (pane == null) {
			showError("Usted no tiene permisos para acceder a este contenido.");
		} else { // pane != null
			// According to the next GUI, a different controller is created for setting the
			// logged user
			switch (fileName) {
			case "Entity1":
				ControllerEntity1 controllerEntity1 = loader.getController();
				controllerEntity1.setUsuarioSession(usuarioSession);
				break;
			case "Entity2":
				ControllerEntity2 controllerEntity2 = loader.getController();
				controllerEntity2.setUsuarioSession(usuarioSession);
				break;
			case "Entity3":
				ControllerEntity3 controllerEntity3 = loader.getController();
				controllerEntity3.setUsuarioSession(usuarioSession);
				break;
			case "LoginAvanzado":
				ControllerLoginAvanzado controllerLoginAvanzado = loader.getController();
				controllerLoginAvanzado.setUsuarioSession(usuarioSession);
				break;
			case "AuditTrail":
				ControllerAuditTrail controllerAuditTrail = loader.getController();
				controllerAuditTrail.setUsuarioSession(usuarioSession);
				break;
			case "Principal":
				ControllerPrincipal controllerPrincipal = loader.getController();
				controllerPrincipal.setUsuarioSession(usuarioSession);
				break;
			}

			rootPane.getChildren().setAll(pane);
		}
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}