package com.max.maxim.service.impl;

import com.max.maxim.MaxImApplicationTests;
import com.max.maxim.bean.vo.UserEntity;
import com.max.maxim.service.UserService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

public class UserServiceImplTest extends MaxImApplicationTests {

  @Resource
  private UserService userService;

  @Test
  public void insertTest() {
    UserEntity user = UserEntity.builder().username("max").password("123456").build();
    userService.insertOne(user);
  }


}
