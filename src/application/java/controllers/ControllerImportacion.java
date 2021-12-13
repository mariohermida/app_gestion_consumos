package application.java.controllers;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

/**
 * Class that manages all the events occurred in Importacion.fxml
 */
public class ControllerImportacion {

	@FXML
	void importarAplicaciones(ActionEvent event) throws IOException, URISyntaxException, CsvException {
		System.out.println("Se ha presionado el botón: importarAplicaciones.");

		FileChooser fc = new FileChooser();
		fc.setTitle("Seleccione el archivo a importar");
		File file = fc.showOpenDialog(null);

		Reader reader = new FileReader(file);

		CSVReader csvReader = new CSVReader(reader);
		String[] nextRecord;
		csvReader.readNext(); // Headers are skipped
		while ((nextRecord = csvReader.readNext()) != null) {
            for (String cell : nextRecord) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
	    reader.close();
	    csvReader.close();
	}

	@FXML
	void importarUsuarios(ActionEvent event) {
		System.out.println("Se ha presionado el botón: importarUsuarios.");
	}

	@FXML
	void importarConsumos(ActionEvent event) {
		System.out.println("Se ha presionado el botón: importarConsumos.");
	}

}
