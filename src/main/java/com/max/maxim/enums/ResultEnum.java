package com.max.maxim.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultEnum {

  SUCCESS(200, "success"),

  ERROR(500, "error"),

  SESSION_NOT_EXISTS(1001, "session not exists"),

  USER_NOT_LOGIN(1002, "user not login"),

  USER_NOT_FOUND_IN_DB(1003, "user not found in db"),

  PASSWORD_IS_NULL(1004, "password is null"),

  PASSWORD_NOT_MATCHED(1005, "password not matched"),

  INPUT_NULL_OR_EMPTY(2001, "input null or empty"),

  ;

  private final Integer code;

  private final String message;
}
