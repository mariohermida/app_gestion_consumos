package application.java.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * Class that manages all the events occurred in Aplicaciones.fxml
 */
public class ControllerAplicaciones {

	@FXML
	private TextField textFieldId;

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

	private Aplicacion selectedAplicacion; // Current aplicacion

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
			aplicaciones = aplicacionDao.getAplicaciones(textFieldId.getText(), textFieldDescripcion.getText(),
					textFieldGestor.getText(), getServidorValue());
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
			try {
				if (!aplicacionDao.updateAplicacion(selectedAplicacion.getId(),
						new Aplicacion(textFieldId.getText(), textFieldDescripcion.getText(), textFieldGestor.getText(),
								Byte.parseByte(textFieldServidor.getText())))) {
					showError("Se produjo un error a la hora de modificar la aplicación.");
					error = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlank();
			// After the insertion the list is updated
			updateTableViewAplicaciones(null);
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
								textFieldGestor.getText(), Byte.parseByte(textFieldServidor.getText())))) {
					showError("Se produjo un error a la hora de añadir la aplicación.");
					error = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlank();
			// After the insertion the list is updated
			updateTableViewAplicaciones(null);
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
				if (!aplicacionDao.deleteAplicaciones(textFieldId.getText(), textFieldDescripcion.getText(),
						textFieldGestor.getText(), getServidorValue())) {
					showError("Se produjo un error a la hora de eliminar la aplicación.");
					error = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				showError("Campos incorrectos.");
				error = true;
			}
		}

		if (!error) {
			setTextFieldsToBlank();
			// After the deletion the list is updated
			updateTableViewAplicaciones(null);
		}
	}

	@FXML
	void exportar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: exportar.");

		List<Aplicacion> aplicaciones = tableViewAplicaciones.getItems();
		if (aplicaciones.isEmpty()) {
			showError("No se ha seleccionada nada para exportar.");
		} else {
			// Set .csv file location
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("CSV files (*.csv)", "*.csv"));
			File file = fc.showSaveDialog(null);

			if (file != null) {
				List<String[]> dataLines = new ArrayList<>(); // Formatted data to be stored
				// For every existing aplicacion an array of strings is created
				// First line includes the headers of the stored information
				dataLines.add(new String[] { "ID_Aplicación", "Descripción", "Gestor", "Servidor" });
				for (Aplicacion aplicacion : aplicaciones) {
					dataLines.add(new String[] { aplicacion.getId(), aplicacion.getDescripcion(),
							aplicacion.getGestor(), Byte.toString(aplicacion.getServidor()) });
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

	/**
	 * It checks if any servidor value has been input. If not, return default one.
	 * 
	 * @return wanted servidor value
	 */
	private byte getServidorValue() {
		if (!textFieldServidor.getText().isBlank()) {
			return Byte.parseByte(textFieldServidor.getText());
		}
		return Byte.MIN_VALUE;
	}

	@FXML
	void selectAplicacion(MouseEvent event) {
		Aplicacion aplicacion = tableViewAplicaciones.getSelectionModel().getSelectedItem();

		if (aplicacion != null) {
			textFieldId.setText(aplicacion.getId());
			textFieldDescripcion.setText(aplicacion.getDescripcion());
			textFieldGestor.setText(aplicacion.getGestor());
			textFieldServidor.setText(Byte.toString(aplicacion.getServidor()));

			selectedAplicacion = aplicacion;
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
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}