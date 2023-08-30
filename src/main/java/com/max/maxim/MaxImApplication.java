package com.max.maxim;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableJdbcHttpSession
@MapperScan("com.max.maxim.dao")
@Slf4j
public class MaxImApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(MaxImApplication.class,
        args);
    ConfigurableEnvironment environment = context.getEnvironment();
    log.info("===============  Start  ==============");
    log.info("Java version: {}", environment.getProperty("java.version"));
    log.info("OS name: {}", environment.getProperty("os.name"));
    log.info("OS version: {}", environment.getProperty("os.version"));
    log.info("OS arch: {}", environment.getProperty("os.arch"));
    log.info("file encoding: {}", environment.getProperty("file.encoding"));
    log.info("user directory: {}", environment.getProperty("user.dir"));
    log.info("user timezone: {}", environment.getProperty("user.timezone"));
    log.info("Server port: {}", environment.getProperty("server.port"));
    log.info("===============  End  ==============");
  }

}
