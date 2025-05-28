package ru.kors.springstudents.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.dto.LogTaskStatusDto;
import ru.kors.springstudents.service.AsyncLogGenerationService;
import org.springframework.beans.factory.annotation.Autowired; // Для self-инъекции
import org.springframework.context.annotation.Lazy;       // Для self-инъекции


import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
// import java.util.UUID; // Больше не нужен UUID
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong; // Используем AtomicLong для ID
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AsyncLogGenerationServiceImpl implements AsyncLogGenerationService {

  private static final Logger log = LoggerFactory.getLogger(AsyncLogGenerationServiceImpl.class);
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  private final Map<String, LogTaskStatusDto> taskStatuses = new ConcurrentHashMap<>();
  private final AtomicLong taskIdCounter = new AtomicLong(1); // Начальное значение для ID задачи

  @Value("${logging.file.path:logs}")
  private String sourceLogsDirectoryPath;

  @Value("${generated.logs.path:generated-logs}")
  private String generatedLogsDirectoryPath;

  private AsyncLogGenerationService self;

  @Autowired
  @Lazy
  public void setSelf(AsyncLogGenerationService self) {
    this.self = self;
  }

  @Override
  public String generateLogFileAsync(LocalDate date) {
    // Генерируем последовательный ID задачи
    long nextId = taskIdCounter.getAndIncrement();
    String taskId = String.valueOf(nextId); // Преобразуем long в String

    LogTaskStatusDto statusDto = new LogTaskStatusDto(taskId, LogTaskStatusDto.TaskStatus.PENDING, "Task created", null);
    taskStatuses.put(taskId, statusDto);

    log.info("Task {} created for generating log for date: {}", taskId, date);
    self.processLogGeneration(taskId, date); // Вызываем асинхронный метод через self-прокси
    return taskId;
  }

  @Async // Этот метод должен быть public (или в интерфейсе) для работы @Async через прокси
  @Override // Если он в интерфейсе
  public void processLogGeneration(String taskId, LocalDate date) { // Сделаем public для вызова через self
    taskStatuses.computeIfPresent(taskId, (id, status) -> {
      status.setStatus(LogTaskStatusDto.TaskStatus.PROCESSING);
      status.setMessage("Processing log generation...");
      return status;
    });
    log.info("Task {}: Starting log generation for {}", taskId, date);

    try {
      Path generatedDir = Paths.get(generatedLogsDirectoryPath);
      if (!Files.exists(generatedDir)) {
        Files.createDirectories(generatedDir);
        log.info("Created directory for generated logs: {}", generatedDir);
      }

      String sourceLogFileNamePattern = "application." + date.format(DATE_FORMATTER) + ".log";
      Path sourceLogFilePath = Paths.get(sourceLogsDirectoryPath, sourceLogFileNamePattern);
      File sourceLogFile = sourceLogFilePath.toFile();

      // Используем taskId полностью в имени файла, так как он теперь числовой и короткий
      String targetFileName = "log_report_" + date.format(DATE_FORMATTER) + "_task_" + taskId + ".log";
      Path targetFilePath = generatedDir.resolve(targetFileName);

      if (!sourceLogFile.exists()) {
        if (date.equals(LocalDate.now())) {
          sourceLogFilePath = Paths.get(sourceLogsDirectoryPath, "application.log");
          sourceLogFile = sourceLogFilePath.toFile();
          if (!sourceLogFile.exists()){
            log.warn("Task {}: Source log file application.log not found for today.", taskId);
            throw new IOException("Source log file application.log not found for today.");
          }
          log.info("Task {}: Using current application.log as source for today's date.", taskId);
        } else {
          log.warn("Task {}: Source log file {} not found.", taskId, sourceLogFileNamePattern);
          throw new IOException("Source log file " + sourceLogFileNamePattern + " not found.");
        }
      }

      List<String> linesToWrite;
      try (Stream<String> lines = Files.lines(sourceLogFilePath, StandardCharsets.UTF_8)) {
        linesToWrite = lines.collect(Collectors.toList());
      }

      Files.write(targetFilePath, linesToWrite, StandardCharsets.UTF_8,
          StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

      log.info("Task {}: Simulating long operation...", taskId);
      Thread.sleep(20000);

      taskStatuses.computeIfPresent(taskId, (id, status) -> {
        status.setStatus(LogTaskStatusDto.TaskStatus.COMPLETED);
        status.setMessage("Log file generated successfully.");
        status.setFilePath(targetFilePath.toString());
        return status;
      });
      log.info("Task {}: Log generation completed. File: {}", taskId, targetFilePath);

    } catch (IOException | InterruptedException e) {
      log.error("Task {}: Error during log generation for date {}: {}", taskId, date, e.getMessage(), e);
      taskStatuses.computeIfPresent(taskId, (id, status) -> {
        status.setStatus(LogTaskStatusDto.TaskStatus.FAILED);
        status.setMessage("Error during log generation: " + e.getMessage());
        return status;
      });
    }
  }


  @Override
  public Optional<LogTaskStatusDto> getTaskStatus(String taskId) {
    return Optional.ofNullable(taskStatuses.get(taskId));
  }

  @Override
  public Optional<String> getGeneratedLogFilePath(String taskId) {
    LogTaskStatusDto statusDto = taskStatuses.get(taskId);
    if (statusDto != null && statusDto.getStatus() == LogTaskStatusDto.TaskStatus.COMPLETED) {
      return Optional.ofNullable(statusDto.getFilePath());
    }
    return Optional.empty();
  }
}