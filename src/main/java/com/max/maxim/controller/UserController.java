package com.max.maxim.controller;

import com.max.maxim.bean.vo.DummyEntity;
import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.bean.vo.UserEntity;
import com.max.maxim.service.UserService;
import com.max.maxim.util.ResultUtil;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

  @Resource
  private UserService userService;

  @PostMapping("insert")
  public ResultEntity<DummyEntity> insert(UserEntity user) {
    userService.insertOne(user);
    return ResultUtil.success();
  }

}
