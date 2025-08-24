package service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public class ErrorLogger {

    private static final Path ERROR_LOG = Paths.get("error.log");

    private ErrorLogger() {}

    public static void writeErrors(List<String> errorLines) {
        if (errorLines == null || errorLines.isEmpty()) {
            return;
        }
        try {
            Files.write(ERROR_LOG, errorLines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Ошибка записи error.log");
            e.printStackTrace();
        }
    }
}
