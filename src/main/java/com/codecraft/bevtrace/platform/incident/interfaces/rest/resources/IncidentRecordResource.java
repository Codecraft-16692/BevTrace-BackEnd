package com.codecraft.bevtrace.platform.incident.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.ConditionType;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.IncidentStatus;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.Severity;
import java.time.Instant;

/**
 * JSON representation of a incident record (field names are the ones the Angular frontend consumes).
 */
public record IncidentRecordResource(Long id, Long logId, Long ruleId, ConditionType anomalyType, Severity severity, IncidentStatus status, Instant detectedAt, String description, Long acknowledgedBy, Instant acknowledgedAt, Instant resolvedAt, int reopenCount) {
}
