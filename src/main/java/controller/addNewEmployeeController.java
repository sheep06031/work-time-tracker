package controller;

import employee.Employee;
import employee.EmployeeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class addNewEmployeeController {
    private EmployeeManager employeeManager = EmployeeManager.instance;
    @FXML
    private TextField nameField;
    @FXML
    private TextField birthTextField;
    @FXML
    private TextField adressTextField;
    @FXML
    private TextField employeeDateTextField;
    @FXML
    private TextField phoneNumberTextField;
    @FXML
    private Button addEmployeeButton;

    @FXML
    private void addEmployeeButtonClick(ActionEvent event) {

        Employee employee = new Employee(nameField.getText(), birthTextField.getText(), adressTextField.getText(),
                employeeDateTextField.getText(), phoneNumberTextField.getText());
        employeeManager.addEmployee(employee);
        Stage stage = (Stage) addEmployeeButton.getScene().getWindow();
        stage.close();
    }
}
