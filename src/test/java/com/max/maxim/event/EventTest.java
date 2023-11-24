package com.max.maxim.event;

import com.max.maxim.MaxImApplicationTests;
import com.max.maxim.event.listener.MaximListener;
import org.junit.jupiter.api.Test;

public class EventTest extends MaxImApplicationTests {

  @Test
  public void test() {

    MaximEvent eventA = new AMaximEvent();
    MaximEvent eventB = new BMaximEvent();
    MaximListener listener = new MaximListener();
    listener.addEvent(eventA);
    listener.addEvent(eventB);

    listener.emitEvents("Hello world");
  }

}
