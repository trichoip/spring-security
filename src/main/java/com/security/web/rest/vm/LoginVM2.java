package com.security.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginVM2 {

  @Schema(example = "trichoip")
  @NotBlank(message = "Username is required")
  private String username;

  @Schema(example = "1")
  @NotBlank(message = "Password is required")
  private String password;
  // private boolean rememberMe;

}
