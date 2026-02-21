package org.univ_paris8.iut.montreuil.dev_avance.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private static final String NO_CORRELATION_ID = "no-id";

    private String corrId(String correlationId) {
        return correlationId != null ? correlationId.substring(0, 8) : NO_CORRELATION_ID;
    }

    @Around("execution(* org.univ_paris8.iut.montreuil.dev_avance.service.*.*(..))")
    public Object logServiceCall(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();
        String correlationId = MDC.get("correlationId");

        String argsDescription = buildArgsDescription(signature.getParameterNames(), joinPoint.getArgs());

        logger.info("→ [{}] {}.{}({})", corrId(correlationId), className, methodName, argsDescription);

        long debut = System.currentTimeMillis();

        try {
            Object resultat = joinPoint.proceed();

            long duree = System.currentTimeMillis() - debut;

            if (duree > 1000) {
                logger.warn("⚠ [{}] {}.{} a pris {}ms — c'est un peu long !",
                        corrId(correlationId), className, methodName, duree);
            } else {
                logger.info("✓ [{}] {}.{} terminé en {}ms",
                        corrId(correlationId), className, methodName, duree);
            }

            return resultat;

        } catch (Exception e) {
            long duree = System.currentTimeMillis() - debut;
            logger.error("✗ [{}] {}.{} a échoué après {}ms — {} : {}",
                    corrId(correlationId), className, methodName, duree,
                    e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
    }

    /**
     * Construit une description lisible des arguments d'une méthode,
     * en masquant les champs sensibles comme "password".
     */
    private String buildArgsDescription(String[] paramNames, Object[] args) {
        if (args == null || args.length == 0) {
            return "aucun argument";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i > 0)
                sb.append(", ");
            String paramName = (paramNames != null && i < paramNames.length) ? paramNames[i] : "arg" + i;

            if (isSensitive(paramName)) {
                sb.append(paramName).append("=[MASQUÉ]");
            } else if (args[i] == null) {
                sb.append(paramName).append("=null");
            } else {
                String valeur = args[i].toString();
                if (valeur.length() > 80) {
                    valeur = valeur.substring(0, 80) + "…";
                }
                sb.append(paramName).append("=").append(valeur);
            }
        }
        return sb.toString();
    }

    private boolean isSensitive(String paramName) {
        String lower = paramName.toLowerCase();
        return lower.contains("password") || lower.contains("token") || lower.contains("secret");
    }
}
