package com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources;

import java.time.Instant;

/**
 * JSON representation of a newsletter subscriber (field names are the ones the Angular frontend consumes).
 */
public record NewsletterSubscriberResource(Long id, String email, Instant subscribedAt) {
}
