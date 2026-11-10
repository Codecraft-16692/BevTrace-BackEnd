package com.codecraft.bevtrace.platform.incident.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.ConditionType;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.Severity;

/**
 * JSON representation of a alert rule (field names are the ones the Angular frontend consumes).
 */
public record AlertRuleResource(Long id, ConditionType conditionType, double threshold, Severity severity, String description, boolean active) {
}
