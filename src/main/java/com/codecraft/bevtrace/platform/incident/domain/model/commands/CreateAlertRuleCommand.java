package com.codecraft.bevtrace.platform.incident.domain.model.commands;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.ConditionType;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.Severity;

/**
 * Creates an alert rule.
 */
public record CreateAlertRuleCommand(ConditionType conditionType, double threshold, Severity severity, String description) {
}
