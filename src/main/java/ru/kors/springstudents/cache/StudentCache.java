package ru.kors.springstudents.cache;

import org.springframework.stereotype.Component;
import ru.kors.springstudents.dto.StudentDetailsDto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Collections;

@Component
public class StudentCache {

  private static final int MAX_CACHE_SIZE = 2;

  private final Map<Long, StudentDetailsDto> cache = Collections.synchronizedMap(
      new LinkedHashMap<Long, StudentDetailsDto>(MAX_CACHE_SIZE, 0.75f, true) {
        @Override
        protected boolean removeEldestEntry(Map.Entry<Long, StudentDetailsDto> eldest) {
          boolean shouldRemove = size() > MAX_CACHE_SIZE;
          if (shouldRemove) {
            System.out.println("==== CACHE EVICTED (LRU), removing ID: " + eldest.getKey() + " ====");
          }
          return shouldRemove;
        }
      });

  public Optional<StudentDetailsDto> get(Long studentId) {
    return Optional.ofNullable(cache.get(studentId));
  }

  public void put(Long studentId, StudentDetailsDto studentDetails) {
    cache.put(studentId, studentDetails);
  }

  public void evict(Long studentId) {
    cache.remove(studentId);
  }

  public void clear() {
    cache.clear();
  }
}