package com.max.maxim.service.impl;

import com.max.maxim.MaxImApplicationTests;
import com.max.maxim.bean.condition.UserUpdateCondition;
import com.max.maxim.bean.dto.UserUpdateDto;
import com.max.maxim.bean.dto.UserUpdateDto.UserUpdateDtoBuilder;
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

  @Test
  public void updateTest() {
    UserUpdateDto condition = UserUpdateDto.builder().username("max").oldPassword("123456")
        .newPassword("654321").build();
    userService.update(condition);
  }


}
