package vn.utc.service.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingConfig {
    @Before("execution(* vn.utc.service.*.*(..))")
    public void logBeforeMethodExecution(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().toShortString();

        log.info("Executing method: {} with arguments: {}", methodName, joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "execution(* vn.utc.service.*.*(..))", returning = "result")
    public void logAfterMethodExecution(JoinPoint joinPoint, Object result) throws JsonProcessingException {
        String methodName = joinPoint.getSignature().toShortString();
        log.info("Method: {} returned: {}", methodName,result);
    }
    //
    @AfterThrowing(pointcut = "execution(* vn.utc.service.*.*(..))", throwing = "exception")
    public void logOnException(JoinPoint joinPoint, Exception exception) {
        String methodName = joinPoint.getSignature().toShortString();
        log.error("Method: {} threw exception: {}", methodName, exception.getMessage());
    }
}
