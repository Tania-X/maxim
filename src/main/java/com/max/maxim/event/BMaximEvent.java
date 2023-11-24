package com.max.maxim.event;

public class BMaximEvent implements MaximEvent{

  @Override
  public void onEvent(String message) {
    System.out.println("b received the message: " + message);
  }
}
