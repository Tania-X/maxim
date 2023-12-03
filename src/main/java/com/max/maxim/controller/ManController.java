package com.max.maxim.controller;

import com.max.maxim.bean.vo.DummyEntity;
import com.max.maxim.bean.vo.ResultEntity;
import com.max.maxim.enums.ResultEnum;
import com.max.maxim.util.CaptchaUtil;
import com.max.maxim.util.ResultUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("man")
@Slf4j
public class ManController {

  @Resource
  private StringEncryptor encryptor;

  @GetMapping("isLogin")
  public ResultEntity<DummyEntity> isLogin(HttpServletRequest request) {
    HttpSession session = request.getSession();
    if (ObjectUtils.isEmpty(session.getAttribute("username"))) {
      log.warn("The user doesn't log in this app.");
      return ResultUtil.error(ResultEnum.USER_NOT_LOGIN);
    } else {
      return ResultUtil.success();
    }
  }

  @GetMapping("imageCode")
  public void imageCode(HttpServletRequest request, HttpServletResponse response) {
    CaptchaUtil.createImageCode(request, response);
  }

  @GetMapping("encrypt")
  public ResultEntity<String> encrypt(@RequestParam String input) {
    if (ObjectUtils.isEmpty(input)) {
      log.warn("input for encryption is empty or null.");
      return ResultUtil.error(ResultEnum.INPUT_NULL_OR_EMPTY);
    }
    return ResultUtil.success(encryptor.encrypt(input));
  }

  // curl -G --data-urlencode input=eap7SvvM8LpOVfCkRQrzoLe1ortj1EV3W/9Vtd7pFImVfmCN+O7OpA3HWedTqKUY http://localhost:11451/man/decrypt
  // attention: special characters like '+' should be converted according to ASCII: '+' -> '%2B'
  @GetMapping("decrypt")
  public ResultEntity<String> decrypt(@RequestParam String input) {
    if (ObjectUtils.isEmpty(input)) {
      log.warn("input for decryption is empty or null.");
      return ResultUtil.error(ResultEnum.INPUT_NULL_OR_EMPTY);
    }
    String decrypted;
    try {
      decrypted = encryptor.decrypt(input);
    } catch (Exception e) {
      log.error("encryption error", e);
      throw new EncryptionOperationNotPossibleException(e);
    }
    return ResultUtil.success(decrypted);
  }

}
