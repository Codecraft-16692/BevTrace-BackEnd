package com.codecraft.bevtrace.platform.incident.domain.model.commands;

/**
 * Acknowledges an open incident.
 */
public record AcknowledgeIncidentCommand(Long incidentId, Long userId) {
}
