package com.max.maxim.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.max.maxim.constant.MaximConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public CustomLoginFilter(AuthenticationManager authenticationManager,
      CustomAuthenticationHandler authenticationHandler) throws Exception {
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
    // todo
    throw new RuntimeException();
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
    if (isContentTypeJson(request)) {
      try {
        Map<String, String> map = OBJECT_MAPPER.readValue(request.getInputStream(),
            new TypeReference<>() {
            });
        username = map.get(getUsernameParameter());
        password = map.get(getPasswordParameter());
      } catch (IOException e) {
        log.error("error occurs while reading username&password from request: {}.", request, e);
      }
    } else {
      username = obtainUsername(request);
      password = obtainPassword(request);
    }
    username = (username != null) ? username.trim() : "";
    password = (password != null) ? password : "";
    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(
        username, password);
    // Allow subclasses to set the "details" property
    setDetails(request, authRequest);

    return this.getAuthenticationManager().authenticate(authRequest);
  }
}
