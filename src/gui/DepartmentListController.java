package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {

	// Declarar uma depend�ncia
	private DepartmentService service;

	// Criar refer�ncias para os componentes da tela do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	// M�todo de tratamento do bot�o
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department(); // Instaciar um departamento vazio, o bot�o � para cadastrar novo
											// departamento
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	// M�todo para injetar depend�ncia
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	// M�todo auxiliar initializeNodes para iniciar algum componente na tela
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	// M�todo para iniciar o comportamento das colunas
	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));

		// Faz o TableView preencher toda a janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

	// Metodo para carregar os departamentos na obsList
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Department> list = service.findAll(); // Para recuperar todos os departamentos
		obsList = FXCollections.observableArrayList(list);// Carrega a lista dentro do obsList
		tableViewDepartment.setItems(obsList);// Carrega os dados da obsList para mostrar na tela View FX
		initEditButtons(); // Chama o M�todo para acrescentar um novo bot�o com o texto "edit" em cada  linha da tabela
		initRemoveButtons(); // Chama o M�todo para acrescentar um novo bot�o com o texto "remove" em cada  linha da tabela
	}

	// Metodo para instaciar a janela de di�logo
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // Para carregar a View

			// Para injetar o novo departamento no controlador da tela de formulario
			DepartmentFormController controller = loader.getController(); // Pegar referencia para o Controlador

			controller.setDepartment(obj); // Injetar no controlador o novo departamento
			controller.setDepartmentService(new DepartmentService());// Injetar o departamento de servi�o
			controller.subscribeDataChangeListeners(this); // Executa o m�todo para atualizar od dados
			controller.updateFormData(); // Para carregar os dados do obj no formul�rio

			// Para carregar a janela do formulario sobre outra janela, para preencher o
			// formul�rio
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data"); // Para configura o t�tulo da janela
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); // A janela n�o pode ser redimensionada
			dialogStage.initOwner(parentStage); // Quem � o pai desta janela? � o "parentStage"
			dialogStage.initModality(Modality.WINDOW_MODAL); // Enquanto vc n�o fechar esta janela, vc n�o pode acessar
																// a janela anterior
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace(); //Aparece no console as poss�veis mensagens de erro 
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	// M�todo para atualizar os dados que foram alterados
	@Override
	public void onDataChanger() {
		updateTableView();
	}

	// M�todo para acrescentar um novo bot�o com o texto "edit" em cada linha da tabela
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// M�todo para acrescentar um novo bot�o com o texto "remove" em cada linha da tabela
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	//M�todo para remover um departamento
	private void removeEntity(Department obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		if (result.get()== ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj); // Remove o departamento
				updateTableView(); //Faz a atualiza��o da tabela ap�s remo��o do departamento
			}
			catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
}
