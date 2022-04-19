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
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Class that manages all the events occurred in Consumos.fxml
 */
public class ControllerEntity3 {

	@FXML
	private AnchorPane rootPane;

	// Fields for Consumo

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
	private ComboBox<String> comboBoxIdUsuario;

	@FXML
	private ComboBox<String> comboBoxAplicacion;

	@FXML
	private ComboBox<String> comboBoxMes;

	@FXML
	private TextField textField1;

	@FXML
	private TextField textField2;

	@FXML
	private TextField textField3;

	@FXML
	private TextField textField4;

	@FXML
	private TableColumn<Consumo, String> tableColumn1;

	@FXML
	private TableColumn<Consumo, String> tableColumn2;

	@FXML
	private TableColumn<Consumo, String> tableColumn3;

	@FXML
	private TableColumn<Consumo, Integer> tableColumn4;

	@FXML
	private TableView<Consumo> tableViewConsumos;

	// Fields for Usuario

	@FXML
	private Label label6;

	@FXML
	private Label label7;

	@FXML
	private Label label8;

	@FXML
	private Label label9;

	@FXML
	private Label label10;

	@FXML
	private Label label11;

	@FXML
	private TextField textField5;

	@FXML
	private TextField textField6;

	@FXML
	private TextField textField7;

	@FXML
	private TextField textField8;

	@FXML
	private TextField textField9;

	@FXML
	private TextField textField10;

	@FXML
	private TableColumn<Usuario, String> tableColumn5;

	@FXML
	private TableColumn<Usuario, String> tableColumn6;

	@FXML
	private TableColumn<Usuario, String> tableColumn7;

	@FXML
	private TableColumn<Usuario, String> tableColumn8;

	@FXML
	private TableColumn<Usuario, String> tableColumn9;

	@FXML
	private TableColumn<Usuario, String> tableColumn10;

	@FXML
	private TableView<Usuario> tableViewUsuarios;

	private List<String> currentFiltersInfo1; // Current search filters for: idUsuario, App and Mes
	private List<Integer> currentFiltersInfo2; // Current search filters for: min and max consumo

	private Consumo selectedConsumo; // Current consumo (user mouse click)
	private Usuario selectedUsuario; // Current usuario (user mouse click

	// Object for retrieving the values stored in file
	private Properties properties = new Properties();

