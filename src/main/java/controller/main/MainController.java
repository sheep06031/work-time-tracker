package controller.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {


    @FXML private BorderPane borderPane;
    @FXML private Button exitButton;

    @FXML
    private void exitProgram() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }


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
        loadCenter("/com/example/worktimetracker/payrollManagement.fxml");
    }


    private void loadCenter(String fxmlPath) throws IOException {
        Parent content = FXMLLoader.load(getClass().getResource(fxmlPath));
        borderPane.setCenter(content);
    }
}
