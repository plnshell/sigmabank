package service;

import model.Department;
import model.Employee;
import model.OutputMode;
import util.FileUtil;
import util.NumberUtils;

import java.util.*;
import java.nio.file.Paths;

public class StatService {
    private StatService() {}

    public static void generate(Map<String, Department> departments,
                                OutputMode outputMode,
                                String outputPath) {
        List<String> lines = new ArrayList<>();
        lines.add("department,min,max,mid");

        List<String> depNames = new ArrayList<>(departments.keySet());
        Collections.sort(depNames);

        for (String depName : depNames) {
            Department dep = departments.get(depName);
            List<Double> salaries = dep.getEmployees().stream()
                    .map(Employee::getSalary)
                    .filter(s -> s > 0)
                    .toList();

            double min = salaries.isEmpty() ? 0.0 : Collections.min(salaries);
            double max = salaries.isEmpty() ? 0.0 : Collections.max(salaries);
            double mid = salaries.isEmpty() ? 0.0 :
                    salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

            lines.add(String.format("%s,%.2f,%.2f,%.2f",
                    depName,
                    NumberUtils.roundUpTwoDecimals(min),
                    NumberUtils.roundUpTwoDecimals(max),
                    NumberUtils.roundUpTwoDecimals(mid)
            ));
        }

        if (outputMode == OutputMode.FILE) {
            FileUtil.writeToFile(Paths.get(outputPath), lines);
        } else {
            lines.forEach(System.out::println);
        }
    }
}
