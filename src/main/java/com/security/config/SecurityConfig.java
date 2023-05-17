package com.security.config;

import com.security.security.jwt.JwtAuthEntryPoint;
import com.security.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final JwtAuthEntryPoint unauthorizedHandler;
  // private final CorsConfig corsConfig;

  private static final String[] AUTH_WHITELIST = {
    "/swagger-resources/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/v3/api-docs/**",
    "/webjars/**",
    "/oauth2/**",
    "/",
  };

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http
      .cors(CustomCorsConfiguration::new)
      .csrf()
      .disable()
      // .addFilter(corsConfig.corsFilter())
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .exceptionHandling()
      .authenticationEntryPoint(unauthorizedHandler)
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      .and()
      // .authenticationProvider(authenticationProvider())
      .authorizeHttpRequests()
      .requestMatchers(AUTH_WHITELIST)
      .permitAll()
      .requestMatchers("/api/hello", "/api/authenticate", "/api/refresh")
      .permitAll()
      .requestMatchers("/api/test")
      .hasAuthority("ADMIN")
      .anyRequest()
      .authenticated()
      // .and()
      // .formLogin()
      .and()
      .build();
  }
  // ban dau chua jwt , chua co login db
  // @Bean
  // public UserDetailsService userDetailsService(PasswordEncoder encoder) {
  //   UserDetails admin = User.withUsername("admin").password(encoder.encode("1")).roles("ADMIN").build();
  //   UserDetails user = User.withUsername("user").password(encoder.encode("1")).roles("USER").build();
  //   return new InMemoryUserDetailsManager(admin, user);
  // }

  // neu bat userDetailsService() thi phai bat authenticationProvider() moi chay duoc
  // @Bean
  // public UserDetailsService userDetailsService() {
  //   return new UserDetailsServiceImpl();
  // }

  // @Bean
  // public AuthenticationProvider authenticationProvider() {
  //   DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
  //   daoAuthenticationProvider.setUserDetailsService(userDetailsService());
  //   daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
  //   return daoAuthenticationProvider;
  // }

  // neu dung AuthenticationManager thi bat len
  // @Bean
  // public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
  //   return authenticationConfiguration.getAuthenticationManager();
  // }
}
