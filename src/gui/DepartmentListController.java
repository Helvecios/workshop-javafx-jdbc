package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {
	
	//Declarar uma depend�ncia
	private DepartmentService service;
	

	//Criar refer�ncias para os componentes da tela do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	
	//M�todo de tratamento do bot�o
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}
		
	//M�todo para injetar depend�ncia
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
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
	
	//Metodo para carregar os departamentos na obsList
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); //Para recuperar todos os departamentos
		obsList = FXCollections.observableArrayList(list);//Carrega a lista dentro do obsList
		tableViewDepartment.setItems(obsList);//Carrega os dados da obsList para mostrar na tela View FX
	}

}
