package repository;

import employee.Employee;
import javafx.collections.ObservableList;
import java.io.*;

public class EmployeeCsvWriter {
    public static void write(ObservableList<Employee> employees, String filePath) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
            bw.write("id,이름,생년월일,주소,입사 날짜,전화번호\n");
            for (Employee emp : employees) {
                bw.write(emp.getEmployeeId() + "," + emp.getName() + "," + emp.getBirth() + ","
                        + emp.getAddress() + "," + emp.getEmployeedate() + "," + emp.getPhoneNumber() + "\n");
            }
        }
    }
}
