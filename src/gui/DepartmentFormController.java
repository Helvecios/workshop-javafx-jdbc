package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	//Criar uma dependência para o departamento
	private Department entity;
	
	//Criar uma dependência do DepartmentServece com set
	private DepartmentService service;
	
	//Declaração dos componentes da tela
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	//Implementar o metodo set do entity
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	//Implementar o metodo set do service
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	//Declarações dos métodos para tratar os eventos dos botões
	@FXML
	public void onBtSaveAction(ActionEvent event) { //Método para salvar o Departamento no BD
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
		entity = getFormData();
		service.saveOrUpdate(entity); //Salvar no BD
		Utils.currentStage(event).close();//Fechar a janela depois de salvar o novo departamento
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Department getFormData() { //Método para pegar os dados do formulário
		Department obj = new Department(); //Criando um obj vazio
		
		obj.setId(Utils.tryParseToInt(txtId.getText())); //Converte para inteiro o que estiver no txtId
		obj.setName(txtName.getText());
		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();//Fechar a janela
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes(); //Chama a função para retringir o tipo de dado a ser preenchidos nos campos Id e Name
	}
	
	//Função para retringir o tipo de dado a ser preenchidos nos campos Id e Name
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	//Metodo para preencher os dados do formulário
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		
		txtId.setText(String.valueOf(entity.getId())); //Usa o String.valueOf para converter o número em String
		txtName.setText(entity.getName());
	}
}
