package com.example.worktimetracker;

import employee.Employee;
import employee.EmployeeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class employee_managementController {

    @FXML private TableView<Employee> employeeTableView;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> birthColumn;
    @FXML private TableColumn<Employee, String> addressColumn;
    @FXML private TableColumn<Employee, String> employeedateColumn;
    @FXML private TableColumn<Employee, String> phoneNumberColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        employeedateColumn.setCellValueFactory(new PropertyValueFactory<>("employeedate"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        employeeTableView.setItems(EmployeeManager.instance.getEmployeeList());
    }

    @FXML
    private void openAddNewEmployeeWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addNewEmployee.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("사원 추가");
        stage.show();
    }
}
