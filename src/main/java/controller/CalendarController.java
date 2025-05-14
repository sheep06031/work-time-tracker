package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarController {

    @FXML private ComboBox<Integer> yearCombo;
    @FXML private ComboBox<Integer> monthCombo;
    @FXML private GridPane calendarGrid;
    @FXML private TextField wageTextField;

    @FXML
    public void initialize() {
        int currentYear = LocalDate.now().getYear();

        for (int i = currentYear - 5; i <= currentYear + 5; i++) {
            yearCombo.getItems().add(i);
        }
        yearCombo.setValue(currentYear);

        for (int i = 1; i <= 12; i++) {
            monthCombo.getItems().add(i);
        }
        monthCombo.setValue(LocalDate.now().getMonthValue());

        yearCombo.setOnAction(e -> buildCalendar(yearCombo.getValue(), monthCombo.getValue()));
        monthCombo.setOnAction(e -> buildCalendar(yearCombo.getValue(), monthCombo.getValue()));

        buildCalendar(currentYear, LocalDate.now().getMonthValue());
    }

    @FXML
    private void onUpdateCalendar() {
        Integer year = yearCombo.getValue();
        Integer month = monthCombo.getValue();

        if (year != null && month != null) {
            buildCalendar(year, month);
        }
    }

    private void buildCalendar(int year, int month) {
        calendarGrid.getChildren().clear();

        String[] days = {"월", "화", "수", "목", "금", "토", "일"};
        for (int i = 0; i < days.length; i++) {
            Label dayLabel = new Label(days[i]);
            dayLabel.setStyle("-fx-font-weight: bold;");
            dayLabel.setMaxWidth(Double.MAX_VALUE);
            dayLabel.setAlignment(Pos.CENTER);
            calendarGrid.add(dayLabel, i, 0);
        }

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        int length = yearMonth.lengthOfMonth();
        int startCol = (firstDay.getDayOfWeek().getValue() + 6) % 7;

        int row = 1;
        int col = startCol;

        for (int day = 1; day <= length; day++) {
            VBox box = new VBox(5);
            box.setPrefSize(110, 110);
            box.setStyle("-fx-border-color: lightgray; -fx-alignment: top-center; -fx-padding: 5;");

            Label dateLabel = new Label(String.valueOf(day));
            TextField workInput = new TextField();
            workInput.setPromptText("근무시간");
            workInput.setTranslateY(10);

            CheckBox holidayCheck = new CheckBox("휴일 지정");
            holidayCheck.setTranslateX(-15);
            holidayCheck.setTranslateY(10);

            Label payLabel = new Label("급여: 0원");

            holidayCheck.selectedProperty().addListener((obs, oldVal, isSelected) -> {
                if (isSelected) {
                    box.setStyle("-fx-background-color: #fde3e6; -fx-border-color: lightgray; -fx-alignment: top-center; -fx-padding: 5;");
                } else {
                    box.setStyle("-fx-background-color: transparent; -fx-border-color: lightgray; -fx-alignment: top-center; -fx-padding: 5;");
                }
            });

            workInput.textProperty().addListener((obs, oldVal, newVal) -> {
                try {
                    String wageStr = wageTextField.getText().replace(",", "").trim();
                    int wage = Integer.parseInt(wageStr);
                    int hours = Integer.parseInt(newVal.trim());
                    int pay = wage * hours;
                    payLabel.setText("급여: " + pay + "원");
                } catch (NumberFormatException e) {
                    payLabel.setText("급여: -");
                }
            });

            LocalDate currentDate = LocalDate.of(year, month, day);
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                holidayCheck.setSelected(true);
            }

            box.getChildren().addAll(dateLabel, payLabel, holidayCheck, workInput);
            calendarGrid.add(box, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }
}
