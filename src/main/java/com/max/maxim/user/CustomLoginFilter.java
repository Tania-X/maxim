package com.max.maxim.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.max.maxim.constant.MaximConstant;
import com.max.maxim.exception.UserLoginException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public CustomLoginFilter(AuthenticationManager authenticationManager,
      CustomAuthenticationHandler authenticationHandler) {
    super(authenticationManager);
    setAuthenticationFailureHandler(authenticationHandler);
    setAuthenticationSuccessHandler(authenticationHandler);
    //login path
    setFilterProcessesUrl("/app/login");
  }

  private static boolean isContentTypeJson(HttpServletRequest request) {
    String contentType = request.getContentType();
    if (!ObjectUtils.isEmpty(contentType)) {
      return MaximConstant.APPLICATION_JSON_CHARSET_UTF_8.contains(contentType);
    }
    log.error("content-type in request invalid.");
    throw new UserLoginException("content-type in request invalid.");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    if (!HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
      throw new AuthenticationServiceException(
          "Authentication method not supported: " + request.getMethod());
    }
    String username = null;
    String password = null;
    String verifyCode = null;
    if (isContentTypeJson(request)) {
      try {
        Map<String, String> map = OBJECT_MAPPER.readValue(request.getInputStream(),
            new TypeReference<>() {
            });
        username = map.get(getUsernameParameter());
        password = map.get(getPasswordParameter());
        verifyCode = map.get(MaximConstant.CAPTCHA_VERIFY_CODE);
      } catch (IOException e) {
        log.error("error occurs while reading username&password from request: {}.", request, e);
      }
    } else {
      username = obtainUsername(request);
      password = obtainPassword(request);
      verifyCode = request.getParameter(MaximConstant.CAPTCHA_VERIFY_CODE);
    }
    // verify the verifyCode
    HttpSession session = request.getSession();
    String verifyCodeOrig = (String) session.getAttribute(MaximConstant.CAPTCHA_VERIFY_CODE);
    if (ObjectUtils.isEmpty(verifyCodeOrig)) {
      log.error("verify code in memory not exist.");
      throw new BadCredentialsException("verify code in memory not exist.");
    } else if (ObjectUtils.isEmpty(verifyCode)) {
      log.error("user input verify code is null.");
      throw new BadCredentialsException("user input verify code is null.");
    } else if (!ObjectUtils.nullSafeEquals(verifyCodeOrig, verifyCode)) {
      log.warn("verify code not match. user input[{}], memory exists[{}].", verifyCode,
          verifyCodeOrig);
      throw new BadCredentialsException("verify code not match.");
    }
    session.removeAttribute(MaximConstant.CAPTCHA_VERIFY_CODE);
    username = (username != null) ? username.trim() : "";
    password = (password != null) ? password : "";
    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
        username, password);
    // Allow subclasses to set the "details" property
    setDetails(request, authRequest);

    return this.getAuthenticationManager().authenticate(authRequest);
  }
}
