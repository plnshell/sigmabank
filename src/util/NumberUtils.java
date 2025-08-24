package util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {

    private NumberUtils() {}
    public static boolean isValidSalary(String salaryStr) {
        if (salaryStr == null || salaryStr.isBlank()) {
            return false;
        }
        try {
            double val = Double.parseDouble(salaryStr.trim());
            return val > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static double parseSalary(String salaryStr) {
        return Double.parseDouble(salaryStr.trim());
    }

    public static double roundUpTwoDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.CEILING)
                .doubleValue();
    }
}
