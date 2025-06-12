package ru.kors.springstudents.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.kors.springstudents.service.VisitCounterService;

@Service
public class VisitCounterServiceImpl implements VisitCounterService {

  private static final Logger log = LoggerFactory.getLogger(VisitCounterServiceImpl.class);
  private final Map<String, AtomicLong> visitsByPath = new ConcurrentHashMap<>();

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
  }

  @Override
  public long getVisitsForPath(String requestPath) {
    if (requestPath == null) {
      return 0;
    }
    AtomicLong counter = visitsByPath.get(requestPath);
    return (counter != null) ? counter.get() : 0;
  }

  @Override
  public Map<String, Long> getAllVisitCounts() {
    return visitsByPath.entrySet().stream()
        .collect(Collectors.toConcurrentMap(
            Map.Entry::getKey,
            entry -> entry.getValue().get()
        ));
  }

  @Override
  public long getTotalVisits() {
    return visitsByPath.values().stream()
        .mapToLong(AtomicLong::get)
        .sum();
  }

  @Override
  public void clearAllVisitCounts() {
    log.info("Clearing all visit counts. {} paths before clear.", visitsByPath.size());
    visitsByPath.clear();
    log.info("All visit counts cleared.");
  }
}
