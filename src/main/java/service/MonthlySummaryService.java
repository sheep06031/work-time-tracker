package service;

import employee.Employee;
import util.PathManager;

import java.io.*;
import java.time.YearMonth;
import java.util.*;


public class MonthlySummaryService {
    private static final String FILE_NAME = "MonthlyPayrollSummary.csv";

    public void updateSummary(Employee emp, YearMonth ym, int totalHours, int weeklyAllowance, int totalPay) {

        String dir = PathManager.currentPath + File.separator + "Workhistory";
        File file = new File(dir, FILE_NAME);

        Map<String, String> summaryMap = new LinkedHashMap<>();

        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "UTF-8"))) {
                String header = br.readLine();
                String line;
                while ((line = br.readLine()) != null) {
                    String[] t = line.split(",");
                    String key = t[0] + "-" + t[2] + "-" + t[3];
                    summaryMap.put(key, line);
                }
            } catch (IOException ignored) {}
        }

        String newLine = String.format("%s,%s,%d,%d,%d,%d,%d",
                emp.getEmployeeId(), emp.getName(),
                ym.getYear(), ym.getMonthValue(),
                totalHours, weeklyAllowance, totalPay);

        String mapKey = emp.getEmployeeId() + "-" + ym.getYear() + "-" + ym.getMonthValue();
        summaryMap.put(mapKey, newLine);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), "UTF-8"))) {

            bw.write("직원ID,이름,연도,월,총근무시간,주휴수당,월급총액\n");
            for (String v : summaryMap.values()) bw.write(v + "\n");
        } catch (IOException ignored) {}
    }
}
