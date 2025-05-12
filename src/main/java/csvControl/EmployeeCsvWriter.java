package csvControl;

import employee.Employee;
import javafx.collections.ObservableList;
import java.io.FileWriter;
import java.io.IOException;

public class EmployeeCsvWriter {
    public static void write(ObservableList<Employee> employees, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("id,이름,생년월일,주소,입사 날짜,전화번호\n"); // 헤더
            for (Employee emp : employees) {
                writer.write(emp.getEmployeeId() + "," + emp.getName() + "," + emp.getBirth() + ","
                        + emp.getAddress() + "," + emp.getEmployeedate() + "," + emp.getPhoneNumber()+ "\n");
            }
        }
    }
}
