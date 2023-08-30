package com.max.maxim.configuration;

import com.max.maxim.user.CustomAuthenticationHandler;
import com.max.maxim.user.CustomAuthenticationProvider;
import com.max.maxim.user.CustomLoginFilter;
import com.max.maxim.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

  /*
     @Bean
     public UserDetailsService userDetailsService() {
     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
     manager.createUser(

     There is a very interesting lambda expression in the class User:
     private Function<String, String> passwordEncoder = (password) -> password;
     In the later build method, default encoder is applied to this expression, and we can learn
     from it, although it's never recommended to be employed in production.

          User.withDefaultPasswordEncoder().username("max").password("max123").roles("USER").build());
      return manager;
    }
   */

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration)
      throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new CustomAuthenticationProvider();
  }

  @Bean
  public CustomAuthenticationHandler customAuthenticationHandler() {
    return new CustomAuthenticationHandler();
  }

  @Bean
  public CustomLoginFilter customLoginFilter(AuthenticationManager authenticationManager,
      CustomAuthenticationHandler customAuthenticationHandler) throws Exception {
    return new CustomLoginFilter(authenticationManager, customAuthenticationHandler);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, CustomLoginFilter loginFilter,
      CustomAuthenticationHandler customAuthenticationHandler) throws Exception {

    http.authorizeHttpRequests(
            authorize -> authorize.requestMatchers(HttpMethod.GET, "/lgn/**").permitAll())
        .authorizeHttpRequests(
            authorize -> authorize.requestMatchers(HttpMethod.POST, "/login").permitAll())
        .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated());
    http.addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class);

    http.csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

}
