package com.codecraft.bevtrace.platform.subscription.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.subscription.domain.model.aggregates.SubscriptionPlan;
import com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources.SubscriptionPlanResource;

/**
 * Builds the resource of a subscription plan.
 */
public final class SubscriptionPlanResourceFromEntityAssembler {

    private SubscriptionPlanResourceFromEntityAssembler() {
    }

    public static SubscriptionPlanResource toResourceFromEntity(SubscriptionPlan e) {
        return new SubscriptionPlanResource(e.getId(), e.getCode(), e.getName(), e.getDescriptionKey(), e.getPriceAmount(), e.getCurrency(), e.getBillingPeriod(), e.getMaxRoutes(), e.getMaxDevices(), e.getMaxUsers(), e.getFeatureKeys(), e.getHighlighted(), e.isCustom(), e.getActive());
    }
}
