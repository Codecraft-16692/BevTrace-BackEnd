package com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body of the plan checkout. Card 4000000000000002 is always declined.
 */
public record SubscribeResource(@NotBlank String companyName, @NotBlank String planCode, @NotNull BillingPeriod billingCycle,
                                @NotBlank String cardHolder, @NotBlank String cardNumber) {
}
