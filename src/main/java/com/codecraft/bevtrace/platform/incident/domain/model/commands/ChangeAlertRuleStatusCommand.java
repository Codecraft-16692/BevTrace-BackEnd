package com.codecraft.bevtrace.platform.incident.domain.model.commands;

/**
 * Enables or disables an alert rule.
 */
public record ChangeAlertRuleStatusCommand(Long ruleId, boolean active) {
}
