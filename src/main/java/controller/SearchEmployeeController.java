package controller;

import employee.Employee;
import employee.EmployeeManager;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SearchEmployeeController {
    @FXML private TableView<Employee> employeeTableView;
    @FXML private TableColumn<Employee, String> nameColumnForSearch;
    @FXML private TableColumn<Employee, String> birthColumnForSearch;
    @FXML private TableColumn<Employee, String> phoneNumberColumnForSearch;
    @FXML private TextField searchTextField;
    @FXML private ChoiceBox<String> searchChoiceBox;
    @FXML private Button selectEmployeeButton;


    private FilteredList<Employee> flEmployee = new FilteredList<>(EmployeeManager.getEmployeeList(), p -> true);

    private CalendarController calendarController;



    @FXML
    private void initialize() {
        nameColumnForSearch.setCellValueFactory(new PropertyValueFactory<>("name"));
        birthColumnForSearch.setCellValueFactory(new PropertyValueFactory<>("birth"));
        phoneNumberColumnForSearch.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        searchChoiceBox.getItems().addAll("이름", "생년월일", "전화번호");
        searchChoiceBox.setValue("이름");

        employeeTableView.setItems(flEmployee);

        searchTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            String selected = searchChoiceBox.getValue();
            if (selected == null) return;
            String keyword = newValue.toLowerCase().trim();
            switch (selected) {
                case "이름" -> flEmployee.setPredicate(e -> e.getName().toLowerCase().contains(keyword));
                case "생년월일" -> flEmployee.setPredicate(e -> e.getBirth().toLowerCase().contains(keyword));
                case "전화번호" -> flEmployee.setPredicate(e -> e.getPhoneNumber().toLowerCase().contains(keyword));
            }
        });

        searchChoiceBox.valueProperty().addListener((obs, oldVal, newVal) -> searchTextField.setText(searchTextField.getText()));
        boolean noSelection = employeeTableView.getSelectionModel().getSelectedItem() == null;
        selectEmployeeButton.setDisable(noSelection);

        employeeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean disable = newSel == null;
            selectEmployeeButton.setDisable(disable);
        });

        employeeTableView.getColumns().forEach(col -> col.setReorderable(false));
    }

    @FXML
    private void selectEmployee() {
        Employee selected = employeeTableView.getSelectionModel().getSelectedItem();
        if (selected == null || calendarController == null) return;

        calendarController.setEmployee(selected);

        Stage stage = (Stage) selectEmployeeButton.getScene().getWindow();
        stage.close();
    }


    public void setCalendarController(CalendarController calendarController) {
        this.calendarController = calendarController;
    }
}
