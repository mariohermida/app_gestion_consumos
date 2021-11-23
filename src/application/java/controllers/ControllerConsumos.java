package application.java.controllers;

import java.util.ArrayList;
import java.util.List;
import application.java.dao.ConsumoDao;
import application.java.dao.ConsumoDaoImpl;
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
	private TextField textFieldIdUsuario;

	@FXML
	private TextField textFieldIdAplicacion;

	@FXML
	private ComboBox<Byte> comboBoxMes;

	private ObservableList<Byte> mesList = FXCollections.observableArrayList((byte) 0, (byte) 1, (byte) 2, (byte) 3,
			(byte) 4, (byte) 5, (byte) 6, (byte) 7, (byte) 8, (byte) 9, (byte) 10, (byte) 11, (byte) 12);

	@FXML
	private TextField textFieldConsumo;

	@FXML
	private TableColumn<Consumo, Integer> tableColumnId;

	@FXML
	private TableColumn<Consumo, Usuario> tableColumnIdUsuario;

	@FXML
	private TableColumn<Consumo, Aplicacion> tableColumnIdAplicacion;

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

		// Assign created items to comboBoxMes and set default value
		comboBoxMes.setItems(mesList);
		comboBoxMes.setValue((byte) 0);
	}

	@FXML
	void buscar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: buscar.");

		// If all fields are empty it shows all existing consumos
		List<Consumo> consumos = new ArrayList<>();
		try {
			ConsumoDao consumoDao = new ConsumoDaoImpl();
			// For fields that are empty integers MIN_VALUE is used in order not to trigger
			// an exception
			if (textFieldId.getText().isBlank()) {
				if (textFieldConsumo.getText().isBlank()) {
					consumos = consumoDao.getConsumos(Integer.MIN_VALUE, textFieldIdUsuario.getText(),
							textFieldIdAplicacion.getText(),
							Byte.valueOf(comboBoxMes.getSelectionModel().getSelectedItem().toString()),
							Integer.MIN_VALUE);
				} else {
					consumos = consumoDao.getConsumos(Integer.MIN_VALUE, textFieldIdUsuario.getText(),
							textFieldIdAplicacion.getText(),
							Byte.valueOf(comboBoxMes.getSelectionModel().getSelectedItem().toString()),
							Integer.valueOf(textFieldConsumo.getText()));
				}
			} else {
				if (textFieldConsumo.getText().isBlank()) {
					consumos = consumoDao.getConsumos(Integer.valueOf(textFieldId.getText()),
							textFieldIdUsuario.getText(), textFieldIdAplicacion.getText(),
							Byte.valueOf(comboBoxMes.getSelectionModel().getSelectedItem().toString()),
							Integer.MIN_VALUE);
				} else {
					consumos = consumoDao.getConsumos(Integer.valueOf(textFieldId.getText()),
							textFieldIdUsuario.getText(), textFieldIdAplicacion.getText(),
							Byte.valueOf(comboBoxMes.getSelectionModel().getSelectedItem().toString()),
							Integer.valueOf(textFieldConsumo.getText()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			showError("Campos incorrectos.");
		}

		updateTableViewConsumos(consumos);
	}

	@FXML
	void modificar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: modificar.");
	}

	@FXML
	void anyadir(ActionEvent event) {
		System.out.println("Se ha presionado el botón: anyadir.");
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
			textFieldIdUsuario.setText(consumo.getIdUsuario());
			textFieldIdAplicacion.setText(consumo.getIdAplicacion());
			comboBoxMes.setValue(consumo.getMes());
			textFieldConsumo.setText(Integer.toString(consumo.getConsumo()));
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
		textFieldIdUsuario.setText("");
		textFieldIdAplicacion.setText("");
		comboBoxMes.setValue((byte) 0);
		textFieldConsumo.setText("");
		textFieldNuevoId.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}
