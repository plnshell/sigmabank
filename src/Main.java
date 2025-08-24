import model.CliOptions;
import service.CliParser;
import model.Department;
import model.Employee;
import service.SbParser;
import service.DepartmentWriter;
import util.StatisticsCalculator;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        CliOptions options;
        try {
            options = CliParser.parse(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Ошибка параметров: " + e.getMessage());
            return;
        }

        List<Path> sbFiles;
        try {
            sbFiles = Files.list(Paths.get("."))
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".sb"))
                    .toList();
        } catch (IOException e) {
            System.err.println("Ошибка поиска .sb файлов: " + e.getMessage());
            return;
        }

        if (sbFiles.isEmpty()) {
            System.err.println("В текущей директории нет .sb файлов");
            return;
        }

        Map<String, Department> departments = SbParser.parseFiles(sbFiles);

        if (options.getSortField() != null) {
            Comparator<Employee> comparator;
            if ("name".equals(options.getSortField())) {
                comparator = Comparator.comparing(Employee::getName, String.CASE_INSENSITIVE_ORDER);
            } else {
                comparator = Comparator.comparingDouble(Employee::getSalary);
            }

            if ("desc".equals(options.getSortOrder())) {
                comparator = comparator.reversed();
            }

            for (Department dept : departments.values()) {
                dept.sortEmployees(comparator);
            }
        }

        for (Department dept : departments.values()) {
            DepartmentWriter.writeDepartmentFile(dept);
        }

        if (options.isStatEnabled()) {
            List<String> stats = StatisticsCalculator.calculate(departments);
            if ("file".equals(options.getOutputMode())) {
                Path outFile = Paths.get(options.getOutputPath());
                try {
                    Files.createDirectories(outFile.getParent());
                    Files.write(outFile, stats);
                } catch (IOException e) {
                    System.err.println("Ошибка записи статистики в файл: " + e.getMessage());
                }
            } else {
                stats.forEach(System.out::println);
            }
        }
    }
}