	// This method is called once this class is created
	// It is the first function to be executed
	@FXML
	public void initialize() {
		// Label texts are retrieved from file
		try {
			properties.load(new FileInputStream(new File("C:\\Users\\SIC-LN-34\\Desktop\\M\\titles.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		label1.setText(properties.getProperty("entity3_title1"));
		label2.setText(properties.getProperty("entity3_title2"));
		label3.setText(properties.getProperty("entity3_title3"));
		label4.setText(properties.getProperty("entity3_title4"));
		label5.setText(properties.getProperty("entity3_title5"));
		
		tableColumn1.setText(properties.getProperty("entity3_title1"));
		tableColumn2.setText(properties.getProperty("entity3_title2"));
		tableColumn3.setText(properties.getProperty("entity3_title3"));
		tableColumn4.setText(properties.getProperty("entity3_title4"));
		
		// tableViewConsumos setup
		// Column values are assigned to attributes within Consumo class
		tableColumn1.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
		tableColumn2.setCellValueFactory(new PropertyValueFactory<>("idAplicacion"));
		tableColumn3.setCellValueFactory(new PropertyValueFactory<>("mes"));
		tableColumn4.setCellValueFactory(new PropertyValueFactory<>("consumo"));

		refrescarCampos(null);

		// Possible mes values are loaded into the convenient comboBox
		comboBoxMes.setItems(FXCollections.observableArrayList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
				"Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"));
		
		label6.setText(properties.getProperty("entity2_title1"));
		label7.setText(properties.getProperty("entity2_title2"));
		label8.setText(properties.getProperty("entity2_title3"));
		label9.setText(properties.getProperty("entity2_title4"));
		label10.setText(properties.getProperty("entity2_title5"));
		label11.setText(properties.getProperty("entity2_title6"));
		
		tableColumn5.setText(properties.getProperty("entity2_title1"));
		tableColumn6.setText(properties.getProperty("entity2_title2"));
		tableColumn7.setText(properties.getProperty("entity2_title3"));
		tableColumn8.setText(properties.getProperty("entity2_title4"));
		tableColumn9.setText(properties.getProperty("entity2_title5"));
		tableColumn10.setText(properties.getProperty("entity2_title6"));

		// tableViewUsuarios setup
		// Column values are assigned to attributes within Usuario class
		tableColumn5.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumn6.setCellValueFactory(new PropertyValueFactory<>("nombre"));
		tableColumn7.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
		tableColumn8.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumn9.setCellValueFactory(new PropertyValueFactory<>("telefono"));
		tableColumn10.setCellValueFactory(new PropertyValueFactory<>("direccion"));

		currentFiltersInfo1 = new ArrayList<>();
		currentFiltersInfo2 = new ArrayList<>();

		// Dynamic search for id aplicacion
		textField4.textProperty().addListener((observable, oldValue, newValue) -> {
			AplicacionDaoImpl aplicacionDao = new AplicacionDaoImpl();
			// All aplicaciones that meet the name in the text field will be displayed in
			// the combobox
			List<Aplicacion> listAplicaciones = aplicacionDao.getAplicaciones(newValue, "", "", Byte.MIN_VALUE);
			ObservableList<String> observableList = FXCollections.observableArrayList();
			for (Aplicacion aplicacion : listAplicaciones) {
				observableList.add(aplicacion.getId() + " - " + aplicacion.getDescripcion() + " - "
						+ aplicacion.getGestor() + " - " + aplicacion.getServidor());
				comboBoxAplicacion.setValue(aplicacion.getId() + " - " + aplicacion.getDescripcion() + " - "
						+ aplicacion.getGestor() + " - " + aplicacion.getServidor());
			}
			comboBoxAplicacion.setItems(observableList);
		});

		// Dynamic search for all usuario fields
		// Once a change on any field is made the tableView is updated
		// Filters can be used altogether
		UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
		textField5.textProperty().addListener((observable, oldValue, newValue) -> {
			updateTableViewUsuarios(usuarioDao.getUsuarios(newValue, textField6.getText(), textField7.getText(),
					textField8.getText(), textField9.getText(), textField10.getText()));
		});

		textField6.textProperty().addListener((observable, oldValue, newValue) -> {
			updateTableViewUsuarios(usuarioDao.getUsuarios(textField5.getText(), newValue, textField7.getText(),
					textField8.getText(), textField9.getText(), textField10.getText()));
		});

		textField7.textProperty().addListener((observable, oldValue, newValue) -> {
			updateTableViewUsuarios(usuarioDao.getUsuarios(textField5.getText(), textField6.getText(), newValue,
					textField8.getText(), textField9.getText(), textField10.getText()));
		});

		textField8.textProperty().addListener((observable, oldValue, newValue) -> {
			updateTableViewUsuarios(usuarioDao.getUsuarios(textField5.getText(), textField6.getText(),
					textField7.getText(), newValue, textField9.getText(), textField10.getText()));
		});

		textField9.textProperty().addListener((observable, oldValue, newValue) -> {
			updateTableViewUsuarios(usuarioDao.getUsuarios(textField5.getText(), textField6.getText(),
					textField7.getText(), textField8.getText(), newValue, textField10.getText()));
		});

		textField10.textProperty().addListener((observable, oldValue, newValue) -> {
			updateTableViewUsuarios(usuarioDao.getUsuarios(textField5.getText(), textField6.getText(),
					textField7.getText(), textField8.getText(), textField9.getText(), newValue));
		});

		// Initially, all consumos and usuarios are shown
		updateTableViewConsumos(null);
		updateTableViewUsuarios(null);

	}

	/**
	 * Method that takes the last values retrieved from both textfields and
	 * comboboxes. It is used for storing the info while modifying a consumo.
	 */
	private void updateSearchFilters() {
		// Search filters are stored every time a search is done. This way, when
		// deleting, inserting or modifying, the consumos will be displayed according
		// to the last search filters
		currentFiltersInfo1.removeAll(currentFiltersInfo1);
		currentFiltersInfo2.removeAll(currentFiltersInfo2);
		currentFiltersInfo1.add(getIdUsuarioValue());
		currentFiltersInfo1.add(getAplicacionValue());
		currentFiltersInfo1.add(getMesValue());
		currentFiltersInfo2.add(getConsumoMinValue());
		currentFiltersInfo2.add(getConsumoMaxValue());
	}

	@FXML
	void buscar(ActionEvent event) {
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

			updateSearchFilters();

			consumos = consumoDao.getConsumos(idUsuario, getAplicacionValue(), getMesValue(), getConsumoMinValue(),
					getConsumoMaxValue());
		} catch (Exception e) {
			e.printStackTrace();
			showError("Campos incorrectos.");
		}

		updateTableViewConsumos(consumos);
	}

	@FXML
	void modificar(ActionEvent event) {
		boolean error = false;

		// Update the values for having the possibility of modifying a consumo
		// The consumo to update is taken from the selected consumos (mouse click)
		// The new values to be assigned for the consumo are taken from the comboboxes
		updateSearchFilters();

		List<Consumo> consumos = new ArrayList<>();
		if (comboBoxMes.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdUsuario.getSelectionModel().getSelectedItem() == null
				|| comboBoxAplicacion.getSelectionModel().getSelectedItem() == null) {
			showError("Los campos ID Usuario, ID Aplicación y Mes no pueden estar vacíos.");
			error = true;
		} else {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			try {
				if (!consumoDao.updateConsumo(selectedConsumo.getIdUsuario(), selectedConsumo.getIdAplicacion(),
						selectedConsumo.getMes(), new Consumo(getIdUsuarioValue(), getAplicacionValue(), getMesValue(),
								getNuevoConsumoValue()))) {
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
		boolean error = false;

		List<Consumo> consumos = new ArrayList<>();
		if (comboBoxMes.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdUsuario.getSelectionModel().getSelectedItem() == null
				|| comboBoxAplicacion.getSelectionModel().getSelectedItem() == null) {
			showError("Los campos ID Usuario, ID Aplicación y Mes no pueden estar vacíos.");
			error = true;
		} else {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			try {
				if (!consumoDao.insertConsumo(new Consumo(getIdUsuarioValue(), getAplicacionValue(), getMesValue(),
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
		boolean error = false;

		List<Consumo> consumos = new ArrayList<>();
		if (comboBoxMes.getSelectionModel().getSelectedItem() == null
				&& comboBoxIdUsuario.getSelectionModel().getSelectedItem() == null
				&& comboBoxAplicacion.getSelectionModel().getSelectedItem() == null && textField1.getText().isBlank()
				&& textField2.getText().isBlank()) {
			showError(
					"Al menos uno de los siguientes campos no ha de estar vacío: ID Usuario, ID Aplicación, Mes, ConsumoMin y consumoMax.");
			error = true;
		} else {
			try {
				ConsumoDao consumoDao = new ConsumoDaoImpl();
				if (!consumoDao.deleteConsumos(getIdUsuarioValue(), getAplicacionValue(), getMesValue(),
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

	// It does export only the consumos shown in the tableViewConsumos
	@FXML
	void exportar(ActionEvent event) {
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

	private String getAplicacionValue() {
		if (comboBoxAplicacion.getSelectionModel().getSelectedItem() != null) {
			// The content is split due to this format is used:
			// idAplicacion - descripcion - gestor - servidor
			// The only element wanted is the idAplicacion
			return comboBoxAplicacion.getSelectionModel().getSelectedItem().toString().split(" -")[0];
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
		if (!textField1.getText().isBlank()) {
			return Integer.parseInt(textField1.getText());
		}
		return Integer.MIN_VALUE;
	}

	private int getConsumoMaxValue() {
		if (!textField2.getText().isBlank()) {
			return Integer.parseInt(textField2.getText());
		}
		return Integer.MAX_VALUE;
	}

	private int getNuevoConsumoValue() {
		if (!textField3.getText().isBlank()) {
			return Integer.parseInt(textField3.getText().toString());
		}
		return 0;
	}

	/*
	 * End of getXValue methods.
	 */

	@FXML
	void selectConsumo(MouseEvent event) {
		Consumo consumo = tableViewConsumos.getSelectionModel().getSelectedItem();
		AplicacionDaoImpl aplicacionDao = new AplicacionDaoImpl();

		if (consumo != null) { // Check that consumo is not null
			// Get all aplicacion information related to consumo from database
			Aplicacion aplicacion = aplicacionDao.getAplicaciones(consumo.getIdAplicacion(), "", "", Byte.MIN_VALUE)
					.get(0);

			// Set values in cells
			comboBoxIdUsuario.setValue(consumo.getIdUsuario());
			comboBoxAplicacion.setValue(aplicacion.getId() + " - " + aplicacion.getDescripcion() + " - "
					+ aplicacion.getGestor() + " - " + aplicacion.getServidor());
			comboBoxMes.setValue(consumo.getMes());
			textField3.setText(Integer.toString(consumo.getConsumo()));

			// Assign current consumo to the selected one
			selectedConsumo = consumo;
		}
	}

	@FXML
	void selectUsuario(MouseEvent event) {
		Usuario usuario = tableViewUsuarios.getSelectionModel().getSelectedItem();

		if (usuario != null) {
			textField5.setText(usuario.getId());
			textField6.setText(usuario.getNombre());
			textField7.setText(usuario.getApellidos());
			textField8.setText(usuario.getEmail());
			textField9.setText(usuario.getTelefono());
			textField10.setText(usuario.getDireccion());

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
			observableList.add(aplicacion.getId() + " - " + aplicacion.getDescripcion() + " - " + aplicacion.getGestor()
					+ " - " + aplicacion.getServidor());
		}
		comboBoxAplicacion.setItems(observableList);

		if (event != null) {
			buscar(null);
		}
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
		comboBoxAplicacion.setValue(null);
		comboBoxMes.setValue(null);
		textField1.setText("");
		textField2.setText("");
		textField3.setText("");
		textField4.setText("");
	}

	/**
	 * When an operation regarding the database is done all fields within
	 * tableViewUsuarios are set to blank in order not to overwrite values.
	 */
	private void setTextFieldsToBlankUsuario() {
		textField5.setText("");
		textField6.setText("");
		textField7.setText("");
		textField8.setText("");
		textField9.setText("");
		textField10.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}