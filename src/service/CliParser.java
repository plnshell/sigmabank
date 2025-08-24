package service;

import model.CliOptions;
import java.util.Locale;

public class CliParser {

    private CliParser() {}

    public static CliOptions parse(String[] args) {
        CliOptions options = new CliOptions();

        for (String arg : args) {
            if (arg.startsWith("--sort=") || arg.startsWith("-s=")) {
                String value = extractValue(arg);
                if (!value.equals("name") && !value.equals("salary")) {
                    throw new IllegalArgumentException("Недопустимый тип сортировки: " + value);
                }
                options.setSortField(value);

            } else if (arg.startsWith("--order=")) {
                String value = extractValue(arg);
                if (!value.equals("asc") && !value.equals("desc")) {
                    throw new IllegalArgumentException("Недопустимый порядок сортировки: " + value);
                }
                options.setSortOrder(value);

            } else if (arg.equals("--stat")) {
                options.setStatEnabled(true);

            } else if (arg.startsWith("--output=") || arg.startsWith("-o=")) {
                String value = extractValue(arg);
                if (!value.equals("console") && !value.equals("file")) {
                    throw new IllegalArgumentException("Недопустимый режим вывода статистики: " + value);
                }
                options.setOutputMode(value);

            } else if (arg.startsWith("--path=")) {
                String value = extractValue(arg);
                if (value.isEmpty()) {
                    throw new IllegalArgumentException("Путь к файлу статистики не может быть пустым");
                }
                options.setOutputPath(value);

            } else {
                throw new IllegalArgumentException("Неизвестный параметр: " + arg);
            }
        }

        if (options.getSortOrder() != null && options.getSortField() == null) {
            throw new IllegalArgumentException("--order нельзя использовать без --sort");
        }

        if (options.getOutputPath() != null && !"file".equals(options.getOutputMode())) {
            throw new IllegalArgumentException("--path можно использовать только при --output=file");
        }

        if ("file".equals(options.getOutputMode()) && options.getOutputPath() == null) {
            throw new IllegalArgumentException("--output=file требует указания --path=<файл>");
        }

        if (!options.isStatEnabled() &&
                (options.getOutputMode() != null || options.getOutputPath() != null)) {
            throw new IllegalArgumentException("Флаги статистики допустимы только вместе с --stat");
        }

        return options;
    }

    private static String extractValue(String arg) {
        String[] parts = arg.split("=", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Параметр указан без значения: " + arg);
        }
        return parts[1].trim().toLowerCase(Locale.ROOT);
    }
}
