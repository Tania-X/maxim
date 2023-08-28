package com.max.maxim.user;

import com.max.maxim.constant.MaximConstant;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    // store username in session
    HttpSession session = request.getSession();
    session.setAttribute(MaximConstant.SESSION_USERNAME, authentication.getPrincipal());
    // feedback to frontend
    response.setCharacterEncoding("utf-8");
    response.setContentType("application/json; charset=utf-8");
    response.getWriter().write("{\"status\": \"ok\", \"code\": \"200\"}");
  }
}
