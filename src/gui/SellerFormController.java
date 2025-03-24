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

	private List<DataChangeListener> dcls = new ArrayList<>();
	private Seller Seller;
	private SellerService ss;
	private DepartmentService ds;

	public void setServices(SellerService ss, DepartmentService ds) {
		this.ss = ss;
		this.ds = ds;
	}

	public void setSeller(Seller Seller) {
		this.Seller = Seller;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dcls.add(listener);
	}

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

	@FXML
	private ComboBox<Department> comboBoxDepartment;

	@FXML
	private ObservableList<Department> obsList;

	@FXML
	private Label labelErrorName;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button save;

	@FXML
	private Button cancel;

	@FXML
	public void onSaveAction(ActionEvent event) {
		if (Seller == null)
			throw new IllegalStateException("Entity was null");
		if (ss == null)
			throw new IllegalStateException("Service was null");
		try {
			Seller = getFormData();
			ss.saveOrUpdate(Seller);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener dcl : dcls) {
			dcl.onDataChange();
		}
	}

	private Seller getFormData() {

		ValidationException exception = new ValidationException("Validation error");

		if (Seller == null) {
			throw new IllegalStateException("Seller entity was null");
		}
		Seller.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtName.getText() == null || txtName.getText().trim().equals(""))
			exception.addError("name", "Field cannot be empty");

		Seller.setName(txtName.getText());

		if (exception.getErrors().size() > 0)
			throw exception;

		return Seller;
	}

	@FXML
	public void onCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 50);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 50);
		Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		initializeNodes();
	}

	public void updateFormData() {
		if (Seller == null)
			throw new IllegalStateException("Entity was null");

		txtId.setText(String.valueOf(Seller.getId()));
		txtName.setText(Seller.getName());
		txtEmail.setText(Seller.getEmail());
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%.2f", Seller.getBaseSalary()));
		if (Seller.getBirthDate() != null)
			dpBirthDate.setValue(LocalDate.ofInstant(Seller.getBirthDate().toInstant(), ZoneId.systemDefault()));
		if (Seller.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}
		else
		comboBoxDepartment.setValue(Seller.getDepartment());
	}

	public void loadAssociatedObjects() {
		if (ds == null) {
			throw new IllegalStateException("Department Service was null");
		}
		List<Department> list = ds.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

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
