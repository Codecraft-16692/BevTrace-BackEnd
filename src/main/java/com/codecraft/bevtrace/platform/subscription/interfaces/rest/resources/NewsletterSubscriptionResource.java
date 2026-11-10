package com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body of the newsletter form.
 */
public record NewsletterSubscriptionResource(@NotBlank String email) {
}
