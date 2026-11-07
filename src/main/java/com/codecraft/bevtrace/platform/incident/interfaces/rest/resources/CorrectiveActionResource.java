package com.codecraft.bevtrace.platform.incident.interfaces.rest.resources;

import java.time.Instant;

/**
 * JSON representation of a corrective action (field names are the ones the Angular frontend consumes).
 */
public record CorrectiveActionResource(Long id, Long incidentId, Long userId, String description, Instant appliedAt) {
}
