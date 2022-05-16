package application.java.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import application.java.dao.UsuarioDao;
import application.java.dao.UsuarioDaoImpl;
import application.java.dao.Usuario_internoDao;
import application.java.dao.Usuario_internoDaoImpl;
import application.java.model.Usuario;
import application.java.model.Usuario_interno;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Class that manages all the events occurred in Usuarios.fxml
 */
public class ControllerAdministracion {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField textFieldUsuario;

	@FXML
	private TextField textFieldClave;

	@FXML
	private TextField textFieldPermiso;

	@FXML
	private TableColumn<Usuario_interno, String> tableColumn1;

	@FXML
	private TableColumn<Usuario_interno, String> tableColumn2;

	@FXML
	private TableColumn<Usuario_interno, Byte> tableColumn3;

	@FXML
	private TableView<Usuario_interno> tableViewUsuarios;

	private Usuario_interno selectedUsuario; // Current usuario

	@FXML
	public void initialize() {

		tableColumn1.setText("Usuario");
		tableColumn2.setText("Clave");
		tableColumn3.setText("Permiso");

		// tableViewUsuarios setup
		// Columns values are assigned to the attributes within Usuario_interno class
		tableColumn1.setCellValueFactory(new PropertyValueFactory<>("usuario"));
		tableColumn2.setCellValueFactory(new PropertyValueFactory<>("clave"));
		tableColumn3.setCellValueFactory(new PropertyValueFactory<>("permiso"));

		// Initially, all existing usuarios are shown
		updateTableViewUsuarios(null);
	}

	@FXML
	void buscar(ActionEvent event) {
		List<Usuario_interno> usuarios = new ArrayList<>();
		Usuario_internoDao usuario_internoDao = new Usuario_internoDaoImpl();
		usuarios = usuario_internoDao.getUsuarios(textFieldUsuario.getText(), getPermisoValue());

		updateTableViewUsuarios(usuarios);
	}

	@FXML
	void modificar(ActionEvent event) {
		System.out.println("modificar");
	}

	@FXML
	void anyadir(ActionEvent event) {
		System.out.println("anyadir");
	}

	@FXML
	void eliminar(ActionEvent event) {
		System.out.println("eliminar");
	}

	@FXML
	void selectUsuario(MouseEvent event) {
		Usuario_interno usuario = tableViewUsuarios.getSelectionModel().getSelectedItem();

		if (usuario != null) {
			textFieldUsuario.setText(usuario.getUsuario());
			textFieldClave.setText(usuario.getClave());
			textFieldPermiso.setText(Byte.toString(usuario.getPermiso()));

			selectedUsuario = usuario;
		}
	}

	private byte getPermisoValue() {
		if (!textFieldPermiso.getText().isBlank()) {
			return Byte.parseByte(textFieldPermiso.getText());
		}
		return Byte.MIN_VALUE;
	}

	@FXML
	void restablecerCampos(ActionEvent event) {
		setTextFieldsToBlank();
	}

	@FXML
	void atras(ActionEvent event) {
		AnchorPane pane = null;
		try {
			pane = FXMLLoader.load(getClass().getResource("/application/resources/view/Principal.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		rootPane.getChildren().setAll(pane);
	}

	/**
	 * Updates the tableViewUsuarios element. If listUsuarios is null it shows all
	 * the existing usuarios, otherwise shows the ones given in the parameter.
	 * 
	 * @param listUsuarios
	 */
	private void updateTableViewUsuarios(List<Usuario_interno> listUsuarios) {
		Usuario_internoDao usuario_internoDao = new Usuario_internoDaoImpl();
		List<Usuario_interno> usuarios;
		if (listUsuarios == null) { // Take all existing usuarios
			usuarios = usuario_internoDao.getAllUsuarios();
		} else { // Show the ones given
			usuarios = listUsuarios;
		}

		// List is converted into ObservableList type
		ObservableList<Usuario_interno> observableList = FXCollections.observableArrayList();
		for (Usuario_interno usuario : usuarios) {
			observableList.add(usuario);
		}

		// Show items
		tableViewUsuarios.setItems(observableList);
	}

	/**
	 * When an operation regarding the database is done all fields are set to blank
	 * in order not to overwrite values.
	 */
	private void setTextFieldsToBlank() {
		textFieldUsuario.setText("");
		textFieldClave.setText("");
		textFieldPermiso.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}
}