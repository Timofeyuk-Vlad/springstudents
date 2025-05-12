package ru.kors.springstudents.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kors.springstudents.dto.StudentDetailsDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

@Component
public class StudentCache {

  private static final Logger log = LoggerFactory.getLogger(StudentCache.class);

  private static final int MAX_CACHE_SIZE = 2;

  private final Map<Long, StudentDetailsDto> cache = Collections.synchronizedMap(
      new LinkedHashMap<Long, StudentDetailsDto>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, StudentDetailsDto> eldest) {
          boolean shouldRemove = size() > MAX_CACHE_SIZE;
          if (shouldRemove) {
            log.info("==== CACHE EVICTED (LRU), removing ID: {} ====", eldest.getKey());
          }
          return shouldRemove;
        }
      });

  public Optional<StudentDetailsDto> get(Long studentId) {
    StudentDetailsDto student = cache.get(studentId);
    if (student != null) {
      log.debug("Cache hit for student ID: {}", studentId);
    } else {
      log.debug("Cache miss for student ID: {}", studentId);
    }
    return Optional.ofNullable(student);
  }

  public void put(Long studentId, StudentDetailsDto studentDetails) {
    log.debug("Putting student ID: {} into cache", studentId);
    cache.put(studentId, studentDetails);
  }

  public void evict(Long studentId) {
    log.info("Evicting student ID: {} from cache", studentId);
    cache.remove(studentId);
  }

  public void clear() {
    log.info("Clearing all entries from student cache");
    cache.clear();
  }
}