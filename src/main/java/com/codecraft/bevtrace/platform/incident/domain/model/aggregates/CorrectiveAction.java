package com.codecraft.bevtrace.platform.incident.domain.model.aggregates;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * What was done to fix an incident.
 */
public class CorrectiveAction extends AbstractDomainAggregateRoot<CorrectiveAction> {

    private Long id;
    private Long incidentId;
    private Long userId;
    private String description;
    private Instant appliedAt;

    protected CorrectiveAction() {
    }

    /**
     * Registers an action.
     *
     * @param incidentId  incident being fixed
     * @param userId      user that acted
     * @param description what was done, at least 10 characters
     * @return the action
     */
    public static CorrectiveAction apply(Long incidentId, Long userId, String description) {
        if (description == null || description.trim().length() < 10) {
            throw new IllegalStateException("incident.errors.description-short");
        }
        var action = new CorrectiveAction();
        action.incidentId = incidentId;
        action.userId = userId;
        action.description = description.trim();
        action.appliedAt = Instant.now();
        return action;
    }

    public Long getId() {
        return id;
    }

    public Long getIncidentId() {
        return incidentId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public Instant getAppliedAt() {
        return appliedAt;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static CorrectiveAction restore(Long id, Long incidentId, Long userId, String description, Instant appliedAt) {
        var a = new CorrectiveAction();
        a.id = id;
        a.incidentId = incidentId;
        a.userId = userId;
        a.description = description;
        a.appliedAt = appliedAt;
        return a;
    }
}
