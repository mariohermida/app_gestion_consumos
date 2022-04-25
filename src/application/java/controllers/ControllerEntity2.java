package application.java.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Class that manages all the events occurred in Usuarios.fxml
 */
public class ControllerEntity2 {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private Label label1;

	@FXML
	private Label label2;

	@FXML
	private Label label3;

	@FXML
	private Label label4;

	@FXML
	private Label label5;

	@FXML
	private Label label6;

	@FXML
	private TextField textField1;

	@FXML
	private TextField textField2;

	@FXML
	private TextField textField3;

	@FXML
	private TextField textField4;

	@FXML
	private TextField textField5;

	@FXML
	private TextField textField6;

	@FXML
	private TableColumn<Usuario, String> tableColumn1;

	@FXML
	private TableColumn<Usuario, String> tableColumn2;

	@FXML
	private TableColumn<Usuario, String> tableColumn3;

	@FXML
	private TableColumn<Usuario, String> tableColumn4;

	@FXML
	private TableColumn<Usuario, String> tableColumn5;

	@FXML
	private TableColumn<Usuario, String> tableColumn6;

	@FXML
	private TableView<Usuario> tableViewUsuarios;

	private Usuario selectedUsuario; // Current usuario

	// Object for retrieving the values stored in file
	private Properties properties = new Properties();

	@FXML
	public void initialize() {
		// Label texts are retrieved from file
		try {
			properties.load(new FileInputStream(new File("src/application/resources/conf/titles.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Texts are established
		label1.setText(properties.getProperty("entity2_title1"));
		label2.setText(properties.getProperty("entity2_title2"));
		label3.setText(properties.getProperty("entity2_title3"));
		label4.setText(properties.getProperty("entity2_title4"));
		label5.setText(properties.getProperty("entity2_title5"));
		label6.setText(properties.getProperty("entity2_title6"));

		// Table column texts are set
		tableColumn1.setText(properties.getProperty("entity2_title1"));
		tableColumn2.setText(properties.getProperty("entity2_title2"));
		tableColumn3.setText(properties.getProperty("entity2_title3"));
		tableColumn4.setText(properties.getProperty("entity2_title4"));
		tableColumn5.setText(properties.getProperty("entity2_title5"));
		tableColumn6.setText(properties.getProperty("entity2_title6"));

		// tableViewUsuarios setup
		// Columns values are assigned to the attributes within Usuario class
		tableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumn2.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		tableColumn3.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
		tableColumn4.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumn5.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		tableColumn6.setCellValueFactory(new PropertyValueFactory<>("direccion"));

		// Initially, all existing usuarios are shown
		updateTableViewUsuarios(null);
	}

	@FXML
	void buscar(ActionEvent event) {
		List<Usuario> usuarios = new ArrayList<>();
		UsuarioDao usuarioDao = new UsuarioDaoImpl();
		usuarios = usuarioDao.getUsuarios(textField1.getText(), textField2.getText(), textField3.getText(),
				textField4.getText(), textField5.getText(), textField6.getText());

		updateTableViewUsuarios(usuarios);
	}

	@FXML
	void modificar(ActionEvent event) {
		boolean error = false;

		if (textField1.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
			error = true;
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			if (!usuarioDao.updateUsuario(selectedUsuario.getId(),
					new Usuario(textField1.getText(), textField2.getText(), textField3.getText(), textField4.getText(),
							textField5.getText(), textField6.getText()))) {
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

		if (textField1.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
			error = true;
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			if (!usuarioDao.insertUsuario(new Usuario(textField1.getText(), textField2.getText(), textField3.getText(),
					textField4.getText(), textField5.getText(), textField6.getText()))) {
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

		if (textField1.getText().isBlank() && textField2.getText().isBlank() && textField3.getText().isBlank()
				&& textField4.getText().isBlank() && textField5.getText().isBlank() && textField6.getText().isBlank()) {
			showError("Los seis campos no pueden estar vacíos.");
			error = true;
		} else {
			UsuarioDao usuarioDao = new UsuarioDaoImpl();
			if (!usuarioDao.deleteUsuarios(textField1.getText(), textField2.getText(), textField3.getText(),
					textField4.getText(), textField5.getText(), textField6.getText())) {
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
			textField1.setText(usuario.getId());
			textField2.setText(usuario.getNombre());
			textField3.setText(usuario.getApellidos());
			textField4.setText(usuario.getEmail());
			textField5.setText(usuario.getTelefono());
			textField6.setText(usuario.getDireccion());

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
		textField4.setText("");
		textField5.setText("");
		textField6.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}
}