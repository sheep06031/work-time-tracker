package controller.home;

import employee.Employee;
import employee.EmployeeManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import util.PathManager;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class HomeController {
    @FXML Text totalEmployee;
    @FXML Text totalWorkTime;
    @FXML Text totalPayroll;
    @FXML Text todayText;
    @FXML ChoiceBox<Integer> yearChoiceBox, monthChoiceBox;
    @FXML Text latestEmp1Text, latestEmp2Text;
    @FXML Text nearBirth1, nearBirth2;
    @FXML LineChart<String, Number> lineChart;


    @FXML
    public void initialize() {
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            yearChoiceBox.getItems().add(i);
        }
        yearChoiceBox.setValue(currentYear);

        int currentMonth = LocalDate.now().getMonthValue();
        for (int i = 1; i <= 12; i++) {
            monthChoiceBox.getItems().add(i);
        }
        monthChoiceBox.setValue(currentMonth);

        updateInfo();
        updateEmployeeCount();

        EmployeeManager.getEmployeeList().addListener((javafx.collections.ListChangeListener.Change<? extends employee.Employee> c) -> {
            updateEmployeeCount();
        });

        yearChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> updateInfo());
        monthChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> updateInfo());



        todayText.setText(getToday());
        getlatestEmp();
        showNearestBirthdays();
        drawSalaryGraph();


    }

    private void updateEmployeeCount() {
        totalEmployee.setText(String.valueOf(EmployeeManager.getEmployeeList().size()));
    }
    private void updateInfo() {
        totalWorkTime.setText(String.valueOf(getTotalsFromSummaryCsv(yearChoiceBox.getValue(), monthChoiceBox.getValue())[0]));
        totalPayroll.setText(String.valueOf(getTotalsFromSummaryCsv(yearChoiceBox.getValue(), monthChoiceBox.getValue())[1]));
    }


    private int[] getTotalsFromSummaryCsv(int year, int month) {
        int totalHours = 0;
        int totalPay   = 0;

        String path = PathManager.currentPath + File.separator + "Workhistory"
                + File.separator + "MonthlyPayrollSummary.csv";
        File file = new File(path);
        if (!file.exists()) return new int[]{0,0};

        try (Scanner sc = new Scanner(file, "UTF-8")) {
            if (sc.hasNextLine()) sc.nextLine();
            while (sc.hasNextLine()) {
                String[] t = sc.nextLine().split(",");
                int rowYear = Integer.parseInt(t[2].trim());
                int rowMonth = Integer.parseInt(t[3].trim());

                if (rowYear == year && rowMonth == month) {
                    totalHours += Integer.parseInt(t[4].trim());
                    totalPay += Integer.parseInt(t[6].trim());
                }
            }
        } catch (Exception ignored) {}
        return new int[]{totalHours, totalPay};
    }

    private String getToday() {
        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("오늘 : yyyy년 MM월 dd일");
        return now.format(formatter);
    }

    private void getlatestEmp() {
        ObservableList<Employee> list = EmployeeManager.getEmployeeList();
        String employee1 = "- ";
        String employee2 = "- ";

        if (list.size() >= 2) {
            employee1 += list.get(list.size() - 1).getName() + "(" + list.get(list.size() - 1).getBirth() + ")";
            employee2 += list.get(list.size() - 2).getName() + "(" + list.get(list.size() - 2).getBirth() + ")";
            latestEmp1Text.setText(employee1);
            latestEmp2Text.setText(employee2);
        } else if (list.size() == 1) {
            employee1 += list.get(0).getName() + "(" + list.get(0).getBirth() + ")";
            latestEmp1Text.setText(employee1);
            latestEmp2Text.setText("- ");
        } else {
            latestEmp1Text.setText("- ");
            latestEmp2Text.setText("- ");
        }
    }

    private void showNearestBirthdays() {
        List<Employee> employees = new ArrayList<>(EmployeeManager.getEmployeeList());
        LocalDate today = LocalDate.now();

        List<AbstractMap.SimpleEntry<Employee, Long>> sorted = employees.stream()
                .filter(e -> e.getBirth() != null && !e.getBirth().isBlank())
                .map(e -> {
                    try {
                        LocalDate birth = LocalDate.parse(e.getBirth());
                        LocalDate nextBirthday = birth.withYear(today.getYear());
                        if (nextBirthday.isBefore(today) || nextBirthday.isEqual(today)) {
                            nextBirthday = nextBirthday.plusYears(1);
                        }
                        long daysUntil = ChronoUnit.DAYS.between(today, nextBirthday);
                        return new AbstractMap.SimpleEntry<>(e, daysUntil);
                    } catch (Exception ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .sorted(Map.Entry.comparingByValue())
                .toList();


        if (sorted.size() >= 1) {
            Employee e1 = sorted.get(0).getKey();
            nearBirth1.setText("- " + e1.getName() + " (" + e1.getBirth() + ")");
        } else {
            nearBirth1.setText("-");
        }

        if (sorted.size() >= 2) {
            Employee e2 = sorted.get(1).getKey();
            nearBirth2.setText("- " + e2.getName() + " (" + e2.getBirth() + ")");
        } else {
            nearBirth2.setText("-");
        }
    }

    private void drawSalaryGraph() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("월별 총 급여");

        String path = PathManager.currentPath + File.separator + "Workhistory" + File.separator + "MonthlyPayrollSummary.csv";
        File file = new File(path);
        if (!file.exists()) return;

        Map<String, Integer> monthlyTotal = new TreeMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            String header = reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 7) continue;

                String year = tokens[2].trim();
                String month = tokens[3].trim();
                int pay = Integer.parseInt(tokens[6].trim());
                String key = year + "-" + String.format("%02d", Integer.parseInt(month));

                monthlyTotal.put(key, monthlyTotal.getOrDefault(key, 0) + pay);
            }
        } catch (IOException | NumberFormatException ignored) {}

        for (Map.Entry<String, Integer> entry : monthlyTotal.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        lineChart.getData().clear();
        lineChart.getData().add(series);
    }

}






