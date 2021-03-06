package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainViewController implements Initializable {

	//Item de controle de tela
	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;
	
	
	//Declarar m�todos para cada tipo de a��es dos menus
	@FXML
	private void onMenuItemSellerAction() {
		//Incluindo uma fun��o lambda para inicializar o controlador
		loadView("/gui/SellerList.fxml", (SellerListController controller) -> {
			controller.setSellerService(new SellerService());
			controller.updateTableView();
			
		});
		}
		
		@FXML
		private void onMenuItemDepartmentAction() {
			//Incluindo uma fun��o lambda para inicializar o controlador
			loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
				controller.setDepartmentService(new DepartmentService());
				controller.updateTableView();
				
			});
		}
			
			@FXML
			private void onMenuItemAbouttAction() {
				loadView("/gui/About.fxml", x -> {});
			}	
		
	//M�todo
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
	}
	
	//Instanciar o FXMLLoader 	(fun��o gen�rica tipo <T>)
		private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
				VBox newVbox = loader.load();
				
				Scene mainScene = Main.getMainScene();
				VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
				
				//Para abrir a janela referente ao About
				Node mainMenu = mainVBox.getChildren().get(0);
				mainVBox.getChildren().clear();
				mainVBox.getChildren().add(mainMenu);
				mainVBox.getChildren().addAll(newVbox.getChildren());
				
				//Comando para ativar a fun��o que for passada em Consumer<T> initializinAction)
				T controller = loader.getController();
				//Para executar a a��o "initializinAction"
				initializingAction.accept(controller);
							
			}
			catch (IOException e) {
				Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR );
			}
		}
	
}
