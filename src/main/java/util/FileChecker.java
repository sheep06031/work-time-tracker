package util;

import employee.Employee;

import java.io.*;

public class FileChecker {
    public boolean checkWorkerInfoFile() {
        String path = PathManager.currentPath;
        if (path == null) {
            System.err.println("경로가 설정되지 않았습니다.");
            return false;
        }

        String workerInfoFilePath = path + File.separator + "WorkerInfo.csv";
        File infofile = new File(workerInfoFilePath);

        if (infofile.exists() && !infofile.isDirectory()) {
            return true;
        } else {
            String header = "id,이름,생년월일,주소,입사 날짜,전화번호\n";
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(workerInfoFilePath), "UTF-8")) {
                writer.write(header);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public boolean checkPayrollFile(Employee employee) {
        String path = PathManager.currentPath;
        if (path == null) {
            System.err.println("경로가 설정되지 않았습니다.");
            return false;
        }

        String WorkhistoryDirectoryPath = path + File.separator + "Workhistory";
        File WorkhistoryDirectory = new File(WorkhistoryDirectoryPath);
        boolean directoryCreated = WorkhistoryDirectory.mkdir();


        String fileName = employee.getEmployeeId() + ".csv";
        String workhistoryFilePath = WorkhistoryDirectoryPath + File.separator + fileName;
        File workhistoryFile = new File(workhistoryFilePath);

        if (workhistoryFile.exists() && !workhistoryFile.isDirectory()) {
            return true;
        } else {
            String header = "날짜,1일 근로시간,시급,당일 근무시간,휴일지정,급여\n";
            try (Writer writer = new OutputStreamWriter(new FileOutputStream(workhistoryFilePath), "UTF-8")) {
                writer.write(header);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public void deletePayrollFile(Employee employee) {
        String path = PathManager.currentPath;
        if (path == null) {
            System.err.println("경로가 설정되지 않았습니다.");
        }

        String WorkhistoryDirectoryPath = path + File.separator + "Workhistory";
        String fileName = employee.getEmployeeId() + ".csv";
        String workhistoryFilePath = WorkhistoryDirectoryPath + File.separator + fileName;
        File workhistoryFile = new File(workhistoryFilePath);

        if(workhistoryFile.exists()){ workhistoryFile.delete(); }
    }
}
