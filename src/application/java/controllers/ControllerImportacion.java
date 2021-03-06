package application.java.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Optional;
import java.util.Properties;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import application.java.dao.AplicacionDaoImpl;
import application.java.dao.ConsumoDaoImpl;
import application.java.dao.UsuarioDaoImpl;
import application.java.model.Aplicacion;
import application.java.model.Consumo;
import application.java.model.Usuario;
import application.java.model.Usuario_interno;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * Class that manages all the events occurred in Importacion.fxml
 */
public class ControllerImportacion {

	@FXML
	private AnchorPane rootPane;

	@FXML
	private Button inicializacionButton;

	@FXML
	private Button importarEntity1Button;

	@FXML
	private Button importarEntity2Button;

	@FXML
	private Button importarEntity3Button;

	// Object for retrieving the values stored in file
	private Properties properties = new Properties();

	// User that has just logged into the system
	private Usuario_interno usuarioSession;

	@FXML
	public void initialize() {
		// Title name for buttons are retrieved from file
		try {
			//properties.load(new FileInputStream(new File("C:\\Users\\SIC-LN-34\\Desktop\\M\\titles.properties")));
			properties.load(new FileInputStream(new File("conf/titles.properties")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		importarEntity1Button.setText("Importar " + properties.getProperty("entity1"));
		importarEntity2Button.setText("Importar " + properties.getProperty("entity2"));
		importarEntity3Button.setText("Importar " + properties.getProperty("entity3"));
	}

	@FXML
	void inicializar(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmaci?n");
		alert.setContentText("?Est?s seguro de que deseas eliminar todas las aplicaciones y los consumos?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			AplicacionDaoImpl aplicacionDaoImpl = new AplicacionDaoImpl();
			aplicacionDaoImpl.deleteAplicaciones("", "", "", Byte.MIN_VALUE);
			ConsumoDaoImpl consumoDaoImpl = new ConsumoDaoImpl();
			consumoDaoImpl.deleteConsumos("", "", "", Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
	}

	@FXML
	void importarEntity1(ActionEvent event) throws IOException, CsvValidationException {
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
				showError("Se produjo un error a la hora de importar la aplicaci?n: " + nextRecord[0]);
			}
		}
		reader.close();
		csvReader.close();
	}

	@FXML
	void importarEntity2(ActionEvent event) throws IOException, CsvValidationException {
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
	void importarEntity3(ActionEvent event) throws IOException, CsvValidationException {
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/resources/view/Software.fxml"));
		try {
			pane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ControllerSoftware controllerSoftware = loader.getController();
		controllerSoftware.setUsuarioSession(usuarioSession);
		rootPane.getChildren().setAll(pane);
	}

	public void setUsuarioSession(Usuario_interno usuarioSession) {
		this.usuarioSession = usuarioSession;
	}

	public void showError(String message) {
		Alert alert = new Alert(AlertType.ERROR, message);
		alert.showAndWait();
	}

}
