package application.java;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		// Set this view as the main one
		primaryStage.setOnCloseRequest(e -> Platform.exit());

		Parent root = FXMLLoader.load(getClass().getResource("/application/resources/view/Principal.fxml"));

		// Set stage settings
		Stage mystage = new Stage();
		mystage.setTitle("Aplicación de gestión de consumos");
		mystage.setScene(new Scene(root));
		mystage.setResizable(false);
		mystage.show();
	}

}