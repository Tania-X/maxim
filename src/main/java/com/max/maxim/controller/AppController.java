package com.max.maxim.controller;

import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.util.ResultUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app")
public class AppController {

  @GetMapping("home")
  public ResultEntity<String> home(HttpServletRequest request) {
    HttpSession session = request.getSession();
    return ResultUtil.success("welcome back, " + session.getAttribute("principal_name"));
  }


}
