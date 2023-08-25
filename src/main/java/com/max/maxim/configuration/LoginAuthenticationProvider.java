package com.max.maxim.configuration;

import com.max.maxim.user.CustomUserDetailsService;
import jakarta.annotation.Resource;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.ObjectUtils;

@Slf4j
public class LoginAuthenticationProvider implements AuthenticationProvider {

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
    String password = userDetails.getPassword();
    password = encryptor.encrypt(password);
    List<GrantedAuthority> authorities = (List<GrantedAuthority>) userDetails.getAuthorities();
    return new UsernamePasswordAuthenticationToken(username, password, authorities);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return true;
  }
}
