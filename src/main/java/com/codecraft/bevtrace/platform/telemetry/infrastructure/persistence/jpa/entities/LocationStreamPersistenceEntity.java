package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.LocationStream;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link LocationStream}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "location_streams")
public class LocationStreamPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long deviceId;
    private double latitude;
    private double longitude;
    private double speed;
    private double temperature;
    @Column(name = "recorded_at")
    private Instant timestamp;

    public static LocationStreamPersistenceEntity fromDomain(LocationStream d) {
        var e = new LocationStreamPersistenceEntity();
        e.setId(d.getId());
        e.deviceId = d.getDeviceId();
        e.latitude = d.getLatitude();
        e.longitude = d.getLongitude();
        e.speed = d.getSpeed();
        e.temperature = d.getTemperature();
        e.timestamp = d.getTimestamp();
        return e;
    }

    public LocationStream toDomain() {
        return LocationStream.restore(getId(), deviceId, latitude, longitude, speed, temperature, timestamp);
    }
}
