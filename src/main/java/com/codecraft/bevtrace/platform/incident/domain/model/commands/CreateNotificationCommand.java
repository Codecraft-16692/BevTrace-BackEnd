package com.codecraft.bevtrace.platform.incident.domain.model.commands;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.NotificationType;

/**
 * Creates a notification for every user of a role.
 */
public record CreateNotificationCommand(String recipientRole, NotificationType type, String title, String message) {
}
