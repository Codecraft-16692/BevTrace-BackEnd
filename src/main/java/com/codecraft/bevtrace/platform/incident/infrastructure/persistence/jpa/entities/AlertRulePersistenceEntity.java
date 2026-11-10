package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.AlertRule;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.ConditionType;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.Severity;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

/**
 * Stored form of {@link AlertRule}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "alert_rules")
public class AlertRulePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Enumerated(EnumType.STRING)
    private ConditionType conditionType;
    private double threshold;
    @Enumerated(EnumType.STRING)
    private Severity severity;
    private String description;
    private boolean active;

    public static AlertRulePersistenceEntity fromDomain(AlertRule d) {
        var e = new AlertRulePersistenceEntity();
        e.setId(d.getId());
        e.conditionType = d.getConditionType();
        e.threshold = d.getThreshold();
        e.severity = d.getSeverity();
        e.description = d.getDescription();
        e.active = d.getActive();
        return e;
    }

    public AlertRule toDomain() {
        return AlertRule.restore(getId(), conditionType, threshold, severity, description, active);
    }
}
