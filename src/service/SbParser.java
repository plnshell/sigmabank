package service;

import model.Department;
import model.Employee;
import model.Manager;
import util.NumberUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class SbParser {
    private SbParser() {}

    public static Map<String, Department> parseFiles(List<Path> sbFiles) {
        Map<Integer, Manager> managersById = new HashMap<>();
        List<Employee> employees = new ArrayList<>();
        List<String> errorLines = new ArrayList<>();

        for (Path file : sbFiles) {
            try {
                List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
                for (String raw : lines) {
                    String line = raw.trim();
                    if (line.isEmpty()) continue;

                    String[] parts = line.split(",");
                    if (parts.length != 5) {
                        errorLines.add(line);
                        continue;
                    }

                    String role = parts[0].trim();
                    String idStr = parts[1].trim();
                    String name = parts[2].trim();
                    String salaryStr = parts[3].trim();
                    String depOrManager = parts[4].trim();

                    if (!isPositiveInt(idStr)) {
                        errorLines.add(line);
                        continue;
                    }
                    int id = Integer.parseInt(idStr);

                    if ("Manager".equalsIgnoreCase(role)) {
                        if (!NumberUtils.isValidSalary(salaryStr) || depOrManager.isEmpty()) {
                            errorLines.add(line);
                            continue;
                        }
                        double salary = NumberUtils.roundUpTwoDecimals(NumberUtils.parseSalary(salaryStr));
                        managersById.put(id, new Manager(id, name, salary, depOrManager));

                    } else if ("Employee".equalsIgnoreCase(role)) {
                        if (!NumberUtils.isValidSalary(salaryStr) || !isPositiveInt(depOrManager)) {
                            errorLines.add(line);
                            continue;
                        }
                        double salary = NumberUtils.roundUpTwoDecimals(NumberUtils.parseSalary(salaryStr));
                        int managerId = Integer.parseInt(depOrManager);
                        employees.add(new Employee(id, name, salary, managerId));

                    } else {
                        errorLines.add(line);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла " + file + ": " + e.getMessage());
            }
        }

        Map<String, Department> departments = new HashMap<>();
        for (Manager m : managersById.values()) {
            departments.computeIfAbsent(m.getDepartmentName(), Department::new)
                    .setManager(m);
        }

        for (Employee e : employees) {
            Manager mgr = managersById.get(e.getManagerId());
            if (mgr == null) {
                errorLines.add(e.toSbFormat());
                continue;
            }
            departments.computeIfAbsent(mgr.getDepartmentName(), Department::new)
                    .addEmployee(e);
        }

        if (!errorLines.isEmpty()) {
            writeErrors(errorLines);
        }

        return departments;
    }

    private static boolean isPositiveInt(String s) {
        try {
            return Integer.parseInt(s) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void writeErrors(List<String> errors) {
        try {
            Files.write(Paths.get("error.log"), errors, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Ошибка записи error.log: " + e.getMessage());
        }
    }
}
