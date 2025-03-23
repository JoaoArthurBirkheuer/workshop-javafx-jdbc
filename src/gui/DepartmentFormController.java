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

	private Department department;
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
	public void onSaveAction() {
		System.out.println("1");
	}
	
	@FXML
	public void onCancelAction() {
		System.out.println("1");
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
