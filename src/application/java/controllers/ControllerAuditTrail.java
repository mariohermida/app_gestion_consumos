package application.java.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

import application.java.dao.AuditTrailDao;
import application.java.dao.AuditTrailDaoImpl;
import application.java.model.AuditTrail;
import application.java.model.Usuario_interno;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ControllerAuditTrail {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private TextField textFieldMovs;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnId;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnTipo;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnTabla;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnAccion;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnFechaHora;

	@FXML
	private TableColumn<AuditTrail, String> tableColumnUsuarioBBDD;

	@FXML
	private TableView<AuditTrail> tableViewAuditTrail;

	// User that has just logged into the system
	private Usuario_interno usuarioSession;

	@FXML
	public void initialize() {
		// tableViewAuditTrail setup
		// Once the controller is called, the tableView shows all the info
		// Columns values are assigned to the attributes within AuditTrail class
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
		tableColumnTabla.setCellValueFactory(new PropertyValueFactory<>("tabla"));
		tableColumnAccion.setCellValueFactory(new PropertyValueFactory<>("accion"));
		tableColumnFechaHora.setCellValueFactory(new PropertyValueFactory<>("fechaHora"));
		tableColumnUsuarioBBDD.setCellValueFactory(new PropertyValueFactory<>("usuarioBBDD"));

		updateTableViewAuditTrail();
	}

	@FXML
	void exportarPdf(ActionEvent event) throws IOException {
		List<AuditTrail> auditTrail = tableViewAuditTrail.getItems();
		if (auditTrail.isEmpty()) {
			showError("Audit Trail vacío.");
		} else {
			// Set .pdf file location
			FileChooser fc = new FileChooser();
			fc.getExtensionFilters().add(new ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
			File file = fc.showSaveDialog(null);

			if (file != null) {
				Document document = null;
				PdfDocument pdf;
				pdf = new PdfDocument(new PdfWriter(file));
				document = new Document(pdf);

				// Set title
				Paragraph paragraph = new Paragraph("Audit Trail: Logs").setFontColor(new DeviceRgb(8, 73, 117))
						.setFontSize(20f).setBold();
				paragraph.getAccessibilityProperties().setRole(StandardRoles.H1);
				document.add(paragraph);

				// Set data
				PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
				for (AuditTrail log : auditTrail) {
					document.add(new Paragraph().add(new Text("ID: ").setFont(boldFont)).add(new Text(log.getId())));
					document.add(
							new Paragraph().add(new Text("Tipo: ").setFont(boldFont)).add(new Text(log.getTipo())));
					document.add(
							new Paragraph().add(new Text("Tabla: ").setFont(boldFont)).add(new Text(log.getTabla())));
					document.add(
							new Paragraph().add(new Text("Acción: ").setFont(boldFont)).add(new Text(log.getAccion())));
					document.add(new Paragraph().add(new Text("Fecha y hora: ").setFont(boldFont))
							.add(new Text(log.getFechaHora())));
					document.add(new Paragraph().add(new Text("Usuario BBDD: ").setFont(boldFont))
							.add(new Text(log.getUsuarioBBDD() + "\n\n")));
				}
				document.close();
			}
		}
	}

	@FXML
	void exportarCsv(ActionEvent event) {
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
				dataLines.add(new String[] { "ID", "Tipo", "Tabla", "Acción", "Fecha y hora", "Usuario BBDD" });
				for (AuditTrail log : auditTrail) {
					dataLines.add(new String[] { log.getId(), log.getTipo(), log.getTabla(), log.getAccion(),
							log.getFechaHora(), log.getUsuarioBBDD() });
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

	@FXML
	void exportarCsvUltimosMovs(ActionEvent event) {
		List<AuditTrail> auditTrail = tableViewAuditTrail.getItems();
		if (auditTrail.isEmpty()) {
			showError("Audit Trail vacío.");
		} else {

			List<String[]> dataLines = new ArrayList<>(); // Formatted data to be stored
			// Obtain only the last x movements
			AuditTrail log;
			for (int i = auditTrail.size() - Integer.parseInt(textFieldMovs.getText()); i < auditTrail.size(); i++) {
				log = auditTrail.get(i);
				dataLines.add(new String[] { log.getId(), log.getTipo(), log.getTabla(), log.getAccion(),
						log.getFechaHora(), log.getUsuarioBBDD() });
			}

			File file = new File("/Users/SIC-LN-34/Desktop/M/log_original.csv");
			File csv = new File(file.getAbsolutePath());
			try (PrintWriter pw = new PrintWriter(csv)) {
				dataLines.stream().map(this::convertToCSV).forEach(pw::println);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	private String convertToCSV(String[] data) {
		return Stream.of(data).collect(Collectors.joining(","));
	}

	@FXML
	void refrescar(ActionEvent event) {
		updateTableViewAuditTrail();
	}

	@FXML
	void atras(ActionEvent event) {
		AnchorPane pane = null;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/view/Software.fxml"));
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ControllerSoftware controller = loader.getController();
		controller.setUsuarioSession(usuarioSession);
		rootPane.getChildren().setAll(pane);
	}

	public void setUsuarioSession(Usuario_interno usuarioSession) {
		this.usuarioSession = usuarioSession;
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
