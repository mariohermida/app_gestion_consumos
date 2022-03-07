package application.java.controllers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import application.java.dao.AplicacionDaoImpl;
import application.java.dao.ConsumoDaoImpl;
import application.java.dao.UsuarioDaoImpl;
import application.java.model.Aplicacion;
import application.java.model.Consumo;
import application.java.model.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * Class that manages all the events occurred in Importacion.fxml
 */
public class ControllerImportacion {
	
	@FXML
	private AnchorPane rootPane;

	@FXML
	void importarAplicaciones(ActionEvent event) throws IOException, CsvValidationException {
		FileChooser fc = new FileChooser();
		fc.setTitle("Seleccione el archivo de aplicaciones a importar:");
		File file = fc.showOpenDialog(null);

		Reader reader = new FileReader(file);

		CSVReader csvReader = new CSVReader(reader);
		csvReader.readNext(); // Headers are not stored

		AplicacionDaoImpl aplicacionDao = new AplicacionDaoImpl();
		String[] nextRecord;
		while ((nextRecord = csvReader.readNext()) != null) {
			if (!aplicacionDao.insertAplicacion(
					new Aplicacion(nextRecord[0], nextRecord[1], nextRecord[2], Byte.parseByte(nextRecord[3])))) {
				showError("Se produjo un error a la hora de importar la aplicación: " + nextRecord[0]);
			}
		}
		reader.close();
		csvReader.close();
	}	

	@FXML
	void importarUsuarios(ActionEvent event) throws IOException, CsvValidationException {
		FileChooser fc = new FileChooser();
		fc.setTitle("Seleccione el archivo de usuarios a importar:");
		File file = fc.showOpenDialog(null);

		Reader reader = new FileReader(file);

		CSVReader csvReader = new CSVReader(reader);
		csvReader.readNext(); // Headers are not stored

		UsuarioDaoImpl usuarioDao = new UsuarioDaoImpl();
		String[] nextRecord;
		while ((nextRecord = csvReader.readNext()) != null) {
			if (!usuarioDao.insertUsuario(new Usuario(nextRecord[0], nextRecord[1], nextRecord[2], nextRecord[3],
					nextRecord[4], nextRecord[5]))) {
				showError("Se produjo un error a la hora de importar el usuario: " + nextRecord[0]);
			}
		}
		reader.close();
		csvReader.close();
	}

	@FXML
	void importarConsumos(ActionEvent event) throws IOException, CsvValidationException {
		FileChooser fc = new FileChooser();
		fc.setTitle("Seleccione el archivo de consumos a importar:");
		File file = fc.showOpenDialog(null);

		Reader reader = new FileReader(file);

		CSVReader csvReader = new CSVReader(reader);
		csvReader.readNext(); // Headers are not stored

		ConsumoDaoImpl consumoDao = new ConsumoDaoImpl();
		String[] nextRecord;
		while ((nextRecord = csvReader.readNext()) != null) {
			if (!consumoDao.insertConsumo(
					new Consumo(nextRecord[0], nextRecord[1], nextRecord[2], Integer.parseInt(nextRecord[3])))) {
				showError("Se produjo un error a la hora de importar el consumo: " + nextRecord[0] + ", "
						+ nextRecord[1] + ", " + nextRecord[2]);
			}
		}
		reader.close();
		csvReader.close();
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

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}
