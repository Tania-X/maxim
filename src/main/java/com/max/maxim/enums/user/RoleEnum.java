package com.max.maxim.enums.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {

  ADMIN(0, "admin"),
  USER(10, "user");

  private final Integer roleId;

  private final String roleDesc;
}
