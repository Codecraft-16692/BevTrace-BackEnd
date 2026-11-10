package com.codecraft.bevtrace.platform.incident.domain.model.commands;

/**
 * Registers a corrective action on an unresolved incident.
 */
public record RegisterCorrectiveActionCommand(Long incidentId, Long userId, String description) {
}
