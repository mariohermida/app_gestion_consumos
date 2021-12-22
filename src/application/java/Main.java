package application.java;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	// It points to the main window, in order to be visible by others stages
	//public static Stage mainStage;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		// Set this view as the main one
		primaryStage.setOnCloseRequest(e -> Platform.exit());

		Parent root = null;
		try {
			root = FXMLLoader.load(getClass().getResource("/application/resources/view/Principal.fxml"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Set stage settings
		Stage mystage = new Stage();
		mystage.setTitle("Aplicación de gestión de consumos");
		mystage.setScene(new Scene(root));
		mystage.setResizable(false);
		mystage.show();

		// Set mainStage pointer to the main window (this)
		//mainStage = primaryStage;
	}

}