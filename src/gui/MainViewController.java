package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

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
		System.out.println("onMenuItemSellerAction");
		}
		
		@FXML
		private void onMenuItemDepartmentAction() {
			System.out.println("onMenuItemDepartmentAction");
		}
			
			@FXML
			private void onMenuItemAbouttAction() {
				System.out.println("onMenuItemAboutAction");
			}	
		
	//M�todo
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	
	

}
