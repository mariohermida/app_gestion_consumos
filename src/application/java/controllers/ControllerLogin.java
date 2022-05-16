package application.java.controllers;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import application.java.dao.Usuario_internoDaoImpl;
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
 * Class that manages all the events occurred in Login.fxml
 */
public class ControllerLogin {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField textFieldUsuario;

	@FXML
	private PasswordField passwordFieldContrasenya;

	@FXML
	void acceder(ActionEvent event) {
		Usuario_interno usuario = null;

		Usuario_internoDaoImpl usuario_internoDao = new Usuario_internoDaoImpl();
		usuario = usuario_internoDao.getUsuario(textFieldUsuario.getText());

		// As passwords are stored using SHA-256 algorithm it is necessary to revert
		// that step
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		String hash = toHexString(md.digest(passwordFieldContrasenya.getText().getBytes(StandardCharsets.UTF_8)));

		if (usuario != null && hash.equals(usuario.getClave())) {
			openNewWindow("Principal");
		} else {
			showError("Credenciales incorrectas");
		}
	}

	/**
	 * Convert byte array into hexadecimal value
	 * 
	 * @param hash
	 * @return
	 */
	private static String toHexString(byte[] hash) {
		// Convert byte array into signum representation
		BigInteger number = new BigInteger(1, hash);

		// Convert message digest into hex value
		StringBuilder hexString = new StringBuilder(number.toString(16));

		// Pad with leading zeros
		while (hexString.length() < 64) {
			hexString.insert(0, '0');
		}

		return hexString.toString();
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

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}