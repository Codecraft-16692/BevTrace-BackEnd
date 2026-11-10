package com.codecraft.bevtrace.platform.incident.domain.model.aggregates;

import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.ConditionType;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.IncidentStatus;
import com.codecraft.bevtrace.platform.incident.domain.model.valueobjects.Severity;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Duration;
import java.time.Instant;

/**
 * An anomaly detected on a shipment. It owns its life cycle.
 */
public class IncidentRecord extends AbstractDomainAggregateRoot<IncidentRecord> {

    /** A resolved incident is reopened, instead of duplicated, if the anomaly returns within this window. */
    public static final Duration REOPEN_WINDOW = Duration.ofHours(24);

    private Long id;
    private Long logId;
    private Long ruleId;
    private ConditionType anomalyType;
    private Severity severity;
    private IncidentStatus status;
    private Instant detectedAt;
    private String description;
    private Long acknowledgedBy;
    private Instant acknowledgedAt;
    private Instant resolvedAt;
    private int reopenCount;

    protected IncidentRecord() {
    }

    /**
     * Opens an incident.
     *
     * @param logId       shipment where it happened
     * @param rule        rule that was violated
     * @param description what was observed
     * @return an open incident
     */
    public static IncidentRecord detect(Long logId, AlertRule rule, String description) {
        var incident = new IncidentRecord();
        incident.logId = logId;
        incident.ruleId = rule.getId();
        incident.anomalyType = rule.getConditionType();
        incident.severity = rule.getSeverity();
        incident.status = IncidentStatus.OPEN;
        incident.detectedAt = Instant.now();
        incident.description = description;
        incident.reopenCount = 0;
        return incident;
    }

    /**
     * Takes responsibility for an open incident.
     *
     * @param userId acknowledging user
     */
    public void acknowledge(Long userId) {
        if (status != IncidentStatus.OPEN) {
            throw new IllegalStateException("incident.errors.not-open");
        }
        status = IncidentStatus.ACKNOWLEDGED;
        acknowledgedBy = userId;
        acknowledgedAt = Instant.now();
    }

    /**
     * Ensures corrective actions can still be added.
     */
    public void requireUnresolved() {
        if (status == IncidentStatus.RESOLVED) {
            throw new IllegalStateException("incident.errors.already-resolved");
        }
    }

    /**
     * Resolves the incident; it needs at least one corrective action.
     *
     * @param actionCount number of corrective actions registered
     */
    public void resolve(long actionCount) {
        requireUnresolved();
        if (actionCount == 0) {
            throw new IllegalStateException("incident.errors.action-required");
        }
        status = IncidentStatus.RESOLVED;
        resolvedAt = Instant.now();
    }

    /**
     * @param now reference time
     * @return true when it was resolved less than {@link #REOPEN_WINDOW} ago
     */
    public boolean resolvedRecently(Instant now) {
        return status == IncidentStatus.RESOLVED && resolvedAt != null
                && Duration.between(resolvedAt, now).compareTo(REOPEN_WINDOW) < 0;
    }

    /**
     * Reopens a resolved incident because the anomaly happened again.
     */
    public void reopen() {
        status = IncidentStatus.OPEN;
        resolvedAt = null;
        acknowledgedBy = null;
        acknowledgedAt = null;
        reopenCount++;
    }

    public Long getLogId() {
        return logId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public IncidentStatus getStatus() {
        return status;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public Long getId() {
        return id;
    }

    public ConditionType getAnomalyType() {
        return anomalyType;
    }

    public Severity getSeverity() {
        return severity;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public String getDescription() {
        return description;
    }

    public Long getAcknowledgedBy() {
        return acknowledgedBy;
    }

    public Instant getAcknowledgedAt() {
        return acknowledgedAt;
    }

    public int getReopenCount() {
        return reopenCount;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static IncidentRecord restore(Long id, Long logId, Long ruleId, ConditionType anomalyType, Severity severity, IncidentStatus status, Instant detectedAt, String description, Long acknowledgedBy, Instant acknowledgedAt, Instant resolvedAt, int reopenCount) {
        var a = new IncidentRecord();
        a.id = id;
        a.logId = logId;
        a.ruleId = ruleId;
        a.anomalyType = anomalyType;
        a.severity = severity;
        a.status = status;
        a.detectedAt = detectedAt;
        a.description = description;
        a.acknowledgedBy = acknowledgedBy;
        a.acknowledgedAt = acknowledgedAt;
        a.resolvedAt = resolvedAt;
        a.reopenCount = reopenCount;
        return a;
    }
}
