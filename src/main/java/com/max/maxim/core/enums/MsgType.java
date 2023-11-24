package com.max.maxim.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MsgType {

  SIMPLE_CHAT(0),
  GROUP_CHAT(1),

  ;

  private final Integer mark;
}
