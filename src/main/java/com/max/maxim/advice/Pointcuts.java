package com.max.maxim.advice;

import jakarta.servlet.FilterConfig;
import org.aspectj.lang.annotation.Pointcut;

/**
 * manage all the pointcuts here.
 */
public class Pointcuts {

  /**
   * all the filters are related to {@link org.springframework.web.filter.GenericFilterBean#init(FilterConfig)}
   * while init method is modified by 'final', dynamic proxies can't be applied to it.
   * we have to ignore it from pointcut, otherwise this exception occurs:
   * Cannot invoke "org.apache.commons.logging.Log.isDebugEnabled()" because "this.logger" is null
   */
  @Pointcut("execution(public * com.max.maxim.controller.*.*(..))"
      + " || execution(public * com.max.maxim.user.*.*(..))"
      + " && !execution(public * com.max.maxim.user.*Filter.*(..))")
  public void httpLog() {

  }

}
