package application.java.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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