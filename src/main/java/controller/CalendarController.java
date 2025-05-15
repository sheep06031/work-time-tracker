package controller;

import employee.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class CalendarController {

    @FXML private ComboBox<Integer> yearCombo;
    @FXML private ComboBox<Integer> monthCombo;
    @FXML private GridPane calendarGrid;
    @FXML private TextField wageTextField;
    @FXML private TextField standardHourField;
    @FXML private Text totalWeeklyAllowanceText;
    @FXML private Text nameText;
    @FXML private Text phoneNumberText;

    private Employee employee;
    private final Map<LocalDate, Integer> holidayPayMap = new HashMap<>();
    private final Map<LocalDate, Integer> workHourMap = new HashMap<>();
    private final NumberFormat numberFormat = NumberFormat.getInstance();

    @FXML
    public void initialize() {
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            yearCombo.getItems().add(i);
        }
        yearCombo.setValue(currentYear);

        for (int i = 1; i <= 12; i++) {
            monthCombo.getItems().add(i);
        }
        monthCombo.setValue(LocalDate.now().getMonthValue());

        yearCombo.setOnAction(e -> buildCalendar(yearCombo.getValue(), monthCombo.getValue()));
        monthCombo.setOnAction(e -> buildCalendar(yearCombo.getValue(), monthCombo.getValue()));

        wageTextField.textProperty().addListener((obs, oldVal, newVal) -> calculateWeeklyAllowance(yearCombo.getValue(), monthCombo.getValue()));
        standardHourField.textProperty().addListener((obs, oldVal, newVal) -> calculateWeeklyAllowance(yearCombo.getValue(), monthCombo.getValue()));

        buildCalendar(currentYear, LocalDate.now().getMonthValue());
    }

    @FXML
    private void onUpdateCalendar() {
        Integer year = yearCombo.getValue();
        Integer month = monthCombo.getValue();
        if (year != null && month != null) {
            buildCalendar(year, month);
        }
    }

    private void buildCalendar(int year, int month) {
        calendarGrid.getChildren().clear();
        String[] days = {"월", "화", "수", "목", "금", "토", "일"};

        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            calendarGrid.add(dayLabel, i, 0);
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        int length = yearMonth.lengthOfMonth();
        int startCol = (firstDay.getDayOfWeek().getValue() + 6) % 7;

        int row = 1;
        int col = startCol;

        for (int day = 1; day <= length; day++) {
            VBox box = new VBox(3);
            box.setPrefSize(110, 100);
            box.setStyle("-fx-border-color: lightgray; -fx-alignment: top-center; -fx-padding: 5;");

            Label dateLabel = new Label(String.valueOf(day));
            TextField workInput = new TextField();
            workInput.setPrefWidth(90);
            workInput.setPrefHeight(10);
            workInput.setPromptText("근무시간");
            workInput.setTranslateY(5);

            CheckBox holidayCheck = new CheckBox("휴일 지정");
            holidayCheck.setPrefWidth(90);
            holidayCheck.setPrefHeight(10);
            holidayCheck.setTranslateY(5);

            Label payLabel = new Label("급여: 0원");
            LocalDate currentDate = LocalDate.of(year, month, day);

            workInput.textProperty().addListener((obs, oldVal, newVal) -> {
                updatePay(payLabel, workInput, holidayCheck, box, currentDate);
                calculateWeeklyAllowance(year, month);
            });

            holidayCheck.selectedProperty().addListener((obs, oldVal, isSelected) -> {
                updatePay(payLabel, workInput, holidayCheck, box, currentDate);
            });

            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                holidayCheck.setSelected(true);
                updatePay(payLabel, workInput, holidayCheck, box, currentDate);
            }

            box.getChildren().addAll(dateLabel, payLabel, holidayCheck, workInput);
            calendarGrid.add(box, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        calculateWeeklyAllowance(year, month);
    }

    private void updatePay(Label payLabel, TextField workInput, CheckBox holidayCheck, VBox box, LocalDate date) {
        String input = workInput.getText().trim();
        boolean isHoliday = holidayCheck.isSelected();

        if (isHoliday) {
            box.setStyle("-fx-background-color: #fde3e6; -fx-border-color: lightgray; -fx-alignment: top-center; -fx-padding: 5;");
        } else {
            box.setStyle("-fx-background-color: transparent; -fx-border-color: lightgray; -fx-alignment: top-center; -fx-padding: 5;");
        }

        if (input.isEmpty()) {
            payLabel.setText("급여: -");
            holidayPayMap.remove(date);
            workHourMap.remove(date);
            return;
        }

        try {
            String wageStr = wageTextField.getText().replace(",", "").trim();
            int wage = Integer.parseInt(wageStr);
            int hours = Integer.parseInt(input);
            int pay = wage * hours;

            workHourMap.put(date, hours);

            if (isHoliday) {
                int holidayPay = hours <= 8 ? (int)(pay * 0.5) : pay;
                holidayPayMap.put(date, holidayPay);
                pay = hours <= 8 ? (int)(pay * 1.5) : (int)(pay * 2);
            } else {
                holidayPayMap.remove(date);
            }

            payLabel.setText("급여: " + numberFormat.format(pay) + "원");

        } catch (NumberFormatException e) {
            payLabel.setText("급여: -");
            holidayPayMap.remove(date);
            workHourMap.remove(date);
        }
    }

    private void calculateWeeklyAllowance(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        Map<Integer, Integer> weekHourSum = new HashMap<>();

        for (Map.Entry<LocalDate, Integer> entry : workHourMap.entrySet()) {
            LocalDate date = entry.getKey();
            if (date.getMonthValue() != month || date.getYear() != year) continue;

            int week = date.getDayOfMonth() / 7;
            weekHourSum.put(week, weekHourSum.getOrDefault(week, 0) + entry.getValue());
        }

        int totalWeeklyAllowance = 0;
        try {
            String wageStr = wageTextField.getText().replace(",", "").trim();
            int wage = Integer.parseInt(wageStr);

            int standardHours = 8;
            try {
                standardHours = Integer.parseInt(standardHourField.getText().trim());
            } catch (NumberFormatException ignored) {}

            for (int totalHours : weekHourSum.values()) {
                if (totalHours >= 15) {
                    totalWeeklyAllowance += wage * standardHours;
                }
            }
        } catch (NumberFormatException ignored) {}

        totalWeeklyAllowanceText.setText("당월 주휴수당 총합: " + numberFormat.format(totalWeeklyAllowance) + "원");
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

    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (nameText != null) nameText.setText(employee.getName());
        if (phoneNumberText != null) phoneNumberText.setText(employee.getPhoneNumber());
    }
}