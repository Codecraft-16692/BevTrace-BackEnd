package com.codecraft.bevtrace.platform.incident.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.NotificationType;
import java.time.Instant;

/**
 * JSON representation of a notification (field names are the ones the Angular frontend consumes).
 */
public record NotificationResource(Long id, String recipientRole, NotificationType type, String title, String message, Instant createdAt, boolean read) {
}
