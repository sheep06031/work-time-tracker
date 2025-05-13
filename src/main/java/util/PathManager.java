package util;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.prefs.Preferences;

public class PathManager {
    private static final String PREF_KEY_PATH = "user.filepath";
    public static String currentPath;

    public static String getOrChoosePath(Stage stage) {
        Preferences prefs = Preferences.userNodeForPackage(PathManager.class);
        String savedPath = prefs.get(PREF_KEY_PATH, null);

        if (savedPath != null && new File(savedPath).isDirectory()) {
            currentPath = savedPath;
            return savedPath;
        } else {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("폴더를 지정 해주세요");
            File selectedDirectory = directoryChooser.showDialog(stage);
            if (selectedDirectory != null) {
                prefs.put(PREF_KEY_PATH, selectedDirectory.getAbsolutePath());
                currentPath = selectedDirectory.getAbsolutePath(); // 수정된 부분!
                return currentPath;
            } else {
                return null;
            }
        }
    }
}
