package employee;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EmployeeManager {
    public static EmployeeManager instance = new EmployeeManager();
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    public ObservableList<Employee> getEmployeeList() {
        return employeeList;
    }
    public void addEmployee(Employee emp) {
        employeeList.add(emp);
    }
}
