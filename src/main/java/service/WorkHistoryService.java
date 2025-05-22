package service;

import employee.Employee;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import util.PathManager;

import java.io.*;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

public class WorkHistoryService {
    private final NumberFormat numberFormat = NumberFormat.getInstance();

    public void loadWorkHistory(Employee employee, Map<LocalDate, Integer> workMap, Map<LocalDate, Integer> holidayMap) {
        String path = PathManager.currentPath + File.separator + "Workhistory";
        File file = new File(path, employee.getEmployeeId() + ".csv");
        if (!file.exists()) return;

        try (Scanner scanner = new Scanner(file, "UTF-8")) {
            if (scanner.hasNextLine()) scanner.nextLine();
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                LocalDate date = LocalDate.parse(tokens[0].trim());
                int hours = Integer.parseInt(tokens[3].trim());
                boolean isHoliday = tokens[4].trim().equalsIgnoreCase("true");
                workMap.put(date, hours);
                if (isHoliday) holidayMap.put(date, 0);
            }
        } catch (IOException ignored) {}
    }

    public void saveWorkHistory(Employee emp, Map<LocalDate, Integer> workMap, Map<LocalDate, Integer> holidayMap,
                                TextField wageField, TextField hourField, ComboBox<Integer> yearCombo, ComboBox<Integer> monthCombo) {
        String path = PathManager.currentPath + File.separator + "Workhistory";
        File file = new File(path, emp.getEmployeeId() + ".csv");
        Map<LocalDate, String> existing = new TreeMap<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
                reader.readLine();
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens.length < 1) continue;
                    LocalDate date = LocalDate.parse(tokens[0].trim());
                    existing.put(date, line);
                }
            } catch (IOException ignored) {}
        }

        int wage = Integer.parseInt(wageField.getText().replace(",", "").trim());
        int stdHour = Integer.parseInt(hourField.getText().trim());
        YearMonth ym = YearMonth.of(yearCombo.getValue(), monthCombo.getValue());

        for (int i = 1; i <= ym.lengthOfMonth(); i++) {
            LocalDate date = ym.atDay(i);
            Integer hours = workMap.get(date);
            if (hours == null) continue;

            boolean isHoliday = holidayMap.containsKey(date);
            int pay = wage * hours;
            int holidayBonus = isHoliday ? (hours <= 8 ? (int) (pay * 0.5) : pay) : 0;
            pay += holidayBonus;
            int weeklyBonus = calcWeeklyBonus(date, workMap, stdHour, wage);
            int total = pay + weeklyBonus;

            existing.put(date, String.format("%s,%d,%d,%d,%s,%d",
                    date, stdHour, wage, hours, isHoliday ? "true" : "false", pay));
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            writer.write("날짜,1일 근로시간,시급,당일 근무시간,휴일지정,급여\n");
            for (String l : existing.values()) {
                writer.write(l + "\n");
            }
        } catch (IOException ignored) {}

        int totalHours = workMap.entrySet().stream()
                .filter(e -> e.getKey().getYear() == ym.getYear() && e.getKey().getMonthValue() == ym.getMonthValue())
                .mapToInt(Map.Entry::getValue).sum();

        int weeklyAllowance = calculateWeeklyAllowance(workMap, wageField, hourField,
                ym.getYear(), ym.getMonthValue());

        int totalPay = calculateMonthlyTotal(workMap, holidayMap, wageField,
                weeklyAllowance, ym.getYear(), ym.getMonthValue());

        new MonthlySummaryService().updateSummary(emp, ym, totalHours, weeklyAllowance, totalPay);
    }


    public void buildCalendar(Employee employee, int year, int month, GridPane calendarGrid,
                              TextField wageField, TextField hourField,
                              Map<LocalDate, Integer> holidayMap, Map<LocalDate, Integer> workMap,
                              Text allowanceText, Text totalText, Button saveButton) {
        calendarGrid.getChildren().clear();
        String[] days = {"월", "화", "수", "목", "금", "토", "일"};

        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setAlignment(javafx.geometry.Pos.CENTER);
            calendarGrid.add(dayLabel, i, 0);
        }

        YearMonth ym = YearMonth.of(year, month);
        int col = (ym.atDay(1).getDayOfWeek().getValue() + 6) % 7;
        int row = 1;

        for (int d = 1; d <= ym.lengthOfMonth(); d++) {
            LocalDate date = ym.atDay(d);
            VBox box = new VBox(3);
            box.setPrefSize(110, 100);
            box.setStyle("-fx-border-color: lightgray; -fx-padding: 5; -fx-alignment: top-center;");

            Label dateLabel = new Label(String.valueOf(d));
            TextField input = new TextField();
            input.setPrefWidth(90);
            input.setPromptText("근무시간");
            CheckBox holidayCheck = new CheckBox("휴일 지정");
            holidayCheck.setPrefWidth(90);
            Label payLabel = new Label("급여: -");

            Integer loadedHours = workMap.get(date);
            if (loadedHours != null) input.setText(String.valueOf(loadedHours));
            if (holidayMap.containsKey(date) || date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
                holidayCheck.setSelected(true);

            updatePay(payLabel, input, holidayCheck, box, date, wageField, workMap, holidayMap);
            input.textProperty().addListener((obs, o, n) -> updatePay(payLabel, input, holidayCheck, box, date, wageField, workMap, holidayMap));
            holidayCheck.selectedProperty().addListener((obs, o, n) -> updatePay(payLabel, input, holidayCheck, box, date, wageField, workMap, holidayMap));

            box.getChildren().addAll(dateLabel, payLabel, holidayCheck, input);
            calendarGrid.add(box, col, row);

            if (++col > 6) {
                col = 0;
                row++;
            }
        }

        int allowance = calculateWeeklyAllowance(workMap, wageField, hourField, year, month);
        allowanceText.setText("당월 주휴수당 총합: " + numberFormat.format(allowance) + "원");

        int total = calculateMonthlyTotal(workMap, holidayMap, wageField, allowance, year, month);
        totalText.setText(numberFormat.format(total) + "원");

        saveButton.setDisable(employee == null);
    }

    private void updatePay(Label label, TextField input, CheckBox check, VBox box, LocalDate date,
                           TextField wageField, Map<LocalDate, Integer> workMap, Map<LocalDate, Integer> holidayMap) {
        if (check.isSelected()) {
            box.setStyle("-fx-background-color: #fde3e6; -fx-border-color: lightgray;");
            holidayMap.put(date, 0);
        } else {
            box.setStyle("-fx-background-color: transparent; -fx-border-color: lightgray;");
            holidayMap.remove(date);
        }

        try {
            int wage = Integer.parseInt(wageField.getText().replace(",", "").trim());
            int hours = Integer.parseInt(input.getText().trim());
            int pay = wage * hours;


            workMap.put(date, hours);
            if (check.isSelected()) {
                holidayMap.put(date, 0);
                pay = hours <= 8 ? (int) (pay * 1.5) : (int) (pay * 2);
                box.setStyle("-fx-background-color: #fde3e6; -fx-border-color: lightgray;");
            } else {
                holidayMap.remove(date);
                box.setStyle("-fx-background-color: transparent; -fx-border-color: lightgray;");
            }

            label.setText("급여: " + numberFormat.format(pay) + "원");
        } catch (NumberFormatException e) {
            label.setText("급여: -");
            workMap.remove(date);
            holidayMap.remove(date);
        }
    }

    private int calculateWeeklyAllowance(Map<LocalDate, Integer> map, TextField wageField, TextField hourField, int y, int m) {
        Map<Integer, Integer> weekSum = new HashMap<>();
        for (Map.Entry<LocalDate, Integer> e : map.entrySet()) {
            if (e.getKey().getYear() == y && e.getKey().getMonthValue() == m) {
                int week = e.getKey().getDayOfMonth() / 7;
                weekSum.put(week, weekSum.getOrDefault(week, 0) + e.getValue());
            }
        }
        try {
            int wage = Integer.parseInt(wageField.getText().replace(",", "").trim());
            int standard = Integer.parseInt(hourField.getText().trim());
            int total = 0;
            for (int h : weekSum.values()) if (h >= 15) total += wage * standard;
            return total;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int calculateMonthlyTotal(Map<LocalDate, Integer> work, Map<LocalDate, Integer> holiday,
                                      TextField wageField, int weeklyAllowance, int y, int m) {
        int total = 0;
        try {
            int wage = Integer.parseInt(wageField.getText().replace(",", "").trim());
            for (Map.Entry<LocalDate, Integer> e : work.entrySet()) {
                LocalDate d = e.getKey();
                if (d.getYear() != y || d.getMonthValue() != m) continue;
                int h = e.getValue();
                int daily = wage * h;
                if (holiday.containsKey(d)) daily = h <= 8 ? (int) (daily * 1.5) : (int) (daily * 2);
                total += daily;
            }
            return total + weeklyAllowance;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private int calcWeeklyBonus(LocalDate d, Map<LocalDate, Integer> map, int standard, int wage) {
        int hours = 0;
        for (int i = 0; i < 7; i++) {
            LocalDate day = d.with(DayOfWeek.MONDAY).plusDays(i);
            if (map.containsKey(day)) hours += map.get(day);
        }
        return hours >= 15 ? wage * standard : 0;
    }
}