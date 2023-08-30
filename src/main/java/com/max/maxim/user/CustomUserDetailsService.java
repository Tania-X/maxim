package com.max.maxim.user;

import com.max.maxim.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

  @Resource
  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userService.loadUserByUsername(username);
  }

  /**
   * todo
   * @param user the user to modify the password for
   * @param newPassword the password to change to, encoded by the configured
   * {@code PasswordEncoder}
   * @return the updated UserDetails with the new password
   */
  @Override
  public UserDetails updatePassword(UserDetails user, String newPassword) {
    return null;
  }
}
