package controller.employee;

import employee.Employee;
import employee.EmployeeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        String name = editNameField.getText().trim();
        String birth = editBirthTextField.getText().trim();
        String address = editAddressTextField.getText().trim();
        String employmentDate = editEmployeeDateTextField.getText().trim();
        String phone = editPhoneNumberTextField.getText().trim();

        if (name.isEmpty() || birth.isEmpty() || address.isEmpty() || employmentDate.isEmpty() || phone.isEmpty()) {
            showAlert("모든 항목을 입력해주세요!");
            return;
        }

        employeeManager.editEmployee(employee, name, birth, address, employmentDate, phone);


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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("입력 오류");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setEmployeeTableView(TableView<Employee> tableView) { this.employeeTableView = tableView; }
}
