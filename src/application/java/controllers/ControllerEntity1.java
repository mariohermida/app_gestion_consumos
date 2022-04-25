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
import application.java.dao.AplicacionDao;
import application.java.dao.AplicacionDaoImpl;
import application.java.model.Aplicacion;
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
 * Class that manages all the events occurred in Aplicaciones.fxml
 */
public class ControllerEntity1 {

	@FXML
	private Label label1;

	@FXML
	private Label label2;

	@FXML
	private Label label3;

	@FXML
	private Label label4;

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField textField1;

	@FXML
	private TextField textField2;

	@FXML
	private TextField textField3;

	@FXML
	private TextField textField4;

	@FXML
	private TableColumn<Aplicacion, String> tableColumn1;

	@FXML
	private TableColumn<Aplicacion, String> tableColumn2;

	@FXML
	private TableColumn<Aplicacion, String> tableColumn3;

	@FXML
	private TableColumn<Aplicacion, Byte> tableColumn4;

	@FXML
	private TableView<Aplicacion> tableViewAplicaciones;

	private Aplicacion selectedAplicacion; // Current aplicacion

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
		label1.setText(properties.getProperty("entity1_title1"));
		label2.setText(properties.getProperty("entity1_title2"));
		label3.setText(properties.getProperty("entity1_title3"));
		label4.setText(properties.getProperty("entity1_title4"));
		
		// Table column texts are set
		tableColumn1.setText(properties.getProperty("entity1_title1"));
		tableColumn2.setText(properties.getProperty("entity1_title2"));
		tableColumn3.setText(properties.getProperty("entity1_title3"));
		tableColumn4.setText(properties.getProperty("entity1_title4"));

		// tableViewAplicaciones setup
		// Column values are assigned to the attributes within Aplicacion class
		tableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumn2.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
		tableColumn3.setCellValueFactory(new PropertyValueFactory<>("gestor"));
		tableColumn4.setCellValueFactory(new PropertyValueFactory<>("servidor"));

		// Initially, all existing aplicaciones are shown
		updateTableViewAplicaciones(null);
	}

	@FXML
	void buscar(ActionEvent event) {
		// If all fields are empty it shows all existing aplicaciones
		List<Aplicacion> aplicaciones = new ArrayList<>();
		try {
			AplicacionDao aplicacionDao = new AplicacionDaoImpl();
			aplicaciones = aplicacionDao.getAplicaciones(textField1.getText(), textField2.getText(),
					textField3.getText(), getServidorValue());
		} catch (Exception e) {
			e.printStackTrace();
			showError("Campos incorrectos.");
		}

		updateTableViewAplicaciones(aplicaciones);
	}

	@FXML
	void modificar(ActionEvent event) {
		boolean error = false;

		if (textField1.getText().isBlank()) {
			showError("El campo ID no puede estar vacío.");
			error = true;
		} else {
			AplicacionDao aplicacionDao = new AplicacionDaoImpl();
			try {
				if (!aplicacionDao.updateAplicacion(selectedAplicacion.getId(),
						new Aplicacion(textField1.getText(), textField2.getText(), textField3.getText(),
								Byte.parseByte(textField4.getText())))) {
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
		boolean error = false;

		if (textField1.getText().isBlank() || textField4.getText().isBlank()) {
			showError("Los campos ID y Servidor no pueden estar vacíos.");
			error = true;
		} else {
			AplicacionDao aplicacionDao = new AplicacionDaoImpl();
			try {
				if (!aplicacionDao
						.insertAplicacion(new Aplicacion(textField1.getText(), textField2.getText(),
								textField3.getText(), Byte.parseByte(textField4.getText())))) {
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
		boolean error = false;

		if (textField1.getText().isBlank() && textField2.getText().isBlank()
				&& textField3.getText().isBlank() && textField4.getText().isBlank()) {
			showError("Los cuatro campos no pueden estar vacíos.");
			error = true;
		} else {
			try {
				AplicacionDao aplicacionDao = new AplicacionDaoImpl();
				if (!aplicacionDao.deleteAplicaciones(textField1.getText(), textField2.getText(),
						textField3.getText(), getServidorValue())) {
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
		if (!textField4.getText().isBlank()) {
			return Byte.parseByte(textField4.getText());
		}
		return Byte.MIN_VALUE;
	}

	@FXML
	void selectAplicacion(MouseEvent event) {
		Aplicacion aplicacion = tableViewAplicaciones.getSelectionModel().getSelectedItem();

		if (aplicacion != null) {
			textField1.setText(aplicacion.getId());
			textField2.setText(aplicacion.getDescripcion());
			textField3.setText(aplicacion.getGestor());
			textField4.setText(Byte.toString(aplicacion.getServidor()));

			selectedAplicacion = aplicacion;
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
		textField1.setText("");
		textField2.setText("");
		textField3.setText("");
		textField4.setText("");
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}