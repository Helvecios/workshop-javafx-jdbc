package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	// Criar uma dependência para o departamento
	private Seller entity;

	// Criar uma dependência do SellerServece com set
	private SellerService service;

	// Criar uma dependência para o departamento Service
	private DepartmentService departmentService;

	// Cria lista com os dados que foram alterados
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	// Declaração dos componentes da tela
	@FXML
	private TextField txtId;

	@FXML
	private TextField txtName;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	// ComboBox
	@FXML
	private ComboBox<Department> comboBoxDepartment;

	// Declaração de erros
	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	// Declaração de botões
	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	// Declarar a lista ObservableList
	private ObservableList<Department> obsList;

	// Implementar o metodo set do entity
	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	// Implementar o metodo set do service SellerService e DepartmentService
	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	// Método para adicinar na lista os dados que foram alterados
	public void subscribeDataChangeListeners(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	// Declarações dos métodos para tratar os eventos dos botões
	@FXML
	public void onBtSaveAction(ActionEvent event) { // Método para salvar o Departamento no BD
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity); // Salvar no BD
			notifyDataChangeListernes(); // Chamada do método para notificar que as alterações foram salvas
			Utils.currentStage(event).close();// Fechar a janela depois de salvar o novo departamento
		} catch (ValidationException e) {
			setErrorMessage(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	// Método para notificar os listeners que as alterações foram salvas
	private void notifyDataChangeListernes() {
		for (DataChangeListener listener : dataChangeListeners) { // For_each
			listener.onDataChanger();
		}

	}

	private Seller getFormData() { // Método para pegar os dados do formulário
		Seller obj = new Seller(); // Criando um obj vazio
		ValidationException exception = new ValidationException("Validation error");// Instanciar uma ValidationException

		obj.setId(Utils.tryParseToInt(txtId.getText())); // Converte para inteiro o que estiver no txtId
		// Verificar se o campo Name está vazio
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Field can´t be empty");
		}
		obj.setName(txtName.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();// Fechar a janela
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes(); // Chama a função para retringir o tipo de dado a ser preenchidos nos campos Id e Name
	}

	// Função para retringir o tipo de dado a ser preenchidos nos campos Id, Name, BaseSalary, Email e BirthDate
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	// Metodo para preencher os dados do formulário
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId())); // Usa o String.valueOf para converter o número em String
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", entity.getBaseSalary())); // Usar String.format para converter o número  para String
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault())); // Para pegar o formato da data do local e horário de onde o usuário está
		}
		if (entity.getDepartment() == null) { //É um novo vendedor sendo cadastrado
			comboBoxDepartment.setValue(entity.getDepartment()); //Faz o comboBox estar selecionado no primeiro elemento dele
		}
		else {
		comboBoxDepartment.setValue(entity.getDepartment()); //Preenche o ComboBox
		}
	}

	// Metodo para carregar os objetos associados (chama o DepartmentService e
	// carrega os departamentos do BD e preenche a lista de departamentos
	public void loadAssociatedObjects() {
		if (departmentService == null) {
			throw new IllegalStateException("DepartmentService was null");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list); // Joga os departamentos para dentro da obsList
		comboBoxDepartment.setItems(obsList);// Setar a "list" como a lista que está associada ao combobox
	}

	// Metodo para imprimir o erro de preenchimento no formulário do View FX
	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		{ // Para percorrer os dados do Map

			// Testa se no conjunto de erros tem algum com a chave "name"
			if (fields.contains("name")) {
				labelErrorName.setText(errors.get("name")); // Seta a mensagem de erro no "labelErrorName"
			}
		}
	}
	
	//Metodo de incialização do ComboBox
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
