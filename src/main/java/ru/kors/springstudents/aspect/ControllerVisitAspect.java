package ru.kors.springstudents.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.kors.springstudents.service.VisitCounterService;

@Aspect
@Component
@RequiredArgsConstructor
public class ControllerVisitAspect {

  private static final Logger log = LoggerFactory.getLogger(ControllerVisitAspect.class);
  private final VisitCounterService visitCounterService;

  /**
   * Pointcut, который выбирает все публичные методы во всех классах
   * в пакете ru.kors.springstudents.controller,
   * ИСКЛЮЧАЯ класс VisitStatsController.
   */
  @Pointcut("execution(public * ru.kors.springstudents.controller..*.*(..)) && " +
      "!within(ru.kors.springstudents.controller.VisitStatsController)")
  public void allControllersExceptStats() {}

  /**
   * Advice, который выполняется перед вызовом методов контроллера (кроме VisitStatsController).
   * Регистрирует посещение.
   */
  @Before("allControllersExceptStats()")
  public void incrementControllerVisit(JoinPoint joinPoint) {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      String method = request.getMethod(); // GET, POST, etc.
      String path = request.getRequestURI(); // /api/v1/students, /api/v1/events/1, etc.

      // Формируем уникальный ключ для счетчика
      String visitKey = method + " " + path;
      log.debug("Aspect: Intercepted request. Incrementing visit count for: {}", visitKey);
      visitCounterService.incrementVisit(visitKey);
    } else {
      log.warn("Aspect: Could not retrieve HttpServletRequest. Visit not counted for method: {}",
          joinPoint.getSignature().toShortString());
    }
  }
}