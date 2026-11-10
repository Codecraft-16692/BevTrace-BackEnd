package com.codecraft.bevtrace.platform.subscription.interfaces.rest.resources;

import java.time.Instant;

/**
 * JSON representation of a contact request (field names are the ones the Angular frontend consumes).
 */
public record ContactRequestResource(Long id, String fullName, String email, String company, String message, Instant createdAt) {
}
