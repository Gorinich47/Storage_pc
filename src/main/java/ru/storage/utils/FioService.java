package ru.storage.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FioService {

    private final ResourceLoader resourceLoader;

    public FioService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public List<String> loadFioList(String fileName) {
        // Путь внутри src/main/resources
        Resource resource = resourceLoader.getResource("classpath:fio_list/" + fileName + ".txt");

        // 1. Проверка на существование файла
        if (!resource.exists()) {
            // Можно логировать через @Slf4j: log.error("Файл не найден");
            return Collections.emptyList();
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {

            List<String> lines = reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty()) // Игнорируем пустые строки
                    .collect(Collectors.toList());

            // 2. Проверка на пустоту контента
            if (lines.isEmpty()) {
                // log.warn("Файл пуст");
                return Collections.emptyList();
            }

            return lines;

        } catch (Exception e) {
            // log.error("Ошибка при чтении файла", e);
            return Collections.emptyList();
        }
    }
}
