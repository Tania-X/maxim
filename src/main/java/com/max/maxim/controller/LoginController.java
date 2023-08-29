package com.max.maxim.controller;

import com.max.maxim.bean.vo.DummyEntity;
import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.enums.ResultEnum;
import com.max.maxim.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("lgn")
@Slf4j
public class LoginController {

  @GetMapping("isLogin")
  public ResultEntity<DummyEntity> isLogin(HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (ObjectUtils.isEmpty(session.getAttribute("username"))) {
      log.warn("The user doesn't log in this app.");
      return ResultUtil.error(ResultEnum.SESSION_NOT_EXISTS);
    } else {
      return ResultUtil.success();
    }
  }

}
