package ru.kors.springstudents.service;

import java.util.Map;

public interface VisitCounterService {

  void incrementVisit(String requestPath);

  long getVisitsForPath(String requestPath);

  Map<String, Long> getAllVisitCounts();

  long getTotalVisits();

  void clearAllVisitCounts();
}