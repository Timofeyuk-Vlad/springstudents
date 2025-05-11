package ru.kors.springstudents.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect    // Помечаем класс как аспект
@Component // Делаем его Spring-компонентом для обнаружения
public class LoggingAspect {

  // Создаем логгер для этого класса
  private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

  /**
   * Pointcut, который выбирает все публичные методы в классах из пакета ...controller.
   */
  @Pointcut("execution(public * ru.kors.springstudents.controller..*.*(..))")
  public void controllerMethods() {}

  /**
   * Pointcut, который выбирает все публичные методы в классах из пакета ...service.impl.
   */
  @Pointcut("execution(public * ru.kors.springstudents.service.impl..*.*(..))")
  public void serviceMethods() {}

  /**
   * Advice (совет) типа "Before" - выполняется перед вызовом метода, выбранного pointcut'ом.
   * Логирует вход в метод и его аргументы.
   * Применяется ко всем методам контроллеров и сервисов.
   */
  @Before("controllerMethods() || serviceMethods()")
  public void logMethodEntry(JoinPoint joinPoint) {
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.info("ENTERING: {}.{}() with arguments: {}", className, methodName, Arrays.toString(args));
  }

  /**
   * Advice типа "AfterReturning" - выполняется после успешного возврата из метода.
   * Логирует выход из метода и возвращаемое значение.
   * Применяется ко всем методам контроллеров и сервисов.
   * @param result Объект, возвращенный методом.
   */
  @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
  public void logMethodExit(JoinPoint joinPoint, Object result) {
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    log.info("EXITING: {}.{}() with result: {}", className, methodName, result);
  }

  /**
   * Advice типа "AfterThrowing" - выполняется, если метод выбрасывает исключение.
   * Логирует исключение.
   * Применяется ко всем методам контроллеров и сервисов.
   * @param ex Выброшенное исключение.
   */
  @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
  public void logException(JoinPoint joinPoint, Throwable ex) {
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    log.error("EXCEPTION in {}.{}() : {}", className, methodName, ex.getMessage(), ex); // Логируем с полным stack trace
  }

  /**
   * Advice типа "Around" - может выполняться до и после вызова метода,
   * а также измерять время выполнения. (Пример, можно не использовать если не нужно)
   *
   * @param proceedingJoinPoint Объект для управления вызовом целевого метода.
   * @return Результат вызова целевого метода.
   * @throws Throwable Если целевой метод выбрасывает исключение.
   */
    /* // Раскомментируй, если нужно логирование времени выполнения
    @Around("controllerMethods() || serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed(); // Вызываем целевой метод
        long endTime = System.currentTimeMillis();
        String className = proceedingJoinPoint.getSignature().getDeclaringTypeName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        log.info("TIME: {}.{}() executed in {} ms", className, methodName, (endTime - startTime));
        return result;
    }
    */
}