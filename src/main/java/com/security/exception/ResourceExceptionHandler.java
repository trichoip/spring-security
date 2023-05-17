package com.security.exception;

import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

@RestControllerAdvice
@SuppressWarnings("unused")
public class ResourceExceptionHandler { // implements ProblemHandling
  // @ExceptionHandler
  // public ResponseEntity<Problem> handleAuthenticationException(AuthenticationException e, NativeWebRequest request) {
  //   ThrowableProblem problem = Problem
  //     .builder()
  //     .with("timestamp1", Instant.now())
  //     .with("error1", Status.UNAUTHORIZED.getReasonPhrase())
  //     .withStatus(Status.UNAUTHORIZED)
  //     .withDetail(e.getMessage())
  //     .build();
  //   return create(problem, request);
  // }
}
