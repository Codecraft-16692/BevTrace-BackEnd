package com.codecraft.bevtrace.platform.iam.interfaces.rest.resources;

import java.time.Instant;

/**
 * JSON representation of a user (field names are the ones the Angular frontend consumes).
 */
public record UserResource(Long id, Long roleId, String name, String email, String phone, boolean active, Instant createdAt) {
}
