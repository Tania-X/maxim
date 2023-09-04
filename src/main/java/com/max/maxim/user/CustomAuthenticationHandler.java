package com.max.maxim.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.max.maxim.constant.MaximConstant;
import com.max.maxim.enums.ResultEnum;
import com.max.maxim.util.ResultDetailUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

@Slf4j
public class CustomAuthenticationHandler implements AuthenticationSuccessHandler,
    AuthenticationFailureHandler, SessionInformationExpiredStrategy, LogoutSuccessHandler,
    AccessDeniedHandler, AuthenticationEntryPoint {


  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  /**
   * @param request       that resulted in an <code>AuthenticationException</code>
   * @param response      so that the user agent can begin authentication
   * @param authException that caused the invocation
   * @throws IOException error in json writer
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    String detailMessage =
        authException.getClass().getSimpleName() + " " + authException.getLocalizedMessage();
    if (authException instanceof InsufficientAuthenticationException) {
      detailMessage = "please visit after signing up";
    }
    log.warn(detailMessage);
    response.setContentType(MaximConstant.APPLICATION_JSON_CHARSET_UTF_8);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().println(OBJECT_MAPPER.writeValueAsString(
        ResultDetailUtil.of(detailMessage, "authorization exception", ResultEnum.ERROR.getCode())));
  }

  /**
   * @param request               that resulted in an <code>AccessDeniedException</code>
   * @param response              so that the user agent can be advised of the failure
   * @param accessDeniedException that caused the invocation
   * @throws IOException error in json writer
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    String detailMessage = null;
    if (accessDeniedException instanceof MissingCsrfTokenException) {
      detailMessage = "in lack of CSRF TOKEN, please fetch it from form or header";
    } else if (accessDeniedException instanceof InvalidCsrfTokenException) {
      detailMessage = "invalid CSRF TOKEN";
    } else if (accessDeniedException instanceof CsrfException) {
      detailMessage = accessDeniedException.getLocalizedMessage();
    } else if (accessDeniedException instanceof AuthorizationServiceException) {
      detailMessage = AuthorizationServiceException.class.getSimpleName() + " "
          + accessDeniedException.getLocalizedMessage();
    }
    log.warn(detailMessage);
    response.setContentType(MaximConstant.APPLICATION_JSON_CHARSET_UTF_8);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.getWriter()
        .println(OBJECT_MAPPER.writeValueAsString(
            ResultDetailUtil.of(detailMessage, "forbidden", ResultEnum.ERROR.getCode())));

  }

  /**
   * @param request   the request during which the authentication attempt occurred.
   * @param response  the response.
   * @param exception the exception which was thrown to reject the authentication request.
   * @throws IOException error in json writer
   */
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    response.setContentType(MaximConstant.APPLICATION_JSON_CHARSET_UTF_8);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().println(OBJECT_MAPPER.writeValueAsString(
        ResultDetailUtil.of(exception.getLocalizedMessage(), "authentication failure",
            ResultEnum.ERROR.getCode())));
  }

  /**
   * @param request        the request which caused the successful authentication
   * @param response       the response
   * @param authentication the <tt>Authentication</tt> object which was created during the
   *                       authentication process.
   * @throws IOException error in json writer
   */
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    // store username in session
    HttpSession session = request.getSession();
    session.setAttribute(MaximConstant.SESSION_USERNAME, authentication.getPrincipal());
    response.setContentType(MaximConstant.APPLICATION_JSON_CHARSET_UTF_8);
    response.setStatus(HttpStatus.OK.value());
    // todo
    // SecurityContext在设置Authentication的时候并不会自动写入Session，读的时候却会根据Session判断，所以需要手动写入一次，否则下一次刷新时SecurityContext是新创建的实例。
    request.getSession()
        .setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
            SecurityContextHolder.getContext());
    response.getWriter().println(OBJECT_MAPPER.writeValueAsString(
        ResultDetailUtil.of(authentication.toString(), "authentication success",
            ResultEnum.SUCCESS.getCode())));
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    response.setContentType(MaximConstant.APPLICATION_JSON_CHARSET_UTF_8);
    response.setStatus(HttpStatus.OK.value());
    response.getWriter().println(OBJECT_MAPPER.writeValueAsString(
        ResultDetailUtil.of(authentication.toString(), "logout success",
            ResultEnum.SUCCESS.getCode())));
  }

  @Override
  public void onExpiredSessionDetected(SessionInformationExpiredEvent event)
      throws IOException, ServletException {
    String message = "The account has been logged in from another device,"
        + " if it is not your own operation, please change the password in time";
    final HttpServletResponse response = event.getResponse();
    response.setContentType(MaximConstant.APPLICATION_JSON_CHARSET_UTF_8);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().println(OBJECT_MAPPER.writeValueAsString(
        ResultDetailUtil.of(event.getSessionInformation().toString(), message,
            ResultEnum.ERROR.getCode())));
  }
}
