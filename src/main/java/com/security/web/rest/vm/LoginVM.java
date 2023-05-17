package com.security.web.rest.vm;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginVM(
  @Schema(example = "trichoip") @NotBlank(message = "Username is required") String username,
  @Schema(example = "1") @NotBlank(message = "Password is required") String password
) {}
