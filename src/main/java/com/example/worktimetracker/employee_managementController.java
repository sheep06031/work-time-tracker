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

public class employee_managementController {

    @FXML private TableView<Employee> employeeTableView;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> birthColumn;
    @FXML private TableColumn<Employee, String> addressColumn;
    @FXML private TableColumn<Employee, String> employeedateColumn;
    @FXML private TableColumn<Employee, String> phoneNumberColumn;
    @FXML private TableColumn<Employee, Void> editColumn;


    @FXML
    public void initialize() {
        employeeTableView.setItems(EmployeeManager.getEmployeeList());
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        employeedateColumn.setCellValueFactory(new PropertyValueFactory<>("employeedate"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        employeeTableView.setItems(EmployeeManager.getEmployeeList());


        editColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editButton = new Button("정보 수정");
            {
                editButton.setOnAction(event -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    try {
                        openEditEmployeeWindow(emp);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
            });
        }


    @FXML
    private void openAddNewEmployeeWindow() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addNewEmployee.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("사원 추가");
        stage.show();
    }

    @FXML
    private void openEditEmployeeWindow(Employee emp) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("editEmployee.fxml"));
        Parent root = loader.load();
        EditEmployeeInfoController controller = loader.getController();
        controller.setEmployee(emp);
        controller.setEmployeeTableView(employeeTableView);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("사원 정보 수정");
        stage.show();
    }



}
