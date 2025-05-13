package employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.FileChecker;
import csvControl.*;

import java.io.IOException;

public class EmployeeManager {
    public static EmployeeManager instance = new EmployeeManager();
    private static ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    public static ObservableList<Employee> getEmployeeList() {
        return employeeList;
    }

    public void addEmployee(Employee emp) {
        employeeList.add(emp);
        updateCsvFile();
    }

    public void editEmployee(Employee emp, String name, String birth, String address, String employeedate, String phoneNumber) {
        emp.setName(name);
        emp.setBirth(birth);
        emp.setAddress(address);
        emp.setEmployeedate(employeedate);
        emp.setPhoneNumber(phoneNumber);

        updateCsvFile();
    }

    public void setEmployeeList() {
        FileChecker fileChecker = new FileChecker();
        if (util.PathManager.currentPath == null) {
            System.err.println("경로가 설정되지 않았습니다.");
            return;
        }
        if (fileChecker.checkWorkerInfoFile()) {
            try {
                ObservableList<Employee> loadedList = EmployeeCsvReader.read(
                        util.PathManager.currentPath + java.io.File.separator + "WorkerInfo.csv");
                employeeList.clear();
                employeeList.addAll(loadedList);
            } catch (IOException e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> {
                    new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "사원 정보 파일을 불러오거나 저장하는 중 오류가 발생했습니다.").showAndWait();
                });
            }

        }
    }

    public void updateCsvFile() {
        try {
            EmployeeCsvWriter.write(employeeList, util.PathManager.currentPath + java.io.File.separator + "WorkerInfo.csv");
        } catch (IOException e) {
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR, "사원 정보 파일을 불러오거나 저장하는 중 오류가 발생했습니다.").showAndWait();
            });
        }

    }

}
