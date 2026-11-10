package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.IncidentRecord;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.ConditionType;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.IncidentStatus;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.Severity;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link IncidentRecord}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "incident_records")
public class IncidentRecordPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long logId;
    private Long ruleId;
    @Enumerated(EnumType.STRING)
    private ConditionType anomalyType;
    @Enumerated(EnumType.STRING)
    private Severity severity;
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;
    private Instant detectedAt;
    private String description;
    private Long acknowledgedBy;
    private Instant acknowledgedAt;
    private Instant resolvedAt;
    private int reopenCount;

    public static IncidentRecordPersistenceEntity fromDomain(IncidentRecord d) {
        var e = new IncidentRecordPersistenceEntity();
        e.setId(d.getId());
        e.logId = d.getLogId();
        e.ruleId = d.getRuleId();
        e.anomalyType = d.getAnomalyType();
        e.severity = d.getSeverity();
        e.status = d.getStatus();
        e.detectedAt = d.getDetectedAt();
        e.description = d.getDescription();
        e.acknowledgedBy = d.getAcknowledgedBy();
        e.acknowledgedAt = d.getAcknowledgedAt();
        e.resolvedAt = d.getResolvedAt();
        e.reopenCount = d.getReopenCount();
        return e;
    }

    public IncidentRecord toDomain() {
        return IncidentRecord.restore(getId(), logId, ruleId, anomalyType, severity, status, detectedAt, description, acknowledgedBy, acknowledgedAt, resolvedAt, reopenCount);
    }
}
