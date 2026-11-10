package com.codecraft.bevtrace.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body of the sign-up.
 */
public record SignUpResource(@NotNull Long roleId, @NotBlank String name, @NotBlank String email, String phone,
                             @NotBlank String password) {
}
