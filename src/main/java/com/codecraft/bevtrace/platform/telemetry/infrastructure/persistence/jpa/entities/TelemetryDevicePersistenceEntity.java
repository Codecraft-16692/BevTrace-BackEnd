package com.codecraft.bevtrace.platform.telemetry.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.TelemetryDevice;
import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.ConnectionStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link TelemetryDevice}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "telemetry_devices")
public class TelemetryDevicePersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long vehicleId;
    private Long modelId;
    private String deviceCode;
    private Instant installedAt;
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private ConnectionStatus connectionStatus;
    private Instant lastSignalAt;

    public static TelemetryDevicePersistenceEntity fromDomain(TelemetryDevice d) {
        var e = new TelemetryDevicePersistenceEntity();
        e.setId(d.getId());
        e.vehicleId = d.getVehicleId();
        e.modelId = d.getModelId();
        e.deviceCode = d.getDeviceCode();
        e.installedAt = d.getInstalledAt();
        e.isActive = d.getIsActive();
        e.connectionStatus = d.getConnectionStatus();
        e.lastSignalAt = d.getLastSignalAt();
        return e;
    }

    public TelemetryDevice toDomain() {
        return TelemetryDevice.restore(getId(), vehicleId, modelId, deviceCode, installedAt, isActive, connectionStatus, lastSignalAt);
    }
}
