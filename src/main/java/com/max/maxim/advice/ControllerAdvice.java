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
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class ControllerAdvice {

  private final ThreadLocal<Long> logTime = new ThreadLocal<>();

  @Before("com.max.maxim.advice.Pointcuts.httpLog()")
  public void doBefore(JoinPoint joinPoint) {
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    Assert.notNull(requestAttributes, "requestAttributes should not be nullable.");
    HttpServletRequest request = requestAttributes.getRequest();
    Object[] args = joinPoint.getArgs();
    List<Object> list = new ArrayList<>();
    boolean flg = false;
    for (Object arg : args) {
      if (arg instanceof HttpServletRequest || arg instanceof HttpServletResponse) {
        list.add(arg.getClass().getSimpleName());
        flg = true;
      } else {
        list.add(arg);
      }
    }
    if (!flg) {
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
  public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      logTime.set(System.currentTimeMillis());
      MDC.put(MaximConstant.MAXIM_TRACE_ID,
          UUID.randomUUID().toString().replace("-", "").substring(0, 20));
      return joinPoint.proceed();
    } catch (Throwable t) {
      log.warn("AOP exception handled", t);
      ResultEntity<String> retResult = ResultUtil.error(t.getMessage());
      log.info("response content: {}, time cost: {} ms", retResult,
          (System.currentTimeMillis() - (logTime.get() == null ? System.currentTimeMillis()
              : logTime.get())));
      // later process will be executed in this case, without which could result in class cast exception
      if (t instanceof AuthenticationException) {
        throw t;
      } else {
        return retResult;
      }
    } finally {
      logTime.remove();
      MDC.remove(MaximConstant.MAXIM_TRACE_ID);
    }
  }

  @AfterReturning(returning = "retResult", pointcut = "com.max.maxim.advice.Pointcuts.httpLog()")
  public void doAfterReturning(Object retResult) {
    log.info("response content: {}, time cost: {} ms", retResult,
        (System.currentTimeMillis() - (logTime.get() == null ? System.currentTimeMillis()
            : logTime.get())));
    logTime.remove();
  }

}
