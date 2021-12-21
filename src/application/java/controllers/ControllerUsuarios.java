package application.java.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Class that manages all the events occurred in Usuarios.fxml
 */
public class ControllerUsuarios {
	@FXML
	private TextField textFieldId;

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
	private TableColumn<Usuario, String> tableColumnEmail;

	@FXML
	private TableColumn<Usuario, String> tableColumnTelefono;

	@FXML
	private TableColumn<Usuario, String> tableColumnDireccion;

	@FXML
	private TableView<Usuario> tableViewUsuarios;

	private Usuario selectedUsuario; // Current usuario

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
	void buscar(ActionEvent event) {
		List<Usuario> usuarios = new ArrayList<>();
		UsuarioDao usuarioDao = new UsuarioDaoImpl();
		usuarios = usuarioDao.getUsuarios(textFieldId.getText(), textFieldNombre.getText(),
				textFieldApellidos.getText(), textFieldEmail.getText(), textFieldTelefono.getText(),
				textFieldDireccion.getText());

		updateTableViewUsuarios(usuarios);
	}

	@FXML
	void modificar(ActionEvent event) {
		boolean error = false;

		if (textFieldId.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
			error = true;
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			if (!usuarioDao.updateUsuario(selectedUsuario.getId(),
					new Usuario(textFieldId.getText(), textFieldNombre.getText(), textFieldApellidos.getText(),
							textFieldEmail.getText(), textFieldTelefono.getText(), textFieldDireccion.getText()))) {
				showError("Se produjo un error a la hora de modificar el usuario.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlank();
			// After the insertion the list is updated
			updateTableViewUsuarios(null);
		}
	}

	@FXML
	void anyadir(ActionEvent event) {
		boolean error = false;

		if (textFieldId.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
			error = true;
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			if (!usuarioDao.insertUsuario(
					new Usuario(textFieldId.getText(), textFieldNombre.getText(), textFieldApellidos.getText(),
							textFieldEmail.getText(), textFieldTelefono.getText(), textFieldDireccion.getText()))) {
				showError("Se produjo un error a la hora de añadir el usuario.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlank();
			// After the insertion the list is updated
			updateTableViewUsuarios(null);
		}
	}

	@FXML
	void eliminar(ActionEvent event) {
		boolean error = false;

		if (textFieldId.getText().isBlank() && textFieldNombre.getText().isBlank()
				&& textFieldApellidos.getText().isBlank() && textFieldEmail.getText().isBlank()
				&& textFieldTelefono.getText().isBlank() && textFieldDireccion.getText().isBlank()) {
			showError("Los seis campos no pueden estar vacíos.");
			error = true;
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			if (!usuarioDao.deleteUsuarios(textFieldId.getText(), textFieldNombre.getText(),
					textFieldApellidos.getText(), textFieldEmail.getText(), textFieldTelefono.getText(),
					textFieldDireccion.getText())) {
				showError("Se produjo un error a la hora de eliminar el usuario.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlank();
			// After the deletion the list is updated
			updateTableViewUsuarios(null);
		}
	}

	@FXML
	void exportar(ActionEvent event) {
		List<Usuario> usuarios = tableViewUsuarios.getItems();
		if (usuarios.isEmpty()) {
			showError("No se ha seleccionada nada para exportar.");
		} else {
			// Set .csv file location
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("CSV files (*.csv)", "*.csv"));
			File file = fc.showSaveDialog(null);

			if (file != null) {
				List<String[]> dataLines = new ArrayList<>(); // Formatted data to be stored
				// For every existing usuarios an array of strings is created
				// First line includes the headers of the stored information
				dataLines.add(new String[] { "ID_Usuario", "Nombre", "Apellidos", "Email", "Teléfono", "Dirección" });
				for (Usuario usuario : usuarios) {
					dataLines.add(new String[] { usuario.getId(), usuario.getNombre(), usuario.getApellidos(),
							usuario.getEmail(), usuario.getTelefono(), usuario.getDireccion() });
				}

				File csv = new File(file.getAbsolutePath());
				try (PrintWriter pw = new PrintWriter(csv)) {
					dataLines.stream().map(this::convertToCSV).forEach(pw::println);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private String convertToCSV(String[] data) {
		return Stream.of(data).collect(Collectors.joining(","));
	}

	@FXML
	void selectUsuario(MouseEvent event) {
		Usuario usuario = tableViewUsuarios.getSelectionModel().getSelectedItem();

		if (usuario != null) {
			textFieldId.setText(usuario.getId());
			textFieldNombre.setText(usuario.getNombre());
			textFieldApellidos.setText(usuario.getApellidos());
			textFieldEmail.setText(usuario.getEmail());
			textFieldTelefono.setText(usuario.getTelefono());
			textFieldDireccion.setText(usuario.getDireccion());

			selectedUsuario = usuario;
		}
	}

	@FXML
	void restablecerCampos(ActionEvent event) {
		setTextFieldsToBlank();
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
		textFieldId.setText("");
		textFieldNombre.setText("");
		textFieldApellidos.setText("");
		textFieldEmail.setText("");
		textFieldTelefono.setText("");
		textFieldDireccion.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}
}