module com.example.worktimetracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;

    exports com.example.worktimetracker;

    opens com.example.worktimetracker to javafx.fxml;

    exports controller.main;
    opens controller.main to javafx.fxml;

    exports controller.calendar;
    opens controller.calendar to javafx.fxml;

    exports controller.employee;
    opens controller.employee to javafx.fxml;

    exports controller.home;
    opens controller.home to javafx.fxml;

    exports employee;
    opens employee to javafx.fxml;

    exports repository;
    exports service;
    exports util;
}
