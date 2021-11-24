package application.java.controllers;

import java.util.ArrayList;
import java.util.List;
import application.java.dao.AplicacionDaoImpl;
import application.java.dao.ConsumoDao;
import application.java.dao.ConsumoDaoImpl;
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

/**
 * Class that manages all the events occurred in Consumos.fxml
 */
public class ControllerConsumos {

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldNuevoId;

	@FXML
	private ComboBox<String> comboBoxIdUsuario;

	@FXML
	private ComboBox<String> comboBoxIdAplicacion;

	@FXML
	private ComboBox<Byte> comboBoxMes;

	@FXML
	private TextField textFieldConsumoMin;

	@FXML
	private TextField textFieldConsumoMax;

	@FXML
	private TextField textFieldNuevoConsumo;

	@FXML
	private TableColumn<Consumo, Integer> tableColumnId;

	@FXML
	private TableColumn<Consumo, String> tableColumnIdUsuario;

	@FXML
	private TableColumn<Consumo, String> tableColumnIdAplicacion;

	@FXML
	private TableColumn<Consumo, Byte> tableColumnMes;

	@FXML
	private TableColumn<Consumo, Integer> tableColumnConsumo;

	@FXML
	private TableView<Consumo> tableViewConsumos;

	@FXML
	public void initialize() {
		// tableViewConsumos setup
		// Columns values are assigned to the attributes within Consumo class
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnIdUsuario.setCellValueFactory(new PropertyValueFactory<>("idUsuario"));
		tableColumnIdAplicacion.setCellValueFactory(new PropertyValueFactory<>("idAplicacion"));
		tableColumnMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
		tableColumnConsumo.setCellValueFactory(new PropertyValueFactory<>("consumo"));

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

		// Different mes values are loaded into the convenient comboBox
		comboBoxMes.setItems(FXCollections.observableArrayList((byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5,
				(byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12));
	}

	@FXML
	void buscar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: buscar.");

		// If all fields are empty it shows all existing consumos
		List<Consumo> consumos = new ArrayList<>();
		try {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			// Set default values
			int id = Integer.MIN_VALUE, consumoMin = 0, consumoMax = Integer.MAX_VALUE;
			String idUsuario = "", idAplicacion = "";
			byte mes = 0;
			// Set given values if fields are not empty
			if (!textFieldId.getText().isBlank()) {
				id = Integer.valueOf(textFieldId.getText());
			}
			if (comboBoxIdUsuario.getSelectionModel().getSelectedItem() != null) {
				idUsuario = comboBoxIdUsuario.getSelectionModel().getSelectedItem().toString();
			}
			if (comboBoxIdAplicacion.getSelectionModel().getSelectedItem() != null) {
				idAplicacion = comboBoxIdAplicacion.getSelectionModel().getSelectedItem().toString();
			}
			if (comboBoxMes.getSelectionModel().getSelectedItem() != null) {
				mes = Byte.parseByte(comboBoxMes.getSelectionModel().getSelectedItem().toString());
			}
			if (!textFieldConsumoMin.getText().isBlank()) {
				consumoMin = Integer.valueOf(textFieldConsumoMin.getText());
			}
			if (!textFieldConsumoMax.getText().isBlank()) {
				consumoMax = Integer.valueOf(textFieldConsumoMax.getText());
			}
			consumos = consumoDao.getConsumos(id, idUsuario, idAplicacion, mes, consumoMin, consumoMax);
		} catch (Exception e) {
			e.printStackTrace();
			showError("Campos incorrectos.");
		}

		updateTableViewConsumos(consumos);
	}

	@FXML
	void modificar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: modificar.");
		boolean error = false;

		if (textFieldId.getText().isBlank() || comboBoxMes.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdUsuario.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdAplicacion.getSelectionModel().getSelectedItem() == null) {
			showError("Los campos ID, ID Usuario, ID Aplicación y Mes no pueden estar vacíos.");
			error = true;
		} else {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			try {
				// Set default values
				int id = Integer.parseInt(textFieldId.getText());
				int consumo = 0;
				// Set given values if fields are not empty
				if (!textFieldNuevoId.getText().isBlank()) {
					id = Integer.parseInt(textFieldNuevoId.getText());
				}
				if (!textFieldNuevoConsumo.getText().isBlank()) {
					consumo = Integer.parseInt(textFieldNuevoConsumo.getText().toString());
				}
				if (!consumoDao.updateConsumo(Integer.parseInt(textFieldId.getText()),
						new Consumo(id, comboBoxIdUsuario.getSelectionModel().getSelectedItem().toString(),
								comboBoxIdAplicacion.getSelectionModel().getSelectedItem().toString(),
								Byte.valueOf(comboBoxMes.getSelectionModel().getSelectedItem().toString()), consumo))) {
					showError("Se produjo un error a la hora de modificar el consumo.");
					error = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		// After the insertion the list is updated
		updateTableViewConsumos(null);

		// Fields are set to blank
		if (!error) {
			setTextFieldsToBlank();
		}
	}

	@FXML
	void anyadir(ActionEvent event) {
		System.out.println("Se ha presionado el botón: anyadir.");
		boolean error = false;

		if (comboBoxMes.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdUsuario.getSelectionModel().getSelectedItem() == null
				|| comboBoxIdAplicacion.getSelectionModel().getSelectedItem() == null) {
			showError("Los campos ID Usuario, ID Aplicación y Mes no pueden estar vacíos.");
			error = true;
		} else {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			// Set default values
			int id = Integer.MIN_VALUE;
			int consumo = 0;
			// Set given values if fields are not empty
			if (!textFieldNuevoId.getText().isBlank()) {
				id = Integer.parseInt(textFieldNuevoId.getText());
			}
			if (!textFieldNuevoConsumo.getText().isBlank()) {
				consumo = Integer.parseInt(textFieldNuevoConsumo.getText().toString());
			}
			try {
				if (!consumoDao.insertConsumo(new Consumo(id,
						comboBoxIdUsuario.getSelectionModel().getSelectedItem().toString(),
						comboBoxIdAplicacion.getSelectionModel().getSelectedItem().toString(),
						Byte.valueOf(comboBoxMes.getSelectionModel().getSelectedItem().toString()), consumo))) {
					showError("Se produjo un error a la hora de añadir la aplicación.");
					error = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		// After the insertion the list is updated
		updateTableViewConsumos(null);

		// Fields are set to blank
		if (!error) {
			setTextFieldsToBlank();
		}
	}

	@FXML
	void eliminar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: eliminar.");
	}

	@FXML
	void seleccionarConsumo(MouseEvent event) {
		Consumo consumo = tableViewConsumos.getSelectionModel().getSelectedItem();

		if (consumo != null) {
			textFieldId.setText(Integer.toString(consumo.getId()));
			comboBoxIdUsuario.setValue(consumo.getIdUsuario());
			comboBoxIdAplicacion.setValue(consumo.getIdAplicacion());
			comboBoxMes.setValue(consumo.getMes());
			textFieldNuevoConsumo.setText(Integer.toString(consumo.getConsumo()));
		}
	}

	@FXML
	void restablecerCampos(ActionEvent event) {
		setTextFieldsToBlank();
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
	 * When an operation regarding the database is done all fields are set to blank
	 * in order not to overwrite values.
	 */
	private void setTextFieldsToBlank() {
		textFieldId.setText("");
		comboBoxIdUsuario.setValue(null);
		comboBoxIdAplicacion.setValue(null);
		comboBoxMes.setValue(null);
		textFieldConsumoMin.setText("");
		textFieldConsumoMax.setText("");
		textFieldNuevoId.setText("");
		textFieldNuevoConsumo.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}
