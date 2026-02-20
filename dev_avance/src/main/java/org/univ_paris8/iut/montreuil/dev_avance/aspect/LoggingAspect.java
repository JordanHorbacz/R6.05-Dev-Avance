package org.univ_paris8.iut.montreuil.dev_avance.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* org.univ_paris8.iut.montreuil.dev_avance.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();

        logger.info("Entering method: {}", methodName);

        try {
            Object proceed = joinPoint.proceed();
            long executionTime = System.currentTimeMillis() - start;
            logger.info("Exiting method: {} executed in {} ms", methodName, executionTime);
            return proceed;
        } catch (Throwable e) {
            logger.error("Exception in method: {} with message: {}", methodName, e.getMessage());
            throw e;
        }
    }
}
