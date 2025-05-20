package service;

import employee.Employee;
import employee.EmployeeManager;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class EmployeeService {
    private final ObservableList<Employee> masterList;
    private final FilteredList<Employee> filteredList;

    public EmployeeService() {
        this.masterList = EmployeeManager.getEmployeeList();
        this.filteredList = new FilteredList<>(masterList, p -> true);
    }

    public FilteredList<Employee> getFilteredList() {
        return filteredList;
    }

    public void filterBy(String field, String input) {
        final String keyword = input.toLowerCase().trim();

        filteredList.setPredicate(e -> {
            return switch (field) {
                case "이름" -> e.getName().toLowerCase().contains(keyword);
                case "생년월일" -> e.getBirth().toLowerCase().contains(keyword);
                case "전화번호" -> e.getPhoneNumber().toLowerCase().contains(keyword);
                default -> true;
            };
        });
    }


}
