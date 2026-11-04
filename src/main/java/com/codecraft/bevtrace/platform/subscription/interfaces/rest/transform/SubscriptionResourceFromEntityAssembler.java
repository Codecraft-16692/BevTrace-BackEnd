package com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.Subscription;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.SubscriptionResource;

/**
 * Builds the resource of a subscription.
 */
public final class SubscriptionResourceFromEntityAssembler {

    private SubscriptionResourceFromEntityAssembler() {
    }

    public static SubscriptionResource toResourceFromEntity(Subscription e) {
        return new SubscriptionResource(e.getId(), e.getUserId(), e.getCompanyName(), e.getPlanCode(), e.getBillingCycle(), e.getStatus(), e.getCurrentPeriodStart(), e.getCurrentPeriodEnd(), e.getCancelledAt());
    }
}
