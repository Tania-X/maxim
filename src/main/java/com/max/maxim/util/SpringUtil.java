package com.max.maxim.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
public class SpringUtil implements ApplicationContextAware {

  private static ApplicationContext applicationContext;


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    if (ObjectUtils.isEmpty(SpringUtil.applicationContext)) {
      SpringUtil.applicationContext = applicationContext;
    }
  }

  public static <T> T getBean(Class<T> clazz) {
    return applicationContext.getBean(clazz);
  }

  public static Object getBean(String className) {
    return applicationContext.getBean(className);
  }
}
