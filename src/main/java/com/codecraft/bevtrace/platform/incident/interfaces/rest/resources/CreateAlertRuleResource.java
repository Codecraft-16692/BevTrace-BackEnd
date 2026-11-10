package com.codecraft.bevtrace.platform.incident.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.ConditionType;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body that creates an alert rule.
 */
public record CreateAlertRuleResource(@NotNull ConditionType conditionType, @NotNull Double threshold,
                                      @NotNull Severity severity, @NotBlank String description) {
}
