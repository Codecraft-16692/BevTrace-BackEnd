package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.ConnectionStatus;
import java.time.Instant;

/**
 * JSON representation of a telemetry device (field names are the ones the Angular frontend consumes).
 */
public record TelemetryDeviceResource(Long id, Long vehicleId, Long modelId, String deviceCode, Instant installedAt, boolean isActive, ConnectionStatus connectionStatus, Instant lastSignalAt) {
}
