package csvControl;

import employee.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class EmployeeCsvReader {
    public static ObservableList<Employee> read(String filePath) throws IOException {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; }
                String[] data = line.split(",");
                if (data.length == 6) {
                    employees.add(new Employee(data[1], data[2], data[3], data[4], data[5]));
                }
            }
        }
        return employees;
    }
}
