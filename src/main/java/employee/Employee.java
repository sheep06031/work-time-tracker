package employee;

public class Employee {
    private String name;
    private String birth;
    private String address;
    private String employeedate;
    private String phoneNumber;

    public Employee(String name, String birth, String address, String employeedate, String phoneNumber) {
        this.name = name;
        this.birth = birth;
        this.address = address;
        this.employeedate = employeedate;
        this.phoneNumber = phoneNumber;
    }
    public String getName() { return name; }
    public String getBirth() { return birth; }
    public String getAddress() { return address; }
    public String getEmployeedate() { return employeedate; }
    public String getPhoneNumber() { return phoneNumber; }
}
