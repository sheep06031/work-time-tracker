package employee;

import java.util.UUID;


public class Employee {
    private String name;
    private String birth;
    private String address;
    private String employeedate;
    private String phoneNumber;
    private String employeeId;

    public Employee(String name, String birth, String address, String employeedate, String phoneNumber) {
        this.name = name;
        this.birth = birth;
        this.address = address;
        this.employeedate = employeedate;
        this.phoneNumber = phoneNumber;
        employeeId = UUID.randomUUID().toString();
    }
    public String getEmployeeId() {return employeeId; }
    public String getName() { return name; }
    public String getBirth() { return birth; }
    public String getAddress() { return address; }
    public String getEmployeedate() { return employeedate; }
    public String getPhoneNumber() { return phoneNumber; }
}
