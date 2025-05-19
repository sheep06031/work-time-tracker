package controller;

import util.PathManager;
import employee.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
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
    @FXML private Text total;
    @FXML private Button saveButton;

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

            Integer loadedHours = workHourMap.get(currentDate);
            if (loadedHours != null) {
                workInput.setText(String.valueOf(loadedHours));
            }

            if (holidayPayMap.containsKey(currentDate)) {
                holidayCheck.setSelected(true);
            } else {
                DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                    holidayCheck.setSelected(true);
                }
            }

            updatePay(payLabel, workInput, holidayCheck, box, currentDate);

            workInput.textProperty().addListener((obs, oldVal, newVal) -> {
                updatePay(payLabel, workInput, holidayCheck, box, currentDate);
                calculateWeeklyAllowance(year, month);
            });

            holidayCheck.selectedProperty().addListener((obs, oldVal, isSelected) -> {
                updatePay(payLabel, workInput, holidayCheck, box, currentDate);
            });

            box.getChildren().addAll(dateLabel, payLabel, holidayCheck, workInput);
            calendarGrid.add(box, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }

        calculateWeeklyAllowance(year, month);
        calculateTotalMonthlySalary(year, month);
        saveButton.setDisable(employee == null);
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
        Map<Integer, Integer> weekHourSum = new HashMap<>();

        for (Map.Entry<LocalDate, Integer> entry : workHourMap.entrySet()) {
            LocalDate date = entry.getKey();
            if (date.getMonthValue() != month || date.getYear() != year) continue;
            int week = date.getDayOfMonth() / 7;
            weekHourSum.put(week, weekHourSum.getOrDefault(week, 0) + entry.getValue());
        }

        int totalWeeklyAllowance = 0;
        try {
            int wage = Integer.parseInt(wageTextField.getText().replace(",", "").trim());
            int standardHours = Integer.parseInt(standardHourField.getText().trim());

            for (int totalHours : weekHourSum.values()) {
                if (totalHours >= 15) {
                    totalWeeklyAllowance += wage * standardHours;
                }
            }
        } catch (NumberFormatException ignored) {}

        totalWeeklyAllowanceText.setText("당월 주휴수당 총합: " + numberFormat.format(totalWeeklyAllowance) + "원");
        calculateTotalMonthlySalary(year, month);
    }

    private void calculateTotalMonthlySalary(int year, int month) {
        int totalPay = 0;

        for (Map.Entry<LocalDate, Integer> entry : workHourMap.entrySet()) {
            LocalDate date = entry.getKey();
            if (date.getYear() != year || date.getMonthValue() != month) continue;

            int hours = entry.getValue();
            int wage;
            try {
                wage = Integer.parseInt(wageTextField.getText().replace(",", "").trim());
            } catch (NumberFormatException e) {
                total.setText("-");
                return;
            }

            boolean isHoliday = holidayPayMap.containsKey(date);
            int dailyPay = wage * hours;

            if (isHoliday) {
                dailyPay = hours <= 8 ? (int)(dailyPay * 1.5) : (int)(dailyPay * 2);
            }

            totalPay += dailyPay;
        }

        try {
            int weeklyAllowance = Integer.parseInt(totalWeeklyAllowanceText.getText().replaceAll("[^0-9]", ""));
            totalPay += weeklyAllowance;
        } catch (NumberFormatException ignored) {}

        total.setText(numberFormat.format(totalPay) + "원");
    }

    @FXML
    private void SaveButtonClicked() {
        if (employee == null) return;
        saveWorkHistory();
    }

    private void saveWorkHistory() {
        try {
            String path = PathManager.currentPath;
            String dirPath = path + File.separator + "Workhistory";
            File file = new File(dirPath, employee.getEmployeeId() + ".csv");

            Map<LocalDate, String> existingLines = new TreeMap<>();
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String header = reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens.length > 0) {
                        LocalDate date = LocalDate.parse(tokens[0].trim());
                        existingLines.put(date, line);
                    }
                }
                reader.close();
            }

            int wage = Integer.parseInt(wageTextField.getText().replace(",", "").trim());
            int standardHour = Integer.parseInt(standardHourField.getText().trim());
            YearMonth yearMonth = YearMonth.of(yearCombo.getValue(), monthCombo.getValue());

            for (int i = 1; i <= yearMonth.lengthOfMonth(); i++) {
                LocalDate date = yearMonth.atDay(i);
                Integer hours = workHourMap.get(date);
                if (hours == null) continue;
                boolean isHoliday = holidayPayMap.containsKey(date);
                int pay = wage * hours;
                int holidayBonus = isHoliday ? (hours <= 8 ? (int)(pay * 0.5) : pay) : 0;
                pay += holidayBonus;
                int weeklyBonus = 0;
                int totalWeeklyHour = 0;
                for (int j = 0; j < 7; j++) {
                    LocalDate weekDate = date.with(DayOfWeek.MONDAY).plusDays(j);
                    if (workHourMap.containsKey(weekDate)) {
                        totalWeeklyHour += workHourMap.get(weekDate);
                    }
                }
                if (totalWeeklyHour >= 15) {
                    weeklyBonus = wage * standardHour;
                }
                int total = pay + weeklyBonus;

                existingLines.put(date, String.format("%s,%d,%d,%d,%s,%d,%d,%d",
                        date, standardHour, wage, hours, isHoliday ? "true" : "false", pay, weeklyBonus, total));
            }

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write("날짜,1일 근로시간,시급,당일 근무시간,휴일지정,급여,주휴수당,월급\n");
            for (String line : existingLines.values()) {
                writer.write(line + "\n");
            }
            writer.close();

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private void loadWorkHistory(Employee employee) {
        try {
            String path = PathManager.currentPath;
            String dirPath = path + File.separator + "Workhistory";
            File file = new File(dirPath, employee.getEmployeeId() + ".csv");

            if (!file.exists()) return;

            Scanner scanner = new Scanner(file, "UTF-8");
            if (scanner.hasNextLine()) scanner.nextLine();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(",");
                if (tokens.length < 8) continue;

                LocalDate date = LocalDate.parse(tokens[0].trim());
                int workedHours = Integer.parseInt(tokens[3].trim());
                boolean isHoliday = tokens[4].trim().equalsIgnoreCase("true");

                workHourMap.put(date, workedHours);
                if (isHoliday) holidayPayMap.put(date, 0);
            }

            scanner.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        if (nameText != null) nameText.setText(employee.getName());
        if (phoneNumberText != null) phoneNumberText.setText(employee.getPhoneNumber());
        if (saveButton != null) saveButton.setDisable(false);
        loadWorkHistory(employee);
        buildCalendar(yearCombo.getValue(), monthCombo.getValue());
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
