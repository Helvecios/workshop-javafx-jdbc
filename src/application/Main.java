package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			//Instanciar um novo objeto "loader" do tipo FXMLLoader
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			Parent parent = loader.load(); //Chama o loader.load() para carregar a view
			Scene mainScene = new Scene(parent); //Cena principal
			primaryStage.setScene(mainScene); //Palco da cena
			primaryStage.setTitle("Sample JavaFX application"); //Título do palco
			primaryStage.show(); //Mostra o palco
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
