package repository;

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
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] t = line.split(",");
                Employee e = new Employee(
                        t[0].trim(),           // id
                        t[1].trim(),           // name
                        t[2].trim(),           // birth
                        t[3].trim(),           // address
                        t[4].trim(),           // employeedate
                        t[5].trim());          // phone
                employees.add(e);
            }
        }
        return employees;
    }
}
