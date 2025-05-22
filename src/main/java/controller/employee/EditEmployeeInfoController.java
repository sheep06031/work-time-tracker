package controller.employee;

import employee.Employee;
import employee.EmployeeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public class EditEmployeeInfoController {
    @FXML private TextField editNameField;
    @FXML private TextField editAddressTextField;
    @FXML private TextField editPhoneNumberTextField;
    @FXML private Button editEmployeeButton;
    @FXML private DatePicker birthDatePicker, employeeDatePicker;
    private Employee employee;
    private EmployeeManager employeeManager = EmployeeManager.instance;
    private TableView<Employee> employeeTableView;

    @FXML
    void initialize() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        birthDatePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return (string == null || string.trim().isEmpty()) ? null : LocalDate.parse(string, formatter);
                } catch (DateTimeParseException e) {
                    showAlert("날짜 형식이 잘못되었습니다. 예: 2025-05-21");
                    return null;
                }
            }
        });

        employeeDatePicker.setConverter(new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                try {
                    return (string == null || string.trim().isEmpty()) ? null : LocalDate.parse(string, formatter);
                } catch (DateTimeParseException e) {
                    showAlert("날짜 형식이 잘못되었습니다. 예: 2025-05-21");
                    return null;
                }
            }
        });
    }

    @FXML
    private void editEmployeeButtonClick(ActionEvent event) {
        String name = editNameField.getText().trim();
        String address = editAddressTextField.getText().trim();
        String phone = editPhoneNumberTextField.getText().trim();

        if (name.isEmpty() || birthDatePicker.getValue() == null || address.isEmpty() || employeeDatePicker.getValue() == null || phone.isEmpty()) {
            showAlert("모든 항목을 입력해주세요!");
            return;
        }

        String birth = birthDatePicker.getValue().toString();
        String employmentDate = employeeDatePicker.getValue().toString();
        employeeManager.editEmployee(employee, name, birth, address, employmentDate, phone);


        employeeTableView.refresh();
        Stage stage = (Stage) editEmployeeButton.getScene().getWindow();
        stage.close();
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        editNameField.setText(employee.getName());
        birthDatePicker.setPromptText(employee.getBirth());
        editAddressTextField.setText(employee.getAddress());
        employeeDatePicker.setPromptText(employee.getEmployeedate());
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
