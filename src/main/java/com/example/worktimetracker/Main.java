package com.example.worktimetracker;

import employee.EmployeeManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.PathManager;
import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String path = PathManager.getOrChoosePath(stage);
        if (path != null) {
            EmployeeManager.instance.setEmployeeList();
            showMainWindow(stage, path);
        } else {
            stage.close();
            System.exit(0);
        }
    }

    private void showMainWindow(Stage stg, String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 600);
        stg.setTitle("Work Time Tracker");
        stg.setScene(scene);
        stg.show();
    }


    public static void main(String[] args) {
        launch();
    }
}