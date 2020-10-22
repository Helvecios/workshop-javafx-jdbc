package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
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

	// Declarar uma dependência
	private DepartmentService service;

	// Criar referências para os componentes da tela do DepartmentList
	@FXML
	private TableView<Department> tableViewDepartment;

	@FXML
	private TableColumn<Department, Integer> tableColumnId;

	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private Button btNew;

	private ObservableList<Department> obsList;

	// Método de tratamento do botão
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department(); // Instaciar um departamento vazio, o botão é para cadastrar novo
											// departamento
		createDialogForm(obj, "/gui/DepartmentForm.fxml", parentStage);
	}

	// Método para injetar dependência
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	// Método auxiliar initializeNodes para iniciar algum componente na tela
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();

	}

	// Método para iniciar o comportamento das colunas
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
		initEditButtons(); //Chama o Método para acrescentar um novo botão com o texto "edit" em cada linha da tabela
	}

	// Metodo para instaciar a janela de diálogo
	private void createDialogForm(Department obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // Para carregar a View

			// Para injetar o novo departamento no controlador da tela de formulario
			DepartmentFormController controller = loader.getController(); // Pegar referencia para o Controlador

			controller.setDepartment(obj); // Injetar no controlador o novo departamento
			controller.setDepartmentService(new DepartmentService());// Injetar o departamento de serviço
			controller.subscribeDataChangeListeners(this); // Executa o método para atualizar od dados
			controller.updateFormData(); // Para carregar os dados do obj no formulário

			// Para carregar a janela do formulario sobre outra janela, para preencher o
			// formulário
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data"); // Para configura o título da janela
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false); // A janela não pode ser redimensionada
			dialogStage.initOwner(parentStage); // Quem é o pai desta janela? É o "parentStage"
			dialogStage.initModality(Modality.WINDOW_MODAL); // Enquanto vc não fechar esta janela, vc não pode acessar
																// a janela anterior
			dialogStage.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	// Método para atualizar os dados que foram alterados
	@Override
	public void onDataChanger() {
		updateTableView();
	}

	// Método para acrescentar um novo botão com o texto "edit" em cada linha da tabela
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
}
