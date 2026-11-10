package com.codecraft.bevtrace.platform.incident.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.AlertRule;
import com.codecraft.bevtrace.platform.incident.interfaces.rest.resources.AlertRuleResource;

/**
 * Builds the resource of a alert rule.
 */
public final class AlertRuleResourceFromEntityAssembler {

    private AlertRuleResourceFromEntityAssembler() {
    }

    public static AlertRuleResource toResourceFromEntity(AlertRule e) {
        return new AlertRuleResource(e.getId(), e.getConditionType(), e.getThreshold(), e.getSeverity(), e.getDescription(), e.getActive());
    }
}
