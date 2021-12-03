package application.java.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import application.java.dao.ConsumoDaoImpl;
import application.java.model.Consumo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * Class that manages all the events occurred in Principal.fxml
 */
public class ControllerPrincipal {

	@FXML
	void aplicacionesButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Aplicaciones.");
		openNewWindow("Aplicaciones", "Aplicaciones");
	}

	@FXML
	void usuariosButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Usuarios.");
		openNewWindow("Usuarios", "Usuarios");
	}

	@FXML
	void consumosButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Consumos.");
		openNewWindow("Consumos", "Consumos de usuarios");
	}

	@FXML
	void informesButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Informes.");
		// openNewWindow("Informes", "Informes");

		// Set .csv file location
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("CSV files (*.csv)", "*.csv"));
		File file = fc.showSaveDialog(null);

		if (file != null) {
			List<String[]> dataLines = new ArrayList<>(); // Formatted data to be stored
			List<Consumo> consumos = new ArrayList<>();
			ConsumoDaoImpl consumoDao = new ConsumoDaoImpl();
			consumos = consumoDao.getAllConsumos();
			// For every existing consumo an array of strings is created
			// First line includes the headers of the stored information
			dataLines.add(new String[] { "Usuario ID", "Aplicacion ID", "Mes", "Consumo (MB)" });
			for (Consumo consumo : consumos) {
				dataLines.add(new String[] { consumo.getIdUsuario(), consumo.getIdAplicacion(),
						consumo.getMes(), Integer.toString(consumo.getConsumo()) });
			}

			File csv = new File(file.getAbsolutePath());
			try (PrintWriter pw = new PrintWriter(csv)) {
				dataLines.stream().map(this::convertToCSV).forEach(pw::println);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private String convertToCSV(String[] data) {
		return Stream.of(data).map(this::escapeSpecialCharacters).collect(Collectors.joining(","));
	}

	/**
	 * Special treatment for possible errors introduced while typing data
	 * 
	 * @param data Data to be escaped
	 * @return
	 */
	private String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}

	@FXML
	void auditTrailButton(ActionEvent event) {
		System.out.println("Se ha presionado el bot�n: Audit Trail.");
		openNewWindow("AuditTrail", "Audit Trail");
	}

	/**
	 * Shows a new window according to the .fxml file called fileName
	 * 
	 * @param fileName
	 * @param titleName
	 */
	private void openNewWindow(String fileName, String titleName) {
		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("../../resources/view/" + fileName + ".fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set stage settings
		Scene scene = new Scene(root);
		Stage mystage = new Stage();
		mystage.setTitle(titleName);
		mystage.setScene(scene);
		mystage.setResizable(false);
		mystage.show();
	}

}
