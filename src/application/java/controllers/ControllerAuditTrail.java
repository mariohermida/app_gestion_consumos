package application.java.controllers;

import java.util.List;
import application.java.dao.AuditTrailDao;
import application.java.dao.AuditTrailDaoImpl;
import application.java.model.AuditTrail;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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
	void exportarPdf(ActionEvent event) {
		System.out.println("Se ha presionado el botón: exportar a .pdf.");
	}

	@FXML
	void exportarCsv(ActionEvent event) {
		System.out.println("Se ha presionado el botón: exportar a .csv.");
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

}
