package com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates;

import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.ConnectionStatus;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Duration;
import java.time.Instant;

/**
 * An IoT tracker installed in a vehicle. It owns the connectivity rules.
 */
public class TelemetryDevice extends AbstractDomainAggregateRoot<TelemetryDevice> {

    /** Minutes without signal after which a disconnected device is a critical case. */
    public static final long CRITICAL_MINUTES = 30;

    private Long id;
    private Long vehicleId;
    private Long modelId;
    private String deviceCode;
    private Instant installedAt;
    private boolean isActive;
    private ConnectionStatus connectionStatus;
    private Instant lastSignalAt;

    protected TelemetryDevice() {
    }

    /**
     * Installs a device in a vehicle; it starts connected.
     *
     * @param vehicleId  vehicle
     * @param modelId    hardware model
     * @param deviceCode unique code (stored upper-case)
     * @return the new device
     */
    public static TelemetryDevice provision(Long vehicleId, Long modelId, String deviceCode) {
        var now = Instant.now();
        var device = new TelemetryDevice();
        device.vehicleId = vehicleId;
        device.modelId = modelId;
        device.deviceCode = deviceCode.trim().toUpperCase();
        device.installedAt = now;
        device.isActive = true;
        device.connectionStatus = ConnectionStatus.CONNECTED;
        device.lastSignalAt = now;
        return device;
    }

    /**
     * Registers a signal: the device is connected again.
     *
     * @param at time of the signal
     * @return true when the device was disconnected before (a disconnection period must be closed)
     */
    public boolean receiveSignal(Instant at) {
        boolean wasDisconnected = connectionStatus == ConnectionStatus.DISCONNECTED;
        connectionStatus = ConnectionStatus.CONNECTED;
        if (lastSignalAt == null || at.isAfter(lastSignalAt)) {
            lastSignalAt = at;
        }
        return wasDisconnected;
    }

    /**
     * Marks the device as disconnected.
     */
    public void markDisconnected() {
        connectionStatus = ConnectionStatus.DISCONNECTED;
    }

    /**
     * @param now reference time
     * @return whole minutes since the last signal
     */
    public long minutesWithoutSignal(Instant now) {
        return Math.max(0, Duration.between(lastSignalAt, now).toMinutes());
    }

    /**
     * @param now reference time
     * @return true when disconnected for more than {@link #CRITICAL_MINUTES}
     */
    public boolean isCritical(Instant now) {
        return connectionStatus == ConnectionStatus.DISCONNECTED && minutesWithoutSignal(now) > CRITICAL_MINUTES;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public boolean isActiveDevice() {
        return isActive;
    }

    public ConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public Instant getLastSignalAt() {
        return lastSignalAt;
    }

    public Long getId() {
        return id;
    }

    public Long getModelId() {
        return modelId;
    }

    public Instant getInstalledAt() {
        return installedAt;
    }

    public boolean getIsActive() {
        return isActive;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static TelemetryDevice restore(Long id, Long vehicleId, Long modelId, String deviceCode, Instant installedAt, boolean isActive, ConnectionStatus connectionStatus, Instant lastSignalAt) {
        var a = new TelemetryDevice();
        a.id = id;
        a.vehicleId = vehicleId;
        a.modelId = modelId;
        a.deviceCode = deviceCode;
        a.installedAt = installedAt;
        a.isActive = isActive;
        a.connectionStatus = connectionStatus;
        a.lastSignalAt = lastSignalAt;
        return a;
    }
}
