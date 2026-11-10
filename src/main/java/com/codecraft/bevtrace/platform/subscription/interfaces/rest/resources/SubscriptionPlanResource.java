package com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;
import java.util.List;

/**
 * JSON representation of a subscription plan (field names are the ones the Angular frontend consumes).
 */
public record SubscriptionPlanResource(Long id, String code, String name, String descriptionKey, double priceAmount, String currency, BillingPeriod billingPeriod, int maxRoutes, int maxDevices, int maxUsers, List<String> featureKeys, boolean highlighted, boolean custom, boolean active) {
}
