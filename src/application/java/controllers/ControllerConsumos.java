package application.java.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import application.java.dao.AplicacionDaoImpl;
import application.java.dao.ConsumoDao;
import application.java.dao.ConsumoDaoImpl;
import application.java.dao.UsuarioDao;
import application.java.dao.UsuarioDaoImpl;
import application.java.model.Aplicacion;
import application.java.model.Consumo;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Class that manages all the events occurred in Consumos.fxml
 */
public class ControllerConsumos {

	// Fields for Consumo

	@FXML
	private ComboBox<String> comboBoxIdUsuario;

	@FXML
	private ComboBox<String> comboBoxIdAplicacion;

	@FXML
	private ComboBox<String> comboBoxMes;

	@FXML
	private TextField textFieldConsumoMin;

	@FXML
	private TextField textFieldConsumoMax;

	@FXML
	private TextField textFieldNuevoConsumo;

	@FXML
	private TableColumn<Consumo, String> tableColumnIdUsuarioConsumos;

	@FXML
	private TableColumn<Consumo, String> tableColumnIdAplicacion;

	@FXML
	private TableColumn<Consumo, String> tableColumnMes;

	@FXML
	private TableColumn<Consumo, Integer> tableColumnConsumo;

	@FXML
	private TableView<Consumo> tableViewConsumos;

	// Fields for Usuario

	@FXML
	private TextField textFieldIdUsuario;

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
	private TableColumn<Usuario, String> tableColumnIdUsuario;

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

	private List<String> currentFiltersInfo1; // Current search filters for: idUsuario, idApp and Mes
	private List<Integer> currentFiltersInfo2; // Current search filters for: min and max consumo

	private Consumo selectedConsumo; // Current consumo
	private Usuario selectedUsuario; // Current usuario

	@FXML
	public void initialize() {
		// tableViewConsumos setup
		// Columns values are assigned to attributes within Consumo class
		tableColumnIdUsuarioConsumos.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
		tableColumnIdAplicacion.setCellValueFactory(new PropertyValueFactory<>("idAplicacion"));
		tableColumnMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
		tableColumnConsumo.setCellValueFactory(new PropertyValueFactory<>("consumo"));

		refrescarCampos(null);

		// Possible mes values are loaded into the convenient comboBox
		comboBoxMes.setItems(FXCollections.observableArrayList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
				"Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));

		// tableViewUsuarios setup
		// Columns values are assigned to attributes within Usuario class
		tableColumnIdUsuario.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		tableColumnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		tableColumnDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

		currentFiltersInfo1 = new ArrayList<>();
		currentFiltersInfo2 = new ArrayList<>();
	}

	@FXML
	void buscar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: buscar.");
		// If all fields are empty it shows all existing consumos
		List<Consumo> consumos = new ArrayList<>();
		try {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			// Check whether a usuario has been clicked or not
			String idUsuario;
			if (selectedUsuario == null) {
				idUsuario = getIdUsuarioValue();
			} else {
				idUsuario = selectedUsuario.getId();
			}

			// Search filters are stored every time a search is done. This way, when
			// deleting, inserting and modifying, the consumos will be displayed according
			// to the last search filters
			currentFiltersInfo1.removeAll(currentFiltersInfo1);
			currentFiltersInfo2.removeAll(currentFiltersInfo2);
			currentFiltersInfo1.add(idUsuario);
			currentFiltersInfo1.add(getIdAplicacionValue());
			currentFiltersInfo1.add(getMesValue());
			currentFiltersInfo2.add(getConsumoMinValue());
			currentFiltersInfo2.add(getConsumoMaxValue());

			consumos = consumoDao.getConsumos(idUsuario, getIdAplicacionValue(), getMesValue(), getConsumoMinValue(),
					getConsumoMaxValue());
		} catch (Exception e) {
			e.printStackTrace();
			showError("Campos incorrectos.");
		}

		updateTableViewConsumos(consumos);
	}

	@FXML
	void buscarUsuario(ActionEvent event) {
		System.out.println("Se ha presionado el botón: buscarUsuario.");

		List<Usuario> usuarios = new ArrayList<>();
		UsuarioDao usuarioDao = new UsuarioDaoImpl();
		usuarios = usuarioDao.getUsuarios(textFieldIdUsuario.getText(), textFieldNombre.getText(),
				textFieldApellidos.getText(), textFieldEmail.getText(), textFieldTelefono.getText(),
				textFieldDireccion.getText());

		updateTableViewUsuarios(usuarios);
	}

	@FXML
	void modificar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: modificar.");
		boolean error = false;

