package com.max.maxim.core;

import com.max.maxim.core.server.ImServer;

public class ServerDemoRunner {

  public static void main(String[] args) throws Exception {
    ImServer imServer = new ImServer();
    imServer.bind("127.0.0.1", 44444);
  }

}
