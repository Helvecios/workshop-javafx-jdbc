package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	//Criar uma dependência para o departamento
	private Department entity;
	

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
	
	//Declarações dos métodos para tratar os eventos dos botões
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
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
