package com.max.maxim;

import com.max.maxim.configuration.BaseConfig;
import com.max.maxim.constant.MaximConstant;
import com.max.maxim.core.client.ImClient;
import com.max.maxim.core.server.ImServer;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationRunner implements CommandLineRunner {

  @Resource
  private BaseConfig baseConfig;

  @Override
  public void run(String... args) throws Exception {
    int runnerMode = baseConfig.getRunnerMode();
    if (runnerMode == MaximConstant.CLIENT_MODE) {
      ImClient imClient = new ImClient();
      try {
        imClient.connect(baseConfig.getClientHost(), baseConfig.getClientPort());
      } catch (Exception e) {
        log.error("client fails to start, check if server is running...", e);
      }
    } else {
      ImServer imServer = new ImServer();
      imServer.bind(baseConfig.getServerHost(), baseConfig.getServerPort());
    }
  }
}
