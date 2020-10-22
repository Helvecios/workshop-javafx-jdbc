package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	// Declarar uma dependência
	private SellerService service;

	// Atributos
	// Criar referências para os componentes da tela do SellerList
	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId; // Id

	@FXML
	private TableColumn<Seller, String> tableColumnName; // Name

	@FXML
	private TableColumn<Seller, String> tableColumnEmail; // Email

	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate; // BirthDate

	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary; // BaseSalary

	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT; // Edit

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE; // Remove

	@FXML
	private Button btNew;

	private ObservableList<Seller> obsList;

	// Método de tratamento do botão
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller obj = new Seller(); // Instaciar um departamento vazio, o botão é para cadastrar novo
									// departamento
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	// Método para injetar dependência
	public void setSellerService(SellerService service) {
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
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy"); // Formata a data
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2); // Formata numero com duas casas decimais

		// Faz o TableView preencher toda a janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	// Metodo para carregar os departamentos na obsList
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		List<Seller> list = service.findAll(); // Para recuperar todos os departamentos
		obsList = FXCollections.observableArrayList(list);// Carrega a lista dentro do obsList
		tableViewSeller.setItems(obsList);// Carrega os dados da obsList para mostrar na tela View FX
		initEditButtons(); // Chama o Método para acrescentar um novo botão com o texto "edit" em cada
							// linha da tabela
		initRemoveButtons(); // Chama o Método para acrescentar um novo botão com o texto "remove" em cada
								// linha da tabela
	}

	// Metodo para instaciar a janela de diálogo
	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // Para carregar a View

			// Para injetar o novo departamento no controlador da tela de formulario
			SellerFormController controller = loader.getController(); // Pegar referencia para o Controlador

			controller.setSeller(obj); // Injetar no controlador o novo departamento
			controller.setSellerService(new SellerService());// Injetar o departamento de serviço
			controller.subscribeDataChangeListeners(this); // Executa o método para atualizar od dados
			controller.updateFormData(); // Para carregar os dados do obj no formulário

			// Para carregar a janela do formulario sobre outra janela, para preencher o
			// formulário
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Seller data"); // Para configura o título da janela
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

	// Método para acrescentar um novo botão com o texto "edit" em cada linha da
	// tabela
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	// Método para acrescentar um novo botão com o texto "remove" em cada linha da
	// tabela
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
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

	// Método para remover um departamento
	private void removeEntity(Seller obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		if (result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(obj); // Remove o departamento
				updateTableView(); // Faz a atualização da tabela após remoção do departamento
			} catch (DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

}
