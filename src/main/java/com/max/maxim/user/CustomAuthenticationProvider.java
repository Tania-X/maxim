package com.max.maxim.user;

import jakarta.annotation.Resource;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

  @Resource
  private CustomUserDetailsService userDetailsService;

  @Resource
  private StringEncryptor encryptor;

  @Override
  @SuppressWarnings("unchecked")
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (ObjectUtils.isEmpty(userDetails)) {
      throw new UsernameNotFoundException("user not found.");
    }
    String userPassword = (String) authentication.getCredentials();
    if (ObjectUtils.isEmpty(userPassword)) {
      log.warn("your input password is empty.");
      throw new AuthenticationCredentialsNotFoundException("password empty.");
    }
    String dbPassword = userDetails.getPassword();
    dbPassword = encryptor.decrypt(dbPassword);
    if (!ObjectUtils.nullSafeEquals(userPassword, dbPassword)) {
      log.warn("password not correct for your username.");
      throw new AuthenticationCredentialsNotFoundException("password wrong.");
    }
    log.info("authentication succeeds, entering home...");
    Set<GrantedAuthority> authorities = (Set<GrantedAuthority>) userDetails.getAuthorities();
    return new UsernamePasswordAuthenticationToken(username, userPassword, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
