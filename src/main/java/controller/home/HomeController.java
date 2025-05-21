package controller.home;

import employee.Employee;
import employee.EmployeeManager;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import util.PathManager;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class HomeController {
    @FXML Text totalEmployee;
    @FXML Text totalWorkTime;
    @FXML Text totalPayroll;
    @FXML Text todayText;
    @FXML ChoiceBox<Integer> yearChoiceBox, monthChoiceBox;


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
            if (sc.hasNextLine()) sc.nextLine(); // header
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

    private void getNearBirthday() {
        ObservableList<Employee> list = EmployeeManager.getEmployeeList();
        String employee1 = list.get(list.size()).getName() + "(" + list.get(list.size()).getBirth() +")";
        String employee2 = list.get(list.size()-1).getName() + "(" + list.get(list.size()).getBirth() +")";


    }



}
