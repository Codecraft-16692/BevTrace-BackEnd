package com.codecraft.bevtrace.platform.incident.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Request body that enables or disables an alert rule.
 */
public record UpdateAlertRuleStatusResource(@NotNull Boolean active) {
}
