package com.codecraft.bevtrace.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body of the sign-in.
 */
public record SignInResource(@NotBlank String email, @NotBlank String password) {
}
