package com.max.maxim.advice;

import org.aspectj.lang.annotation.Pointcut;

/**
 * manage all the pointcuts here.
 */
public class Pointcuts {

  @Pointcut("execution(public * com.max.maxim.controller.*.*(..)) || execution(public * com.max.maxim.user.*.*(..))")
  public void httpLog() {

  }

}
