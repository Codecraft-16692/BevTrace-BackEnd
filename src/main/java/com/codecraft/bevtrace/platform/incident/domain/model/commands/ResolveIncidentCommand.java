package com.codecraft.bevtrace.platform.incident.domain.model.commands;

/**
 * Resolves an incident that has at least one corrective action.
 */
public record ResolveIncidentCommand(Long incidentId) {
}
