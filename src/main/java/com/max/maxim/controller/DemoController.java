package com.max.maxim.controller;

import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.util.ResultUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("demo")
public class DemoController {

  @GetMapping("hello")
  public ResultEntity<String> demo() {
    return ResultUtil.success("hello, max!");
  }

}
