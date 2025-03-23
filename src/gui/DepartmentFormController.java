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

	private Department department;
	private DepartmentService ds;
	
	public void setDepartmentService(DepartmentService ds) {
		this.ds = ds;
	}
	
	public void setDepartment(Department department) {
		this.department = department;
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
		Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private Department getFormData() {
		if (department == null) {
	        throw new IllegalStateException("Department entity was null");
	    }
	    department.setId(Utils.tryParseToInt(txtId.getText()));  // Keep using existing instance
	    department.setName(txtName.getText());
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

}
