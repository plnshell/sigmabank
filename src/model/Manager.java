package model;

public class Manager {
    private final int id;
    private final String name;
    private final double salary;
    private final String departmentName;

    public Manager(int id, String name, double salary, String departmentName) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.departmentName = departmentName;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getSalary() { return salary; }
    public String getDepartmentName() { return departmentName; }

    public String toSbFormat() {
        return String.format("Manager,%d,%s,%.0f", id, name, salary);
    }
}
