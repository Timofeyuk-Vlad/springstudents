package ru.kors.springstudents.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.service.VisitCounterService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong; // Используем AtomicLong
import java.util.stream.Collectors;

@Service
public class VisitCounterServiceImpl implements VisitCounterService {

  private static final Logger log = LoggerFactory.getLogger(VisitCounterServiceImpl.class);

  private final Map<String, AtomicLong> visitsByPath = new ConcurrentHashMap<>(); // Значение теперь AtomicLong
  private final AtomicLong totalVisitsCounter = new AtomicLong(0);

  @Override
  public void incrementVisit(String requestPath) {
    if (requestPath == null || requestPath.trim().isEmpty()) {
      log.warn("Attempted to increment visit count for null or empty request path.");
      return;
    }
    visitsByPath.computeIfAbsent(requestPath, k -> {
      log.debug("Creating new visit counter for path: {}", k);
      return new AtomicLong(0);
    }).incrementAndGet();

    totalVisitsCounter.incrementAndGet();
  }

  @Override
  public long getVisitsForPath(String requestPath) {
    if (requestPath == null) {
      return 0;
    }
    AtomicLong counter = visitsByPath.get(requestPath);
    return (counter != null) ? counter.get() : 0; // Используем .get()
  }

  @Override
  public Map<String, Long> getAllVisitCounts() {
    return visitsByPath.entrySet().stream()
        .collect(Collectors.toConcurrentMap(Map.Entry::getKey, entry -> entry.getValue().get())); // Используем .get()
  }

  @Override
  public long getTotalVisits() {
    return totalVisitsCounter.get(); // Используем .get()
  }

  @Override
  public void clearAllVisitCounts() {
    log.info("Clearing all visit counts. {} paths and {} total visits before clear.",
        visitsByPath.size(), totalVisitsCounter.get());
    visitsByPath.clear();
    totalVisitsCounter.set(0); // Сбрасываем AtomicLong до 0
    log.info("All visit counts cleared.");
  }
}