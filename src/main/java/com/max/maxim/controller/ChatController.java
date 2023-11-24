package com.max.maxim.controller;

import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.core.entity.SimpleChatEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("chat")
public class ChatController {

  @PostMapping("sendAndReceive")
  public ResultEntity<SimpleChatEntity> sendAndReceive(@RequestBody SimpleChatEntity dialogue) {
    return null;
  }


}
