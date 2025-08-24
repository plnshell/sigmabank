package util;

import model.Department;
import model.Employee;

import java.util.*;

public class StatisticsCalculator {

    private StatisticsCalculator() {}

    public static List<String> calculate(Map<String, Department> departments) {
        List<String> result = new ArrayList<>();
        result.add("department, min, max, mid");

        departments.keySet().stream()
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .forEach(depName -> {
                    Department dept = departments.get(depName);

                    List<Double> salaries = dept.getEmployees().stream()
                            .map(Employee::getSalary)
                            .filter(s -> s > 0)
                            .toList();

                    double min = 0.0;
                    double max = 0.0;
                    double mid = 0.0;

                    if (!salaries.isEmpty()) {
                        min = salaries.stream().min(Double::compareTo).orElse(0.0);
                        max = salaries.stream().max(Double::compareTo).orElse(0.0);
                        mid = salaries.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
                    }

                    result.add(String.format(Locale.US, "%s,%.2f,%.2f,%.2f",
                            depName,
                            NumberUtils.roundUpTwoDecimals(min),
                            NumberUtils.roundUpTwoDecimals(max),
                            NumberUtils.roundUpTwoDecimals(mid)));
                });

        return result;
    }
}

