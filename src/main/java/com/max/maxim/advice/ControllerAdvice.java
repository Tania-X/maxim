package com.max.maxim.advice;

import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.constant.MaximConstant;
import com.max.maxim.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ControllerAdvice {

  private final ThreadLocal<Long> logTime = new ThreadLocal<>();

  @Before("com.max.maxim.advice.Pointcuts.httpLog()")
  public void doBefore(JoinPoint joinPoint) {
    HttpServletRequest request = null;
    Object[] args = joinPoint.getArgs();
    List<Object> list = new ArrayList<>();
    for (Object arg : args) {
      if (arg instanceof HttpServletRequest) {
        request = (HttpServletRequest) arg;
        list.add(HttpServletRequest.class.getSimpleName());
      }
      if (arg instanceof HttpServletResponse) {
        list.add(HttpServletResponse.class.getSimpleName());
      }
    }
    if (request != null) {
      log.info(
          "http request info: uri={}, request method={}, source ip addr={}, called method={}.{}(), port={}, params={}",
          request.getRequestURI(), request.getMethod(), request.getRemoteHost(),
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
          request.getRemotePort(), list);
    } else {
      log.info("http request info: called method={}.{}(), params={}",
          joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(),
          list);
    }
  }

  @Around("com.max.maxim.advice.Pointcuts.httpLog()")
  public Object doAfterReturning(ProceedingJoinPoint joinPoint) {
    try {
      logTime.set(System.currentTimeMillis());
      MDC.put(MaximConstant.MAXIM_TRACE_ID,
          UUID.randomUUID().toString().replace("-", "").substring(0, 20));
      return joinPoint.proceed();
    } catch (Throwable t) {
      log.warn("unified exception handled", t);
      ResultEntity<String> resResult = ResultUtil.error(t.getMessage());
      log.info("response content: {}, time cost: {} ms", resResult,
          (System.currentTimeMillis() - (logTime.get() == null ? System.currentTimeMillis()
              : logTime.get())));
      return resResult;
    } finally {
      logTime.remove();
      MDC.remove(MaximConstant.MAXIM_TRACE_ID);
    }
  }

  @AfterReturning(returning = "resResult", pointcut = "com.max.maxim.advice.Pointcuts.httpLog()")
  public void doAfterReturning(Object resResult) {
    log.info("response content: {}, time cost: {} ms", resResult,
        (System.currentTimeMillis() - (logTime.get() == null ? System.currentTimeMillis()
            : logTime.get())));
    logTime.remove();
  }

}
