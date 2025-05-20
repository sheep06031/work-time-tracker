package controller.employee;

import employee.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.EmployeeService;

import java.io.IOException;

public class Employee_managementController {
    @FXML private TableView<Employee> employeeTableView;
    @FXML private TableColumn<Employee, String> nameColumn, birthColumn, addressColumn, employeedateColumn, phoneNumberColumn;
    @FXML private TextField searchTextField;
    @FXML private ChoiceBox<String> searchChoiceBox;
    @FXML private Button editEmployeeButton, deleteEmployeeButton;

    private final EmployeeService employeeService = new EmployeeService();

    @FXML
    public void initialize() {
        setupTableColumns();
        setupFiltering();
        setupSelectionListener();
        employeeTableView.setItems(employeeService.getFilteredList());
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthColumn.setCellValueFactory(new PropertyValueFactory<>("birth"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        employeedateColumn.setCellValueFactory(new PropertyValueFactory<>("employeedate"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        employeeTableView.getColumns().forEach(col -> col.setReorderable(false));
    }

    private void setupFiltering() {
        searchChoiceBox.getItems().addAll("이름", "생년월일", "전화번호");
        searchChoiceBox.setValue("이름");

        searchTextField.textProperty().addListener((obs, oldVal, newVal) -> {
            String selected = searchChoiceBox.getValue();
            if (selected != null) employeeService.filterBy(selected, newVal);
        });

        searchChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            employeeService.filterBy(newVal, searchTextField.getText());
        });
    }

    private void setupSelectionListener() {
        employeeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean selected = newSel != null;
            editEmployeeButton.setDisable(!selected);
            deleteEmployeeButton.setDisable(!selected);
        });

        editEmployeeButton.setDisable(true);
        deleteEmployeeButton.setDisable(true);
    }

    @FXML
    private void openAddNewEmployeeWindow() throws IOException {
        loadWindow("/com/example/worktimetracker/addNewEmployee.fxml", "사원 추가");
    }

    @FXML
    private void openEditEmployeeWindow() throws IOException {
        Employee emp = employeeTableView.getSelectionModel().getSelectedItem();
        if (emp == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/worktimetracker/editEmployee.fxml"));
        Parent root = loader.load();
        EditEmployeeInfoController controller = loader.getController();
        controller.setEmployee(emp);
        controller.setEmployeeTableView(employeeTableView);
        new StageBuilder(root, "사원 정보 수정").show();
    }

    @FXML
    private void openAskDeleteEmployeeWindow() throws IOException {
        Employee emp = employeeTableView.getSelectionModel().getSelectedItem();
        if (emp == null) return;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/worktimetracker/askDeleteEmployee.fxml"));
        Parent root = loader.load();
        DeleteEmployeeController controller = loader.getController();
        controller.setEmployee(emp);
        new StageBuilder(root, "삭제 확인").show();
    }

    private void loadWindow(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        new StageBuilder(root, title).show();
    }

    static class StageBuilder {
        private final Stage stage = new Stage();

        StageBuilder(Parent root, String title) {
            stage.setScene(new Scene(root));
            stage.setTitle(title);
        }

        void show() {
            stage.show();
        }
    }
}
