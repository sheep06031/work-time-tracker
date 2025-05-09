package com.example.worktimetracker;

import employee.Employee;


import employee.EmployeeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {


    //메인 창
    @FXML
    private BorderPane borderPane;







    @FXML
    private void loadHome() throws IOException {
        loadCenter("home.fxml");
    }

    @FXML
    private void loadEmployee_Management() throws IOException {
        loadCenter("employee_management.fxml");
    }

    @FXML
    private void loadMyInfo() throws IOException {
        loadCenter("myInfo.fxml");
    }







    private void loadCenter (String fxmlPath) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
        borderPane.setCenter(content);
    }






}