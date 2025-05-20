package controller.employee;

import employee.Employee;
import employee.EmployeeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
        String name = nameField.getText().trim();
        String birth = birthTextField.getText().trim();
        String address = adressTextField.getText().trim();
        String employmentDate = employeeDateTextField.getText().trim();
        String phone = phoneNumberTextField.getText().trim();

        if (name.isEmpty() || birth.isEmpty() || address.isEmpty() || employmentDate.isEmpty() || phone.isEmpty()) {
            showAlert("모든 항목을 입력해주세요!");
            return;
        }

        Employee employee = new Employee(name, birth, address, employmentDate, phone);
        employeeManager.addEmployee(employee);

        Stage stage = (Stage) addEmployeeButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("입력 오류");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
