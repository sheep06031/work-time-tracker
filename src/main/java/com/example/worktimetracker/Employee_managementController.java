package com.example.worktimetracker;

import employee.Employee;
import employee.EmployeeManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;

public class Employee_managementController {

    @FXML private TableView<Employee> employeeTableView;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> birthColumn;
    @FXML private TableColumn<Employee, String> addressColumn;
    @FXML private TableColumn<Employee, String> employeedateColumn;
    @FXML private TableColumn<Employee, String> phoneNumberColumn;
    @FXML private Button editEmplyeeButton;



    @FXML
    public void initialize() {
        employeeTableView.setItems(EmployeeManager.getEmployeeList());
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        employeedateColumn.setCellValueFactory(new PropertyValueFactory<>("employeedate"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        employeeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            editEmplyeeButton.setDisable(newSel == null);
        });
        editEmplyeeButton.setDisable(employeeTableView.getSelectionModel().getSelectedItem() == null);
    }

        @FXML
        private void openAddNewEmployeeWindow () throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addNewEmployee.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("사원 추가");
            stage.show();
        }

        @FXML
        private void openEditEmployeeWindow () throws IOException {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("editEmployee.fxml"));
            Parent root = loader.load();
            EditEmployeeInfoController controller = loader.getController();
            Employee emp = employeeTableView.getSelectionModel().getSelectedItem();
            controller.setEmployee(emp);
            controller.setEmployeeTableView(employeeTableView);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("사원 정보 수정");
            stage.show();
        }
}

