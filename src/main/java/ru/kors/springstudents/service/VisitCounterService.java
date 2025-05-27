package ru.kors.springstudents.service;

import java.util.Map;

public interface VisitCounterService {

  /**
   * Увеличивает счетчик посещений для указанного пути URL (ключа).
   * @param requestPath Ключ, обычно представляющий путь запроса.
   */
  void incrementVisit(String requestPath);

  /**
   * Получает количество посещений для указанного пути.
   * @param requestPath Ключ (путь запроса).
   * @return Количество посещений или 0, если для данного пути нет записей.
   */
  long getVisitsForPath(String requestPath);

  /**
   * Получает карту со статистикой посещений для всех URL.
   * @return Карта, где ключ - URL-путь, значение - количество посещений.
   */
  Map<String, Long> getAllVisitCounts();

  /**
   * Получает общее количество всех зарегистрированных посещений.
   * @return Общее количество посещений.
   */
  long getTotalVisits();

  /**
   * Очищает всю статистику посещений (сбрасывает все счетчики).
   */
  void clearAllVisitCounts();
}