		List<Consumo> consumos = new ArrayList<>();
		if (comboBoxMes.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdUsuario.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdAplicacion.getSelectionModel().getSelectedItem() == null) {
			showError("Los campos ID Usuario, ID Aplicación y Mes no pueden estar vacíos.");
			error = true;
		} else {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			try {
				if (!consumoDao.updateConsumo(selectedConsumo.getIdUsuario(), selectedConsumo.getIdAplicacion(),
						selectedConsumo.getMes(), new Consumo(getIdUsuarioValue(), getIdAplicacionValue(),
								getMesValue(), getNuevoConsumoValue()))) {
					showError("Se produjo un error a la hora de modificar el consumo.");
					error = true;
				} else { // If no errors occur
					consumos = consumoDao.getConsumos(currentFiltersInfo1.get(0), currentFiltersInfo1.get(1),
							currentFiltersInfo1.get(2), currentFiltersInfo2.get(0), currentFiltersInfo2.get(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlankConsumo();
			// List is updated
			updateTableViewConsumos(consumos);
		}
	}

	@FXML
	void anyadir(ActionEvent event) {
		System.out.println("Se ha presionado el botón: anyadir.");
		boolean error = false;

		List<Consumo> consumos = new ArrayList<>();
		if (comboBoxMes.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdUsuario.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdAplicacion.getSelectionModel().getSelectedItem() == null) {
			showError("Los campos ID Usuario, ID Aplicación y Mes no pueden estar vacíos.");
			error = true;
		} else {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			try {
				if (!consumoDao.insertConsumo(new Consumo(getIdUsuarioValue(), getIdAplicacionValue(), getMesValue(),
						getNuevoConsumoValue()))) {
					showError("Se produjo un error a la hora de añadir el consumo.");
					error = true;
				} else { // If no errors occur
					consumos = consumoDao.getConsumos(currentFiltersInfo1.get(0), currentFiltersInfo1.get(1),
							currentFiltersInfo1.get(2), currentFiltersInfo2.get(0), currentFiltersInfo2.get(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlankConsumo();
			// List is updated
			updateTableViewConsumos(consumos);
		}
	}

	@FXML
	void eliminar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: eliminar.");
		boolean error = false;

		List<Consumo> consumos = new ArrayList<>();
		if (comboBoxMes.getSelectionModel().getSelectedItem() == null
				&& comboBoxIdUsuario.getSelectionModel().getSelectedItem() == null
				&& comboBoxIdAplicacion.getSelectionModel().getSelectedItem() == null
				&& textFieldConsumoMin.getText().isBlank() && textFieldConsumoMax.getText().isBlank()) {
			showError(
					"Al menos uno de los siguiente campos ha de no estar vacío: ID Usuario, ID Aplicación, Mes, ConsumoMin y consumoMax.");
			error = true;
		} else {
			try {
				ConsumoDao consumoDao = new ConsumoDaoImpl();
				if (!consumoDao.deleteConsumos(getIdUsuarioValue(), getIdAplicacionValue(), getMesValue(),
						getConsumoMinValue(), getConsumoMaxValue())) {
					showError("Se produjo un error a la hora de eliminar el consumo.");
					error = true;
				} else { // If no errors occur
					consumos = consumoDao.getConsumos(currentFiltersInfo1.get(0), currentFiltersInfo1.get(1),
							currentFiltersInfo1.get(2), currentFiltersInfo2.get(0), currentFiltersInfo2.get(1));
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlankConsumo();
			// List is updated
			updateTableViewConsumos(consumos);
		}
	}

	@FXML
	void exportar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: exportar.");

		List<Consumo> consumos = tableViewConsumos.getItems();
		if (consumos.isEmpty()) {
			showError("No se ha seleccionada nada para exportar.");
		} else {
			// Set .csv file location
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("CSV files (*.csv)", "*.csv"));
			File file = fc.showSaveDialog(null);

			if (file != null) {
				List<String[]> dataLines = new ArrayList<>(); // Formatted data to be stored
				// For every existing consumos an array of strings is created
				// First line includes the headers of the stored information
				dataLines.add(new String[] { "ID_Usuario", "ID_Aplicación", "Mes", "Consumo (MB)" });
				for (Consumo consumo : consumos) {
					dataLines.add(new String[] { consumo.getIdUsuario(), consumo.getIdAplicacion(), consumo.getMes(),
							Integer.toString(consumo.getConsumo()) });
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

	/*
	 * The following methods take the values from Consumo textFields/comboBoxes. If
	 * they are empty, default value is set.
	 */

	private String getIdUsuarioValue() {
		if (comboBoxIdUsuario.getSelectionModel().getSelectedItem() != null) {
			return comboBoxIdUsuario.getSelectionModel().getSelectedItem().toString();
		}
		return "";
	}

	private String getIdAplicacionValue() {
		if (comboBoxIdAplicacion.getSelectionModel().getSelectedItem() != null) {
			return comboBoxIdAplicacion.getSelectionModel().getSelectedItem().toString();
		}
		return "";
	}

	private String getMesValue() {
		if (comboBoxMes.getSelectionModel().getSelectedItem() != null) {
			return comboBoxMes.getSelectionModel().getSelectedItem().toString();
		}
		return "";
	}

	private int getConsumoMinValue() {
		if (!textFieldConsumoMin.getText().isBlank()) {
			return Integer.parseInt(textFieldConsumoMin.getText());
		}
		return Integer.MIN_VALUE;
	}

	private int getConsumoMaxValue() {
		if (!textFieldConsumoMax.getText().isBlank()) {
			return Integer.parseInt(textFieldConsumoMax.getText());
		}
		return Integer.MAX_VALUE;
	}

	private int getNuevoConsumoValue() {
		if (!textFieldNuevoConsumo.getText().isBlank()) {
			return Integer.parseInt(textFieldNuevoConsumo.getText().toString());
		}
		return 0;
	}

	/*
	 * End of getXValue methods.
	 */

	@FXML
	void selectConsumo(MouseEvent event) {
		Consumo consumo = tableViewConsumos.getSelectionModel().getSelectedItem();

		if (consumo != null) {
			comboBoxIdUsuario.setValue(consumo.getIdUsuario());
			comboBoxIdAplicacion.setValue(consumo.getIdAplicacion());
			comboBoxMes.setValue(consumo.getMes());
			textFieldNuevoConsumo.setText(Integer.toString(consumo.getConsumo()));

			selectedConsumo = consumo;
		}
	}

	@FXML
	void selectUsuario(MouseEvent event) {
		Usuario usuario = tableViewUsuarios.getSelectionModel().getSelectedItem();

		if (usuario != null) {
			textFieldIdUsuario.setText(usuario.getId());
			textFieldNombre.setText(usuario.getNombre());
			textFieldApellidos.setText(usuario.getApellidos());
			textFieldEmail.setText(usuario.getEmail());
			textFieldTelefono.setText(usuario.getTelefono());
			textFieldDireccion.setText(usuario.getDireccion());

			selectedUsuario = usuario;

			// At the same time usuario information is loaded, consumos attached to that
			// usuario are shown
			buscar(null);
		}
	}

	@FXML
	void restablecerCamposConsumo(ActionEvent event) {
		setTextFieldsToBlankConsumo();
	}

	@FXML
	void restablecerCamposUsuario(ActionEvent event) {
		setTextFieldsToBlankUsuario();
		selectedUsuario = null;
	}

	@FXML
	void refrescarCampos(ActionEvent event) {
		// Usuarios are loaded into the convenient comboBox
		UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
		List<Usuario> usuarioList = new ArrayList<>();
		usuarioList = usuarioDao.getAllUsuarios();
		ObservableList<String> observableList = FXCollections.observableArrayList();
		for (Usuario usuario : usuarioList) {
			observableList.add(usuario.getId());
		}
		comboBoxIdUsuario.setItems(observableList);

		// Aplicaciones are loaded into the convenient comboBox
		AplicacionDaoImpl aplicacionDao = new AplicacionDaoImpl();
		List<Aplicacion> aplicacionList = new ArrayList<>();
		aplicacionList = aplicacionDao.getAllAplicaciones();
		observableList = FXCollections.observableArrayList();
		for (Aplicacion aplicacion : aplicacionList) {
			observableList.add(aplicacion.getId());
		}
		comboBoxIdAplicacion.setItems(observableList);

		if (event != null) {
			buscarUsuario(null);
			buscar(null);
		}
	}

	/**
	 * Updates the tableViewConsumos element. If listConsumos is null it shows all
	 * the existing consumos, otherwise shows the ones given in the parameter.
	 * 
	 * @param listConsumos
	 */
	private void updateTableViewConsumos(List<Consumo> listConsumos) {
		ConsumoDao consumoDao = new ConsumoDaoImpl();
		List<Consumo> consumos;
		if (listConsumos == null) { // Take all existing consumos
			consumos = consumoDao.getAllConsumos();
		} else { // Show the ones given
			consumos = listConsumos;
		}

		// List is converted into ObservableList type
		ObservableList<Consumo> observableList = FXCollections.observableArrayList();
		for (Consumo consumo : consumos) {
			observableList.add(consumo);
		}

		// Show items
		tableViewConsumos.setItems(observableList);
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
	 * When an operation regarding the database is done all fields within
	 * tableViewConsumos are set to blank in order not to overwrite values.
	 */
	private void setTextFieldsToBlankConsumo() {
		comboBoxIdUsuario.setValue(null);
		comboBoxIdAplicacion.setValue(null);
		comboBoxMes.setValue(null);
		textFieldConsumoMin.setText("");
		textFieldConsumoMax.setText("");
		textFieldNuevoConsumo.setText("");
	}

	/**
	 * When an operation regarding the database is done all fields within
	 * tableViewUsuarios are set to blank in order not to overwrite values.
	 */
	private void setTextFieldsToBlankUsuario() {
		textFieldIdUsuario.setText("");
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