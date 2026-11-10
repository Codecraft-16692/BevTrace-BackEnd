package com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.PaymentStatus;
import java.time.Instant;

/**
 * JSON representation of a payment (field names are the ones the Angular frontend consumes).
 */
public record PaymentResource(Long id, Long subscriptionId, Long userId, double amount, String currency, PaymentStatus status, String cardLast4, Instant paidAt) {
}
