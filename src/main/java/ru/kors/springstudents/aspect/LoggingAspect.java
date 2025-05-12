package ru.kors.springstudents.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

  private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

  @Pointcut("execution(public * ru.kors.springstudents.controller..*.*(..))")
  public void controllerMethods() {}

  @Pointcut("execution(public * ru.kors.springstudents.service.impl..*.*(..))")
  public void serviceMethods() {}

  @Before("controllerMethods() || serviceMethods()")
  public void logMethodEntry(JoinPoint joinPoint) {
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    Object[] args = joinPoint.getArgs();
    log.info("ENTERING: {}.{}() with arguments: {}", className, methodName, Arrays.toString(args));
  }

  @AfterReturning(pointcut = "controllerMethods() || serviceMethods()", returning = "result")
  public void logMethodExit(JoinPoint joinPoint, Object result) {
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    log.info("EXITING: {}.{}() with result: {}", className, methodName, result);
  }

  @AfterThrowing(pointcut = "controllerMethods() || serviceMethods()", throwing = "ex")
  public void logException(JoinPoint joinPoint, Throwable ex) {
    String className = joinPoint.getSignature().getDeclaringTypeName();
    String methodName = joinPoint.getSignature().getName();
    log.error("EXCEPTION in {}.{}() : {}", className, methodName, ex.getMessage(), ex);
  }
}