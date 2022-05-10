package application.java.controllers;

import java.io.IOException;
import java.util.List;
import application.java.dao.UsuarioDao;
import application.java.dao.UsuarioDaoImpl;
import application.java.model.Usuario;
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
	private TextField textField1;

	@FXML
	private TextField textField2;

	@FXML
	private TextField textField3;

	@FXML
	private TableColumn<Usuario, String> tableColumn1;

	@FXML
	private TableColumn<Usuario, String> tableColumn2;

	@FXML
	private TableColumn<Usuario, String> tableColumn3;

	@FXML
	private TableView<Usuario> tableViewUsuarios;

	private Usuario selectedUsuario; // Current usuario

	@FXML
	public void initialize() {
		// tableViewUsuarios setup
		// Columns values are assigned to the attributes within Usuario class
		tableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumn2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		tableColumn3.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

		// Initially, all existing usuarios are shown
		updateTableViewUsuarios(null);
	}

	@FXML
	void buscar(ActionEvent event) {
		System.out.println("buscar");
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
		Usuario usuario = tableViewUsuarios.getSelectionModel().getSelectedItem();

		if (usuario != null) {
			textField1.setText(usuario.getId());
			textField2.setText(usuario.getNombre());
			textField3.setText(usuario.getApellidos());

			selectedUsuario = usuario;
		}
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
	private void updateTableViewUsuarios(List<Usuario> listUsuarios) {
		UsuarioDao usuarioDao = new UsuarioDaoImpl();
		List<Usuario> usuarios;
		if (listUsuarios == null) { // Take all existing usuarios
			usuarios = usuarioDao.getAllUsuarios();
		} else { // Show the ones given
			usuarios = listUsuarios;
		}

		// List is converted into ObservableList type
		ObservableList<Usuario> observableList = FXCollections.observableArrayList();
		for (Usuario usuario : usuarios) {
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
		textField1.setText("");
		textField2.setText("");
		textField3.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}
}