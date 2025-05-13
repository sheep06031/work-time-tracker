package com.example.worktimetracker;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainController {

    // 메인 창
    @FXML
    private BorderPane borderPane;

    @FXML
    private void loadHome() throws IOException {
        loadCenter("/com/example/worktimetracker/home.fxml");
    }

    @FXML
    private void loadEmployee_Management() throws IOException {
        loadCenter("/com/example/worktimetracker/employee_management.fxml");
    }

    @FXML
    private void loadMyInfo() throws IOException {
        loadCenter("/com/example/worktimetracker/myInfo.fxml");
    }

    private void loadCenter(String fxmlPath) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
        borderPane.setCenter(content);
    }
}
