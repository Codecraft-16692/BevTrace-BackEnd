package com.codecraft.bevtrace.platform.incident.domain.model.commands;

/**
 * Marks a notification as read.
 */
public record MarkNotificationReadCommand(Long notificationId) {
}
