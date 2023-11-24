package com.max.maxim.event;

public class AMaximEvent implements MaximEvent{

  @Override
  public void onEvent(String message) {
    System.out.println("a received the message: " + message);
  }
}
