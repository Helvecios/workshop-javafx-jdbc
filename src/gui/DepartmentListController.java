package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
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
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDialogForm("/gui/DepartmentForm.fxml", parentStage);
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

	//Metodo para instaciar a janela de di�logo
	private void createDialogForm(String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); //Para carregar a View
			
			//Para carregar a janela do formulario sobre outra janela, para preencher o formul�rio
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data"); //Para configura o t�tulo da janela
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); //A janela n�o pode ser redimensionada
			dialogStage.initOwner(parentStage); // Quem � o pai desta janela? � o "parentStage"
			dialogStage.initModality(Modality.WINDOW_MODAL); //Enquanto vc n�o fechar esta janela, vc n�o pode acessar a janela anterior
			dialogStage.showAndWait();
			
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}
}
