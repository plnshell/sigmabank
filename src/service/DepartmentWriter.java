package service;

import model.Department;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentWriter {

    private DepartmentWriter() {}

    public static void writeDepartmentFile(Department department) {
        List<String> lines = new ArrayList<>();
        if (department.getManager() != null) {
            lines.add(department.getManager().toSbFormat());
        }
        department.getEmployees().forEach(e -> lines.add(e.toSbFormat()));

        Path file = Paths.get(department.getDepartmentName() + ".sb");

        try {
            Files.write(file, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Ошибка записи файла департамента " + file + ": " + e.getMessage());
        }
    }
}
