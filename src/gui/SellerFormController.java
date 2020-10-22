package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
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
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable {

	// Criar uma dependência para o departamento
	private Seller entity;

	// Criar uma dependência do SellerServece com set
	private SellerService service;

	// Cria lista com os dados que foram alterados
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	// Declaração dos componentes da tela
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

	// Implementar o metodo set do entity
	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	// Implementar o metodo set do service
	public void setSellerService(SellerService service) {
		this.service = service;
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
		ValidationException exception = new ValidationException("Validation error");// Instanciar uma
																					// ValidationException

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
		initializeNodes(); // Chama a função para retringir o tipo de dado a ser preenchidos nos campos Id
							// e Name
	}

	// Função para retringir o tipo de dado a ser preenchidos nos campos Id e Name
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}

	// Metodo para preencher os dados do formulário
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}

		txtId.setText(String.valueOf(entity.getId())); // Usa o String.valueOf para converter o número em String
		txtName.setText(entity.getName());
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

}
