package com.codecraft.bevtrace.platform.incident.domain.model.aggregates;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.ConditionType;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.Severity;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

/**
 * A threshold that, when exceeded on an active route, raises an incident.
 */
public class AlertRule extends AbstractDomainAggregateRoot<AlertRule> {

    private Long id;
    private ConditionType conditionType;
    private double threshold;
    private Severity severity;
    private String description;
    private boolean active;

    protected AlertRule() {
    }

    /**
     * Creates an active rule.
     *
     * @param conditionType watched condition
     * @param threshold     limit, positive (degrees or minutes depending on the condition)
     * @param severity      severity of the incidents it raises
     * @param description   explanation shown to users
     * @return the rule
     */
    public static AlertRule create(ConditionType conditionType, double threshold, Severity severity, String description) {
        if (!(threshold > 0)) {
            throw new IllegalStateException("incident.errors.threshold-invalid");
        }
        var rule = new AlertRule();
        rule.conditionType = conditionType;
        rule.threshold = threshold;
        rule.severity = severity;
        rule.description = description.trim();
        rule.active = true;
        return rule;
    }

    /**
     * Enables or disables the rule.
     *
     * @param value new state
     */
    public void setActive(boolean value) {
        this.active = value;
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public double getThreshold() {
        return threshold;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean getActive() {
        return active;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static AlertRule restore(Long id, ConditionType conditionType, double threshold, Severity severity, String description, boolean active) {
        var a = new AlertRule();
        a.id = id;
        a.conditionType = conditionType;
        a.threshold = threshold;
        a.severity = severity;
        a.description = description;
        a.active = active;
        return a;
    }
}
