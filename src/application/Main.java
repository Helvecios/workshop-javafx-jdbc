package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
			//Instanciar um novo objeto "loader" do tipo FXMLLoader
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			ScrollPane scrollPane = loader.load(); //Chama o loader.load() para carregar a view
			
			scrollPane.setFitToHeight(true); //Para o menu se ajustar automaticamente na altura da tela
			scrollPane.setFitToWidth(true); //Para o menu se ajustar automaticamente na largura da tela
			
			Scene mainScene = new Scene(scrollPane); //Cena principal
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
