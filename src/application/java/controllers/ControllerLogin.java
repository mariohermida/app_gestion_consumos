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

	// User that has just logged into the system
	private Usuario_interno usuario_session;

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
			usuario_session = usuario;
			if (usuario.getPermiso() == 0 || usuario.getPermiso() == 1) {
				openNewWindow("Principal");
			} else {
				openNewWindow("Software");
			}
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
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/application/resources/view/" + fileName + ".fxml"));
			pane = loader.load();
			// If the GUI is redirected to Software window, the logged user will be sent
			if (fileName.equals("Software")) {
				ControllerSoftware controllerSoftware = loader.getController();
				controllerSoftware.setUsuarioSession(usuario_session);
			} else if (fileName.equals("Principal")) {
				ControllerPrincipal controllerPrincipal = loader.getController();
				controllerPrincipal.setUsuarioSession(usuario_session);
			}
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