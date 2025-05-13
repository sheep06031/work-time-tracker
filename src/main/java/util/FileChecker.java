package util;

import java.io.*;

public class FileChecker {
    public boolean checkWorkerInfoFile() {
        String path = PathManager.currentPath;
        if (path == null) {
            System.err.println("경로가 설정되지 않았습니다.");
            return false;
        }

        String workerInfoFilePath = path + File.separator + "WorkerInfo.csv";
        File file = new File(workerInfoFilePath);

        if (file.exists() && !file.isDirectory()) {
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
}
