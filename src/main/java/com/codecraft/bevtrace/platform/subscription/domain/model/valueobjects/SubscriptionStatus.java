package com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects;

/**
 * State of a subscription. {@code PENDING} is a failed first payment.
 */
public enum SubscriptionStatus {
    PENDING, ACTIVE, PAST_DUE, CANCELLED
}
