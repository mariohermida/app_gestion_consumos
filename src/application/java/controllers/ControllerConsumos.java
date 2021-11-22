package application.java.controllers;

import java.util.List;
import application.java.model.Aplicacion;
import application.java.model.Consumo;
import application.java.model.Mes;
import application.java.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
	private TextField textFieldMes;
	
	@FXML
	private TextField textFieldConsumo;

	@FXML
	private TableColumn<Consumo, Integer> tableColumnId;

	@FXML
	private TableColumn<Consumo, Usuario> tableColumnIdUsuario;

	@FXML
	private TableColumn<Consumo, Aplicacion> tableColumnIdAplicacion;

	@FXML
	private TableColumn<Consumo, Mes> tableColumnMes;
	
	@FXML
	private TableColumn<Consumo, Integer> tableColumnConsumo;

	@FXML
	private TableView<Consumo> tableViewConsumos;

	@FXML
	public void initialize() {
		// tableViewConsumos setup
		// Columns values are assigned to the attributes within Consumo class
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnIdUsuario.setCellValueFactory(new PropertyValueFactory<>("usuario"));
		tableColumnIdAplicacion.setCellValueFactory(new PropertyValueFactory<>("aplicacion"));
		tableColumnMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
		tableColumnConsumo.setCellValueFactory(new PropertyValueFactory<>("consumo"));
	}

	@FXML
	void buscar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: buscar.");
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
		
	}

	@FXML
	void restablecerCampos(ActionEvent event) {
		setTextFieldsToBlank();
	}

	/**
	 * Updates the tableViewConsumos element. If listConsumos is null it
	 * shows all the existing consumos, otherwise shows the ones given in the
	 * parameter.
	 * 
	 * @param listConsumos
	 */
	private void updateTableViewConsumos(List<Consumo> listConsumos) {
		
	}

	/**
	 * When an operation regarding the database is done all fields are set to blank
	 * in order not to overwrite values.
	 */
	private void setTextFieldsToBlank() {
		textFieldId.setText("");
		textFieldIdUsuario.setText("");
		textFieldIdAplicacion.setText("");
		textFieldMes.setText("");
		textFieldConsumo.setText("");
		textFieldNuevoId.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}
