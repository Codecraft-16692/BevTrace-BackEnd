package com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.SubscriptionStatus;
import java.time.Instant;

/**
 * JSON representation of a subscription (field names are the ones the Angular frontend consumes).
 */
public record SubscriptionResource(Long id, Long userId, String companyName, String planCode, BillingPeriod billingCycle, SubscriptionStatus status, Instant currentPeriodStart, Instant currentPeriodEnd, Instant cancelledAt) {
}
