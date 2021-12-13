package application.java.controllers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import application.java.dao.AplicacionDaoImpl;
import application.java.model.Aplicacion;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

/**
 * Class that manages all the events occurred in Importacion.fxml
 */
public class ControllerImportacion {

	@FXML
	void importarAplicaciones(ActionEvent event) throws IOException, URISyntaxException, CsvException {
		System.out.println("Se ha presionado el bot�n: importarAplicaciones.");

		FileChooser fc = new FileChooser();
		fc.setTitle("Seleccione el archivo a importar:");
		File file = fc.showOpenDialog(null);

		Reader reader = new FileReader(file);

		CSVReader csvReader = new CSVReader(reader);
		csvReader.readNext(); // Headers are not stored

		AplicacionDaoImpl aplicacionDao = new AplicacionDaoImpl();
		String[] nextRecord;
		while ((nextRecord = csvReader.readNext()) != null) {
			aplicacionDao.insertAplicacion(
					new Aplicacion(nextRecord[0], nextRecord[1], nextRecord[2], Byte.parseByte(nextRecord[3])));
		}
		reader.close();
		csvReader.close();
	}

	@FXML
	void importarUsuarios(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: importarUsuarios.");
	}

	@FXML
	void importarConsumos(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: importarConsumos.");
	}

}
