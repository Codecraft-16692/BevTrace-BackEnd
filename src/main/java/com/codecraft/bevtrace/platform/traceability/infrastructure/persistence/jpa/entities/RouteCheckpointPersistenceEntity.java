package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.RouteCheckpoint;
import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.CheckpointStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link RouteCheckpoint}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "route_checkpoints")
public class RouteCheckpointPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long logId;
    @Column(name = "step_order")
    private int sequence;
    private String locationName;
    private double latitude;
    private double longitude;
    @Enumerated(EnumType.STRING)
    private CheckpointStatus status;
    private Instant reachedAt;
    private String observation;

    public static RouteCheckpointPersistenceEntity fromDomain(RouteCheckpoint d) {
        var e = new RouteCheckpointPersistenceEntity();
        e.setId(d.getId());
        e.logId = d.getLogId();
        e.sequence = d.getSequence();
        e.locationName = d.getLocationName();
        e.latitude = d.getLatitude();
        e.longitude = d.getLongitude();
        e.status = d.getStatus();
        e.reachedAt = d.getReachedAt();
        e.observation = d.getObservation();
        return e;
    }

    public RouteCheckpoint toDomain() {
        return RouteCheckpoint.restore(getId(), logId, sequence, locationName, latitude, longitude, status, reachedAt, observation);
    }
}
