package com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body of the contact form.
 */
public record SubmitContactRequestResource(@NotBlank String fullName, @NotBlank String email, String company,
                                           @NotBlank String message) {
}
