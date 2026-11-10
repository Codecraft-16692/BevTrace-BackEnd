package com.codecraft.bevtrace.platform.subscription.domain.model.commands;

/**
 * Sends a contact request from the public form.
 *
 * @param fullName sender
 * @param email    reply address
 * @param company  optional company
 * @param message  text
 */
public record SubmitContactRequestCommand(String fullName, String email, String company, String message) {
}
