package application.java.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import application.java.dao.AuditTrailDao;
import application.java.dao.AuditTrailDaoImpl;
import application.java.model.AuditTrail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ControllerAuditTrail {

	@FXML
	private TableColumn<AuditTrail, String> tableColumnId;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnTipo;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnAccion;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnFechaHora;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnUsuarioBBDD;

	@FXML
	private TableView<AuditTrail> tableViewAuditTrail;

	@FXML
	public void initialize() {
		// tableViewAuditTrail setup
		// Once the controller is called, the tableView shows all the info
		// Columns values are assigned to the attributes within AuditTrail class
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		tableColumnAccion.setCellValueFactory(new PropertyValueFactory<>("accion"));
		tableColumnFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
		tableColumnUsuarioBBDD.setCellValueFactory(new PropertyValueFactory<>("usuarioBBDD"));

		updateTableViewAuditTrail();
	}

	@FXML
	void exportarPdf(ActionEvent event) throws FileNotFoundException {
		System.out.println("Se ha presionado el botón: exportar a .pdf.");
		/*Document document = null;
		PdfDocument pdf;
		pdf = new PdfDocument(new PdfWriter("C:/Users/SIC-LN-60/Desktop/mypdf.pdf"));
		document = new Document(pdf);
		String line = "Hello! Welcome to iTextPdf";
		document.add(new Paragraph(line));
		document.close();*/
	}

	@FXML
	void exportarCsv(ActionEvent event) {
		System.out.println("Se ha presionado el botón: exportar a .csv.");

		List<AuditTrail> auditTrail = tableViewAuditTrail.getItems();
		if (auditTrail.isEmpty()) {
			showError("Audit Trail vacío.");
		} else {
			// Set .csv file location
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("CSV files (*.csv)", "*.csv"));
			File file = fc.showSaveDialog(null);

			if (file != null) {
				List<String[]> dataLines = new ArrayList<>(); // Formatted data to be stored
				// For every existing log an array of strings is created
				// First line includes the headers of the stored information
				dataLines.add(new String[] { "ID", "Tipo", "Acción", "Fecha y hora", "Usuario BBDD" });
				for (AuditTrail log : auditTrail) {
					dataLines.add(new String[] { log.getId(), log.getTipo(), log.getAccion(), log.getFechaHora(),
							log.getUsuarioBBDD() });
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

	@FXML
	void refrescar(ActionEvent event) {
		System.out.println("Se ha presionado el botón: refrescar.");
		updateTableViewAuditTrail();
	}

	/**
	 * Updates the tableViewAuditTrail element.
	 */
	private void updateTableViewAuditTrail() {
		AuditTrailDao auditTrailDao = new AuditTrailDaoImpl();
		List<AuditTrail> auditTrail = auditTrailDao.getAllLogs();

		// List is converted into ObservableList type
		ObservableList<AuditTrail> observableList = FXCollections.observableArrayList();
		for (AuditTrail log : auditTrail) {
			observableList.add(log);
		}

		// Show items
		tableViewAuditTrail.setItems(observableList);
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}
