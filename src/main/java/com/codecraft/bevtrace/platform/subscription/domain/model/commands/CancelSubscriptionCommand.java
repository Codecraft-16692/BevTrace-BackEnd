package com.codecraft.bevtrace.platform.subscription.domain.model.commands;

/**
 * Cancels a subscription.
 *
 * @param actorUserId    caller
 * @param actorIsAdmin   whether the caller is an administrator (may cancel any subscription)
 * @param subscriptionId subscription to cancel
 */
public record CancelSubscriptionCommand(Long actorUserId, boolean actorIsAdmin, Long subscriptionId) {
}
