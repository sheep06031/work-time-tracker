package csvControl;

import employee.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;

public class EmployeeCsvReader {
    public static ObservableList<Employee> read(String filePath) throws IOException {
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("CSV 파일을 찾을 수 없습니다: " + filePath);
        }
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
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
