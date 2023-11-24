package com.max.maxim.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class BaseConfig {

  @Value("${im.runner.mode}")
  private int runnerMode;

  @Value("${im.server.port}")
  private int serverPort;

  @Value("${im.client.port}")
  private int clientPort;

  @Value("${im.server.host}")
  private String serverHost;

  @Value("${im.client.host}")
  private String clientHost;

}
