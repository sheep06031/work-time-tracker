package com.example.worktimetracker;

import employee.Employee;
import employee.EmployeeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class EditEmployeeInfoController {
    @FXML private TextField editNameField;
    @FXML private TextField editBirthTextField;
    @FXML private TextField editAddressTextField;
    @FXML private TextField editEmployeeDateTextField;
    @FXML private TextField editPhoneNumberTextField;
    @FXML private Button editEmployeeButton;
    private Employee employee;
    private EmployeeManager employeeManager = EmployeeManager.instance;
    private TableView<Employee> employeeTableView;


    @FXML
    private void editEmployeeButtonClick(ActionEvent event) {
        employeeManager.editEmployee(employee, editNameField.getText(),editBirthTextField.getText()
        , editAddressTextField.getText(), editEmployeeDateTextField.getText(),editPhoneNumberTextField.getText() );
        employeeTableView.refresh();
        Stage stage = (Stage) editEmployeeButton.getScene().getWindow();
        stage.close();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        editNameField.setText(employee.getName());
        editBirthTextField.setText(employee.getBirth());
        editAddressTextField.setText(employee.getAddress());
        editEmployeeDateTextField.setText(employee.getEmployeedate());
        editPhoneNumberTextField.setText(employee.getPhoneNumber());
    }

    public void setEmployeeTableView(TableView<Employee> tableView) { this.employeeTableView = tableView; }
}
