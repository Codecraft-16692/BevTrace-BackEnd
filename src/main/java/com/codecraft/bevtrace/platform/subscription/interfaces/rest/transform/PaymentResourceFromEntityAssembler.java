package com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Payment;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.PaymentResource;

/**
 * Builds the resource of a payment.
 */
public final class PaymentResourceFromEntityAssembler {

    private PaymentResourceFromEntityAssembler() {
    }

    public static PaymentResource toResourceFromEntity(Payment e) {
        return new PaymentResource(e.getId(), e.getSubscriptionId(), e.getUserId(), e.getAmount(), e.getCurrency(), e.getStatus(), e.getCardLast4(), e.getPaidAt());
    }
}
