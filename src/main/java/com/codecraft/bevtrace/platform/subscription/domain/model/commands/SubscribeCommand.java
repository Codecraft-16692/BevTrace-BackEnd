package com.codecraft.bevtrace.platform.subscription.domain.model.commands;

import com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects.BillingPeriod;

/**
 * Buys a plan with a card (simulated gateway).
 *
 * @param userId      buyer
 * @param companyName company the plan is for
 * @param planCode    plan code
 * @param billingCycle monthly or yearly
 * @param cardHolder  name on the card
 * @param cardNumber  card number
 */
public record SubscribeCommand(Long userId, String companyName, String planCode, BillingPeriod billingCycle,
                               String cardHolder, String cardNumber) {
}
