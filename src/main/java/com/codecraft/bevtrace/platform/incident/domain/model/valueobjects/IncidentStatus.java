package com.codecraft.bevtrace.platform.incident.domain.model.valueobjects;

/**
 * Life cycle of an incident: {@code OPEN -> ACKNOWLEDGED -> RESOLVED}. A resolved incident can be reopened.
 */
public enum IncidentStatus {
    OPEN, ACKNOWLEDGED, RESOLVED
}
