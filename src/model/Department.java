package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Department {

    private final String departmentName;
    private Manager manager;
    private final List<Employee> employees = new ArrayList<>();

    public Department(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void sortEmployees(Comparator<Employee> comparator) {
        employees.sort(comparator);
    }
}
