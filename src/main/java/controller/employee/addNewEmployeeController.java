package controller.employee;

import employee.Employee;
import employee.EmployeeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class addNewEmployeeController {
    private EmployeeManager employeeManager = EmployeeManager.instance;
    @FXML private TextField nameField;
    @FXML private TextField adressTextField;
    @FXML private TextField phoneNumberTextField;
    @FXML private Button addEmployeeButton;
    @FXML private DatePicker birthDatePicker,employeeDatePicker;



    @FXML
    public void initialize() {
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
    private void addEmployeeButtonClick(ActionEvent event) {
        String name = nameField.getText().trim();
        String address = adressTextField.getText().trim();

        String phone = phoneNumberTextField.getText().trim();




        if (name.isEmpty() || birthDatePicker.getValue() == null || address.isEmpty() || employeeDatePicker.getValue() == null || phone.isEmpty()) {
            showAlert("모든 항목을 입력해주세요!");
            return;
        }
        String employmentDate = employeeDatePicker.getValue().toString();
        String birth = birthDatePicker.getValue().toString();
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
