package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DisconnectionPeriod;
import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.DataStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link DisconnectionPeriod}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "disconnection_periods")
public class DisconnectionPeriodPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long deviceId;
    private Instant startedAt;
    private Instant endedAt;
    @Enumerated(EnumType.STRING)
    private DataStatus dataStatus;

    public static DisconnectionPeriodPersistenceEntity fromDomain(DisconnectionPeriod d) {
        var e = new DisconnectionPeriodPersistenceEntity();
        e.setId(d.getId());
        e.deviceId = d.getDeviceId();
        e.startedAt = d.getStartedAt();
        e.endedAt = d.getEndedAt();
        e.dataStatus = d.getDataStatus();
        return e;
    }

    public DisconnectionPeriod toDomain() {
        return DisconnectionPeriod.restore(getId(), deviceId, startedAt, endedAt, dataStatus);
    }
}
