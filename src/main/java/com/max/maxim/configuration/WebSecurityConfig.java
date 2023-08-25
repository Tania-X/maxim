package com.max.maxim.configuration;

import static org.springframework.security.config.Customizer.withDefaults;

import com.max.maxim.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

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
  public UserDetailsService userDetailsService() {
    return new CustomUserDetailsService();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new LoginAuthenticationProvider();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
        .authorizeHttpRequests(authorize -> authorize
            .anyRequest().authenticated()
        )
        .formLogin(withDefaults())
        .httpBasic(withDefaults());
    return http.build();
  }

}
