package com.security.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.security.security.jwt.JwtFilter;
import com.security.security.jwt.JwtTokenProvider;
import com.security.web.rest.vm.LoginVM2;
import com.security.web.rest.vm.TokenVM;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiTestResource {

  private final ModelMapper modelMapper;
  private final JwtTokenProvider jwtTokenProvider;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  // private final AuthenticationManager authenticationManager;

  @SecurityRequirement(name = "token_auth")
  @GetMapping("/test")
  public ResponseEntity<?> test() {
    return ResponseEntity.ok("Hello World");
  }

  @SecurityRequirement(name = "token_auth")
  @PreAuthorize("hasAuthority('USER')")
  @GetMapping("/test2")
  public ResponseEntity<?> test2() {
    return ResponseEntity.ok("Hello World 2");
  }

  @SecurityRequirement(name = "token_auth")
  @GetMapping("/hello")
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("hello is exception");
  }

  @PostMapping("/authenticate")
  public ResponseEntity<?> authorize(@Valid @RequestBody LoginVM2 loginVM) {
    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtTokenProvider.createToken(authentication);
    String Refreshjwt = jwtTokenProvider.generateRefreshToken(authentication.getName());
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
    return new ResponseEntity<>(new JWTToken(jwt, Refreshjwt), httpHeaders, HttpStatus.OK);
    //
    //  cach 2
    // Authentication authenticate = authenticationManager.authenticate(
    //   new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword())
    // );
    // return ResponseEntity.ok(jwtTokenProvider.generateToken(loginVM.getUsername()));
  }

  @GetMapping("/refresh")
  public ResponseEntity<TokenVM> getAccessTokenFromRefreshToken(@RequestHeader("Refresh-Token") String refreshToken) {
    if (StringUtils.hasText(refreshToken) && jwtTokenProvider.validateRefreshToken(refreshToken)) {
      return ResponseEntity.ok(new TokenVM(jwtTokenProvider.generateAccessToken(refreshToken), refreshToken));
    } else {
      throw new BadCredentialsException("Invalid or expired refresh token");
    }
  }

  @Data
  @AllArgsConstructor
  static class JWTToken {

    private String idToken;
    private String refreshToken;
  }
}
