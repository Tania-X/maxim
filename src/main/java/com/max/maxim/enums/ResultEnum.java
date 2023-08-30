package com.max.maxim.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultEnum {

  SESSION_NOT_EXISTS(1001, "session not exists"),

  USER_NOT_LOGIN(1002, "user not login"),

  ;

  private final Integer code;

  private final String message;
}
