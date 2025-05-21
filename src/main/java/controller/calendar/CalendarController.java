package controller.calendar;

import controller.employee.SearchEmployeeController;
import employee.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import service.WorkHistoryService;
import util.FileChecker;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class CalendarController {
    @FXML private ComboBox<Integer> yearCombo, monthCombo;
    @FXML private GridPane calendarGrid;
    @FXML private TextField wageTextField, standardHourField;
    @FXML private Text totalWeeklyAllowanceText, nameText, phoneNumberText, total;
    @FXML private Button saveButton;

    private final Map<LocalDate, Integer> holidayPayMap = new HashMap<>();
    private final Map<LocalDate, Integer> workHourMap = new HashMap<>();
    private final WorkHistoryService workHistoryService = new WorkHistoryService();

    private Employee employee;

    @FXML
    public void initialize() {
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 5; i <= currentYear + 5; i++) yearCombo.getItems().add(i);
        for (int i = 1; i <= 12; i++) monthCombo.getItems().add(i);

        yearCombo.setValue(currentYear);
        monthCombo.setValue(LocalDate.now().getMonthValue());

        yearCombo.setOnAction(e -> refreshCalendar());
        monthCombo.setOnAction(e -> refreshCalendar());

        wageTextField.textProperty().addListener((obs, oldVal, newVal) -> refreshCalendar());
        standardHourField.textProperty().addListener((obs, oldVal, newVal) -> refreshCalendar());

        refreshCalendar();
    }

    private void refreshCalendar() {
        workHistoryService.buildCalendar(employee, yearCombo.getValue(), monthCombo.getValue(),
                calendarGrid, wageTextField, standardHourField,
                holidayPayMap, workHourMap, totalWeeklyAllowanceText, total, saveButton);
    }

    @FXML
    private void SaveButtonClicked() {
        if (employee == null) return;

        new FileChecker().checkPayrollFile(employee);


        workHistoryService.saveWorkHistory(employee, workHourMap, holidayPayMap,
                    wageTextField, standardHourField, yearCombo, monthCombo);
    }

    public void setEmployee(Employee emp) {
        this.employee = emp;
        nameText.setText(emp.getName());
        phoneNumberText.setText(emp.getPhoneNumber());
        saveButton.setDisable(false);

        workHourMap.clear();
        holidayPayMap.clear();

        workHistoryService.loadWorkHistory(emp, workHourMap, holidayPayMap);
        refreshCalendar();
    }

    @FXML
    private void openSearchEmployeeWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/worktimetracker/searchEmployee.fxml"));
        Parent root = loader.load();
        SearchEmployeeController controller = loader.getController();
        controller.setCalendarController(this);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
