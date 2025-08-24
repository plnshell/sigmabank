package model;

public class Employee {
    private final int id;
    private final String name;
    private final double salary;
    private final int managerId;

    public Employee(int id, String name, double salary, int managerId) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.managerId = managerId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public double getSalary() { return salary; }
    public int getManagerId() { return managerId; }

    public String toSbFormat() {
        return String.format("Employee,%d,%s,%.0f,%d", id, name, salary, managerId);
    }
}