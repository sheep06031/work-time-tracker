package controller;

import employee.Employee;
import employee.EmployeeManager;
import javafx.collections.transformation.FilteredList;
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
    @FXML private Button editEmployeeButton;
    @FXML private Button deleteEmployeeButton;
    @FXML private TextField searchTextField;
    @FXML private ChoiceBox<String> searchChoiceBox;
    private FilteredList<Employee> flEmployee = new FilteredList<>(EmployeeManager.getEmployeeList(), p -> true);

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        employeedateColumn.setCellValueFactory(new PropertyValueFactory<>("employeedate"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        searchChoiceBox.getItems().addAll("이름", "생년월일", "전화번호");
        searchChoiceBox.setValue("이름");

        employeeTableView.setItems(flEmployee);

        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            String selected = searchChoiceBox.getValue();
            if (selected == null) return;
            String keyword = newValue.toLowerCase().trim();
            switch (selected) {
                case "이름" -> flEmployee.setPredicate(e -> e.getName().toLowerCase().contains(keyword));
                case "생년월일" -> flEmployee.setPredicate(e -> e.getBirth().toLowerCase().contains(keyword));
                case "전화번호" -> flEmployee.setPredicate(e -> e.getPhoneNumber().toLowerCase().contains(keyword));
            }
        });

        searchChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> searchTextField.setText(searchTextField.getText()));

        boolean noSelection = employeeTableView.getSelectionModel().getSelectedItem() == null;
        editEmployeeButton.setDisable(noSelection);
        deleteEmployeeButton.setDisable(noSelection);

        employeeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean disable = newSel == null;
            editEmployeeButton.setDisable(disable);
            deleteEmployeeButton.setDisable(disable);
        });
        employeeTableView.getColumns().forEach(col -> col.setReorderable(false));
    }

    @FXML
    private void openAddNewEmployeeWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/worktimetracker/addNewEmployee.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("사원 추가");
        stage.show();
    }

    @FXML
    private void openEditEmployeeWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/worktimetracker/editEmployee.fxml"));
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

    @FXML
    private void openAskDeleteEmployeeWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/worktimetracker/askDeleteEmployee.fxml"));
        Parent root = loader.load();
        DeleteEmployeeController controller = loader.getController();
        Employee emp = employeeTableView.getSelectionModel().getSelectedItem();
        controller.setEmployee(emp);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
