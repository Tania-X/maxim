package com.max.maxim.event.listener;

import com.max.maxim.event.MaximEvent;
import java.util.ArrayList;
import java.util.List;

public class MaximListener {

  private final List<MaximEvent> listenerList = new ArrayList<>();

  public void addEvent(MaximEvent event) {
    listenerList.add(event);
  }

  public void removeEvent(MaximEvent event) {
    listenerList.remove(event);
  }

  public void emitEvents(String message) {
    for (MaximEvent maximEvent : listenerList) {
      maximEvent.onEvent(message);
    }
  }

}
