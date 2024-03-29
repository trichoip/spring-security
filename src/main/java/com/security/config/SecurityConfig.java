package com.security.config;

import com.security.security.jwt.JwtAccessDeniedHandler;
import com.security.security.jwt.JwtAuthEntryPoint;
import com.security.security.jwt.JwtFilter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilter jwtFilter;
  private final JwtAuthEntryPoint unauthorizedHandler;
  private final JwtAccessDeniedHandler accessDeniedHandler;
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
      .exceptionHandling()
      .accessDeniedHandler(accessDeniedHandler)
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


  // @Bean
  public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
    return http
            .cors(CustomCorsConfiguration::new)
            .cors(cors ->
                cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("*"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setAllowCredentials(true);
                    config.addAllowedOriginPattern("*");
                    return config;
                })
            )
            .csrf(csrf -> csrf.disable())
            .csrf(AbstractHttpConfigurer::disable)
            // .addFilter(corsConfig.corsFilter())
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler))
            .exceptionHandling(handling -> handling.accessDeniedHandler(accessDeniedHandler))
            .exceptionHandling(handling -> {
              handling.authenticationEntryPoint(unauthorizedHandler);
              handling.accessDeniedHandler(accessDeniedHandler);
              // ================================================================
              handling.authenticationEntryPoint(unauthorizedHandler).accessDeniedHandler(accessDeniedHandler);
            }) 
            .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandler).accessDeniedHandler(accessDeniedHandler))
            .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> {
              auth.requestMatchers(AUTH_WHITELIST).permitAll();
              auth.requestMatchers("/api/hello", "/api/authenticate", "/api/refresh").permitAll();
              auth.requestMatchers("/api/test").hasAuthority("ADMIN");
              auth.anyRequest().authenticated();

               // ================================================================

              auth.requestMatchers(AUTH_WHITELIST)
                  .permitAll()
                  .requestMatchers("/api/hello", "/api/authenticate", "/api/refresh")
                  .permitAll()
                  .requestMatchers("/api/test")
                  .hasAuthority("ADMIN")
                  .anyRequest()
                  .authenticated();

            })  
            .authorizeHttpRequests(auth ->
              auth.requestMatchers(AUTH_WHITELIST)
                  .permitAll()
                  .requestMatchers("/api/hello", "/api/authenticate", "/api/refresh")
                  .permitAll()
                  .requestMatchers("/api/test")
                  .hasAuthority("ADMIN")
                  .anyRequest()
                  .authenticated()
            )
            // .and()
            // .formLogin()
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

  // neu dung AuthenticationManager thi bat len (cach 2 trong api /authenticate)
  // @Bean
  // public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
  //   return authenticationConfiguration.getAuthenticationManager();
  // }
}
