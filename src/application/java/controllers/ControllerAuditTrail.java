package application.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class ControllerAuditTrail {

	@FXML
	void exportarPdf(ActionEvent event) {
		System.out.println("Se ha presionado el botón: exportar a .pdf.");
	}
	
	@FXML
	void exportarCsv(ActionEvent event) {
		System.out.println("Se ha presionado el botón: exportar a .csv.");
	}
	
}
