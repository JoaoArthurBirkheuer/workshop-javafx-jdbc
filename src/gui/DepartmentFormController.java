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
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private List<DataChangeListener> dcls = new ArrayList<>();
	private Department department;
	private DepartmentService ds;
	
	public void setDepartmentService(DepartmentService ds) {
		this.ds = ds;
	}
	
	public void setDepartment(Department department) {
		this.department = department;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dcls.add(listener);
	}
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button save;
	
	@FXML
	private Button cancel;
	
	@FXML
	public void onSaveAction(ActionEvent event) {
		if(department == null) throw new IllegalStateException("Entity was null");
		if(ds == null) throw new IllegalStateException("Service was null");
		try {
		department = getFormData();
		ds.saveOrUpdate(department);
		notifyDataChangeListeners();
		Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
	}
	
	private void notifyDataChangeListeners() {
		for(DataChangeListener dcl : dcls) {
			dcl.onDataChange();
		}
	}

	private Department getFormData() {
		
		ValidationException exception = new ValidationException("Validation error");
		
		if (department == null) {
	        throw new IllegalStateException("Department entity was null");
	    }
	    department.setId(Utils.tryParseToInt(txtId.getText()));
	    
	    if(txtName.getText() == null || 
	    		txtName.getText().trim().equals(""))
	    	exception.addError("name", "Field cannot be empty");
	    
	    	department.setName(txtName.getText());
	    	
	    if(exception.getErrors().size() > 0) throw exception;
	    
	    return department;
	}

	@FXML
	public void onCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		initializeNodes();
	}
	
	public void updateFormData() {
		if(department == null) throw new IllegalStateException("Entity was null");
		
		txtId.setText(String.valueOf(department.getId()));
		txtName.setText(department.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
