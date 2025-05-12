package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileChecker {
    public boolean checkWorkerInfoFile() {
        String path = PathManager.currentPath;
        String workerInfoFilePath = path + File.separator + "WorkerInfo.csv";
        File file = new File(workerInfoFilePath);

        if (file.exists() && !file.isDirectory()) {
            return true;
        } else {
            String header = "id,이름,생년월일,주소,입사 날짜,전화번호\n";
            try (FileWriter writer = new FileWriter(workerInfoFilePath)) {
                writer.write(header);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}
