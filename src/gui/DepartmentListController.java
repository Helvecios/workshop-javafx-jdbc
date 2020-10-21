package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	//Criar refer�ncias para os componentes da tela do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	
	//M�todo de tratamento do bot�o
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
	
	//M�todo auxiliar initializeNodes para iniciar algum componente na tela
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes(); 
		
	}
	
	//M�todo para iniciar o comportamento das colunas
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		//Faz o TableView preencher toda a janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}
	
	

}
