package com.edu.common.log.aspect;

import com.edu.common.log.annotation.Log;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 操作日志切面
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private static final int MAX_PARAM_LENGTH = 2000;

    private ThreadLocal<Long> startTime = new ThreadLocal<>();

    @Before("@annotation(controllerLog)")
    public void doBefore(Log controllerLog) {
        startTime.set(System.currentTimeMillis());
    }

    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object result) {
        handleLog(joinPoint, controllerLog, null, result);
    }

    @AfterThrowing(pointcut = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    private void handleLog(JoinPoint joinPoint, Log controllerLog, Exception e, Object result) {
        try {
            long costTime = System.currentTimeMillis() - startTime.get();
            startTime.remove();

            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            Map<String, Object> logData = new LinkedHashMap<>();
            logData.put("description", controllerLog.value());
            logData.put("operateType", controllerLog.operateType().name());
            logData.put("className", className);
            logData.put("methodName", methodName);
            logData.put("costTime", costTime + "ms");

            if (e != null) {
                logData.put("status", "FAIL");
                logData.put("errorMsg", e.getMessage());
            } else {
                logData.put("status", "SUCCESS");
            }

            if (controllerLog.isSaveRequestData()) {
                Map<String, Object> params = getRequestParams(joinPoint);
                logData.put("requestParams", params);
            }

            log.info("操作日志: {}", logData);
        } catch (Exception exp) {
            log.error("记录操作日志异常: {}", exp.getMessage(), exp);
        }
    }

    private Map<String, Object> getRequestParams(JoinPoint joinPoint) {
        Map<String, Object> params = new LinkedHashMap<>();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if (args[i] instanceof HttpServletRequest || args[i] instanceof HttpServletResponse) {
                continue;
            }
            params.put(parameterNames[i], args[i]);
        }

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            params.put("requestURI", request.getRequestURI());
            params.put("requestMethod", request.getMethod());
        }

        return params;
    }
}
