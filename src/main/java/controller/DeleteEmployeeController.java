package controller;

import employee.Employee;
import employee.EmployeeManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteEmployeeController {
    @FXML private Button closeButton;
    @FXML private Button deleteButton;
    private Employee employee;
    private EmployeeManager employeeManager = EmployeeManager.instance;


    @FXML
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void deleteEmployee() {
        employeeManager.deleteEmployee(employee);
        Stage stage = (Stage) deleteButton.getScene().getWindow();
        stage.close();
    }

    public void setEmployee(Employee employee) { this.employee = employee; }
}
