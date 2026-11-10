package com.codecraft.bevtrace.platform.subscription.domain.model.commands;

/**
 * Subscribes an e-mail to the newsletter.
 *
 * @param email address
 */
public record SubscribeToNewsletterCommand(String email) {
}
