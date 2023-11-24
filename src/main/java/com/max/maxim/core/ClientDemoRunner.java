package com.max.maxim.core;

import com.max.maxim.core.client.ImClient;

public class ClientDemoRunner {

  public static void main(String[] args) throws Exception {
    ImClient imClient = new ImClient();
    imClient.connect("127.0.0.1", 44444);
  }

}
