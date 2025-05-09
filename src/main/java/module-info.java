module com.example.worktimetracker {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.worktimetracker to javafx.fxml;
    exports com.example.worktimetracker;

    opens employee to javafx.base, javafx.fxml;

}