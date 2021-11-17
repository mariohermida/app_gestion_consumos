package application.java.controllers;

import java.util.ArrayList;
import java.util.List;
import application.java.dao.UsuarioDao;
import application.java.dao.UsuarioDaoImpl;
import application.java.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Class that manages all the events occurred in Usuarios.fxml
 */
public class ControllerUsuarios {
	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldNuevoId;

	@FXML
	private TextField textFieldNombre;

	@FXML
	private TextField textFieldApellidos;

	@FXML
	private TextField textFieldEmail;

	@FXML
	private TextField textFieldTelefono;

	@FXML
	private TextField textFieldDireccion;

	@FXML
	private TableColumn<Usuario, String> tableColumnId;

	@FXML
	private TableColumn<Usuario, String> tableColumnNombre;

	@FXML
	private TableColumn<Usuario, String> tableColumnApellidos;

	@FXML
	private TableColumn<Usuario, Byte> tableColumnEmail;

	@FXML
	private TableColumn<Usuario, Byte> tableColumnTelefono;

	@FXML
	private TableColumn<Usuario, Byte> tableColumnDireccion;

	@FXML
	private TableView<Usuario> tableViewUsuarios;

	@FXML
	public void initialize() {
		// tableViewUsuarios setup
		// Columns values are assigned to the attributes within Usuario class
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		tableColumnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		tableColumnDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
	}

	@FXML
	void mostrarTodo(ActionEvent event) {
		System.out.println("Se ha presionado el botón: mostrarTodo.");
		updateTableViewUsuarios(null);
	}

	@FXML
	void buscar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: buscar.");

		List<Usuario> usuarios = new ArrayList<>();
		if (textFieldId.getText().isBlank() && textFieldNombre.getText().isBlank()
				&& textFieldTelefono.getText().isBlank()) {
			showError("Al menos uno de los siguientes campos ha de no estar vacío: ID, Nombre y Teléfono.");
		} else {
			try {
				UsuarioDao usuarioDao = new UsuarioDaoImpl();
				usuarios = usuarioDao.getUsuarios(textFieldId.getText(), textFieldNombre.getText(), textFieldTelefono.getText());
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
			}
		}

		updateTableViewUsuarios(usuarios);
	}

	@FXML
	void anyadir(ActionEvent event) {
		System.out.println("Se ha presionado el botón: anyadir.");

		if (textFieldId.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			if (!usuarioDao.insertUsuario(
					new Usuario(textFieldId.getText(), textFieldNombre.getText(), textFieldApellidos.getText(),
							textFieldEmail.getText(), textFieldTelefono.getText(), textFieldDireccion.getText()))) {
				showError("Se produjo un error a la hora de añadir el usuario.");
			}
		}

		// After the insertion the list is updated
		updateTableViewUsuarios(null);
	}

	@FXML
	void eliminar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: eliminar.");

		if (textFieldId.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			if (!usuarioDao.deleteUsuario(textFieldId.getText())) {
				showError("Se produjo un error a la hora de eliminar el usuario.");
			}
		}

		// After the deletion the list is updated
		updateTableViewUsuarios(null);
	}

	@FXML
	void modificar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: modificar.");

		if (textFieldId.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			String id = textFieldId.getText();
			if (!textFieldNuevoId.getText().isBlank()) {
				id = textFieldNuevoId.getText();
			}
			if (!usuarioDao.updateUsuario(textFieldId.getText(),
					new Usuario(id, textFieldNombre.getText(), textFieldApellidos.getText(), textFieldEmail.getText(),
							textFieldTelefono.getText(), textFieldDireccion.getText()))) {
				showError("Se produjo un error a la hora de modificar el usuario.");
			}
		}

		// After the insertion the list is updated
		updateTableViewUsuarios(null);
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

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}
}
