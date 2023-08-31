package com.max.maxim.util;

import com.max.maxim.constant.MaximConstant;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.awt.Font;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CaptchaUtil {

  public static void createImageCode(HttpServletRequest request, HttpServletResponse response) {
    response.setContentType("image/gif");
    // since HTTP/1.1 Pragma is deprecated as Cache-Control can do the same thing
    response.setHeader("Pragma", "no-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);

    SpecCaptcha specCaptcha = new SpecCaptcha(128, 50, 4);
    specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 35));
    specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

    String verifyCode = specCaptcha.text();
    // temporarily only consider saving the verifyCode in the app memory
    HttpSession session = request.getSession();
    Object verifyCodeAttribute = session.getAttribute(MaximConstant.CAPTCHA_VERIFY_CODE);
    if (!ObjectUtils.isEmpty(verifyCodeAttribute)) {
      session.removeAttribute(MaximConstant.CAPTCHA_VERIFY_CODE);
    }
    session.setAttribute(MaximConstant.CAPTCHA_VERIFY_CODE, verifyCode);

    try {
      specCaptcha.out(response.getOutputStream());
    } catch (Exception e) {
      log.error("fail to create image code.", e);
    }
    log.info("success in creating image code.");
  }

}
