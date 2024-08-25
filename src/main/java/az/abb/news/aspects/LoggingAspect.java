package az.abb.news.aspects;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoggingAspect {
    static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* az.abb.news.service..*(..))")
    public void logBeforeServiceMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Service method called: {} ", methodName);
    }

    @AfterReturning("execution(* az.abb.news.service..*(..))")
    public void logAfterServiceMethod(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Service Method finished: {} ", methodName);
    }

    @AfterThrowing(value = "execution(* az.abb.news.service..*(..))", throwing = "exception")
    public void logAfterThrowingServiceMethod(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String exceptionName = exception.getClass().getSimpleName();
        logger.error("Service method {} threw exception: {}", methodName, exceptionName);
    }

    @Before("execution(* az.abb.news.controller..*(..))")
    public void logBeforeControllerMethod(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String httpMethod = request.getMethod();
        String methodName = joinPoint.getSignature().getName();
        logger.info("Controller method called: {} with HTTP method: {}", methodName, httpMethod);
    }

    @AfterReturning("execution(* az.abb.news.controller..*(..))")
    public void logAfterControllerMethod(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String httpMethod = request.getMethod();
        String methodName = joinPoint.getSignature().getName();
        logger.info("Controller method finished: {} with HTTP method: {}", methodName, httpMethod);
    }

    @AfterThrowing(value = "execution(* az.abb.news.controller..*(..))", throwing = "exception")
    public void logAfterThrowingControllerMethod(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String exceptionName = exception.getClass().getSimpleName();
        logger.error("Controller method {} threw exception: {}", methodName, exceptionName);
    }
}

