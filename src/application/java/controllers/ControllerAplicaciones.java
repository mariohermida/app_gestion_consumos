package application.java.controllers;

import java.util.ArrayList;
import java.util.List;
import application.java.dao.AplicacionDao;
import application.java.dao.AplicacionDaoImpl;
import application.java.model.Aplicacion;
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

/**
 * Class that manages all the events occurred in Aplicaciones.fxml
 */
public class ControllerAplicaciones {

	@FXML
	private TextField textFieldId;

	@FXML
	private TextField textFieldNuevoId;

	@FXML
	private TextField textFieldDescripcion;

	@FXML
	private TextField textFieldGestor;

	@FXML
	private TextField textFieldServidor;

	@FXML
	private TableColumn<Aplicacion, String> tableColumnId;

	@FXML
	private TableColumn<Aplicacion, String> tableColumnDescripcion;

	@FXML
	private TableColumn<Aplicacion, String> tableColumnGestor;

	@FXML
	private TableColumn<Aplicacion, Byte> tableColumnServidor;

	@FXML
	private TableView<Aplicacion> tableViewAplicaciones;

	@FXML
	public void initialize() {
		// tableViewAplicaciones setup
		// Columns values are assigned to the attributes within Aplicacion class
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		tableColumnGestor.setCellValueFactory(new PropertyValueFactory<>("gestor"));
		tableColumnServidor.setCellValueFactory(new PropertyValueFactory<>("servidor"));
	}

	@FXML
	void buscar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: buscar.");

		// If all fields are empty it shows all existing aplicaciones
		List<Aplicacion> aplicaciones = new ArrayList<>();
		try {
			AplicacionDao aplicacionDao = new AplicacionDaoImpl();
			if (textFieldServidor.getText().isBlank()) {
				// Server is set to MIN_VALUE in order to represent empty servidor field
				aplicaciones = aplicacionDao.getAplicaciones(textFieldId.getText(), textFieldDescripcion.getText(),
						textFieldGestor.getText(), Byte.MIN_VALUE);
			} else {
				aplicaciones = aplicacionDao.getAplicaciones(textFieldId.getText(), textFieldDescripcion.getText(),
						textFieldGestor.getText(), Byte.valueOf(textFieldServidor.getText()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			showError("Campos incorrectos.");
		}

		updateTableViewAplicaciones(aplicaciones);
	}

	@FXML
	void modificar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: modificar.");
		boolean error = false;

		if (textFieldId.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
			error = true;
		} else {
			AplicacionDao aplicacionDao = new AplicacionDaoImpl();
			String id = textFieldId.getText();
			if (!textFieldNuevoId.getText().isBlank()) {
				id = textFieldNuevoId.getText();
			}
			try {
				if (!aplicacionDao.updateAplicacion(textFieldId.getText(),
						new Aplicacion(id, textFieldDescripcion.getText(), textFieldGestor.getText(),
								Byte.valueOf(textFieldServidor.getText())))) {
					showError("Se produjo un error a la hora de modificar la aplicación.");
					error = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		// After the insertion the list is updated
		updateTableViewAplicaciones(null);

		// Fields are set to blank
		if (!error) {
			setTextFieldsToBlank();
		}
	}

	@FXML
	void anyadir(ActionEvent event) {
		System.out.println("Se ha presionado el botón: anyadir.");
		boolean error = false;

		if (textFieldId.getText().isBlank() || textFieldServidor.getText().isBlank()) {
			showError("Los campos ID y Servidor no pueden estar vacíos.");
			error = true;
		} else {
			AplicacionDao aplicacionDao = new AplicacionDaoImpl();
			try {
				if (!aplicacionDao
						.insertAplicacion(new Aplicacion(textFieldId.getText(), textFieldDescripcion.getText(),
								textFieldGestor.getText(), Byte.valueOf(textFieldServidor.getText())))) {
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
		updateTableViewAplicaciones(null);

		// Fields are set to blank
		if (!error) {
			setTextFieldsToBlank();
		}
	}

	@FXML
	void eliminar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: eliminar.");
		boolean error = false;

		if (textFieldId.getText().isBlank() && textFieldDescripcion.getText().isBlank()
				&& textFieldGestor.getText().isBlank() && textFieldServidor.getText().isBlank()) {
			showError("Los cuatro campos no pueden estar vacíos.");
			error = true;
		} else {
			try {
				AplicacionDao aplicacionDao = new AplicacionDaoImpl();
				if (textFieldServidor.getText().isBlank()) {
					// Server is set to MIN_VALUE in order to represent empty servidor field
					if (!aplicacionDao.deleteAplicaciones(textFieldId.getText(), textFieldDescripcion.getText(),
							textFieldGestor.getText(), Byte.MIN_VALUE)) {
						showError("Se produjo un error a la hora de eliminar la aplicación.");
						error = true;
					}
				} else {
					if (!aplicacionDao.deleteAplicaciones(textFieldId.getText(), textFieldDescripcion.getText(),
							textFieldGestor.getText(), Byte.valueOf(textFieldServidor.getText()))) {
						showError("Se produjo un error a la hora de eliminar la aplicación.");
						error = true;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		// After the deletion the list is updated
		updateTableViewAplicaciones(null);

		// Fields are set to blank
		if (!error) {
			setTextFieldsToBlank();
		}
	}

	@FXML
	void seleccionarAplicacion(MouseEvent event) {
		Aplicacion aplicacion = tableViewAplicaciones.getSelectionModel().getSelectedItem();

		if (aplicacion != null) {
			textFieldId.setText(aplicacion.getId());
			textFieldDescripcion.setText(aplicacion.getDescripcion());
			textFieldGestor.setText(aplicacion.getGestor());
			textFieldServidor.setText(Byte.toString(aplicacion.getServidor()));
		}
	}

	@FXML
	void restablecerCampos(ActionEvent event) {
		setTextFieldsToBlank();
	}

	/**
	 * Updates the tableViewAplicaciones element. If listAplicaciones is null it
	 * shows all the existing aplicaciones, otherwise shows the ones given in the
	 * parameter.
	 * 
	 * @param listAplicaciones
	 */
	private void updateTableViewAplicaciones(List<Aplicacion> listAplicaciones) {
		AplicacionDao aplicacionDao = new AplicacionDaoImpl();
		List<Aplicacion> aplicaciones;
		if (listAplicaciones == null) { // Take all existing aplicaciones
			aplicaciones = aplicacionDao.getAllAplicaciones();
		} else { // Show the ones given
			aplicaciones = listAplicaciones;
		}

		// List is converted into ObservableList type
		ObservableList<Aplicacion> observableList = FXCollections.observableArrayList();
		for (Aplicacion aplicacion : aplicaciones) {
			observableList.add(aplicacion);
		}

		// Show items
		tableViewAplicaciones.setItems(observableList);
	}

	/**
	 * When an operation regarding the database is done all fields are set to blank
	 * in order not to overwrite values.
	 */
	private void setTextFieldsToBlank() {
		textFieldId.setText("");
		textFieldDescripcion.setText("");
		textFieldGestor.setText("");
		textFieldServidor.setText("");
		textFieldNuevoId.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}
