package com.max.maxim.controller;

import com.max.maxim.bean.dto.UserUpdateDto;
import com.max.maxim.bean.vo.DummyEntity;
import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.bean.vo.UserEntity;
import com.max.maxim.enums.ResultEnum;
import com.max.maxim.service.UserService;
import com.max.maxim.util.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {

  @Resource
  private UserService userService;

  @PostMapping("insert")
  public ResultEntity<DummyEntity> insert(UserEntity user) {
    userService.insertOne(user);
    return ResultUtil.success();
  }

  @PostMapping("update")
  public ResultEntity<DummyEntity> update(HttpServletRequest request,
      @RequestBody UserUpdateDto user) {
    HttpSession session = request.getSession();
    if (ObjectUtils.isEmpty(session.getAttribute("username"))) {
      log.warn("session null.");
      return ResultUtil.error(ResultEnum.SESSION_NOT_EXISTS);
    }
    ResultEntity<DummyEntity> updateResult = userService.update(user);
    if (!Objects.equals(ResultEnum.SUCCESS.getCode(), updateResult.getCode())) {
      return updateResult;
    }
    return ResultUtil.success();
  }

}
