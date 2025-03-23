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

	private List<DataChangeListener> dcls = new ArrayList<>();
	private Seller Seller;
	private SellerService ss;
	
	public void setSellerService(SellerService ss) {
		this.ss = ss;
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
	private Label labelErrorName;
	
	@FXML
	private Button save;
	
	@FXML
	private Button cancel;
	
	@FXML
	public void onSaveAction(ActionEvent event) {
		if(Seller == null) throw new IllegalStateException("Entity was null");
		if(ss == null) throw new IllegalStateException("Service was null");
		try {
		Seller = getFormData();
		ss.saveOrUpdate(Seller);
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

	private Seller getFormData() {
		
		ValidationException exception = new ValidationException("Validation error");
		
		if (Seller == null) {
	        throw new IllegalStateException("Seller entity was null");
	    }
	    Seller.setId(Utils.tryParseToInt(txtId.getText()));
	    
	    if(txtName.getText() == null || 
	    		txtName.getText().trim().equals(""))
	    	exception.addError("name", "Field cannot be empty");
	    
	    	Seller.setName(txtName.getText());
	    	
	    if(exception.getErrors().size() > 0) throw exception;
	    
	    return Seller;
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
		if(Seller == null) throw new IllegalStateException("Entity was null");
		
		txtId.setText(String.valueOf(Seller.getId()));
		txtName.setText(Seller.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
