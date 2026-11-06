package com.codecraft.bevtrace.platform.incident.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.incident.domain.model.aggregates.CorrectiveAction;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link CorrectiveAction}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "corrective_actions")
public class CorrectiveActionPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long incidentId;
    private Long userId;
    private String description;
    private Instant appliedAt;

    public static CorrectiveActionPersistenceEntity fromDomain(CorrectiveAction d) {
        var e = new CorrectiveActionPersistenceEntity();
        e.setId(d.getId());
        e.incidentId = d.getIncidentId();
        e.userId = d.getUserId();
        e.description = d.getDescription();
        e.appliedAt = d.getAppliedAt();
        return e;
    }

    public CorrectiveAction toDomain() {
        return CorrectiveAction.restore(getId(), incidentId, userId, description, appliedAt);
    }
}
