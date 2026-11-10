package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.TelemetryDevice;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.TelemetryDeviceResource;

/**
 * Builds the resource of a telemetry device.
 */
public final class TelemetryDeviceResourceFromEntityAssembler {

    private TelemetryDeviceResourceFromEntityAssembler() {
    }

    public static TelemetryDeviceResource toResourceFromEntity(TelemetryDevice e) {
        return new TelemetryDeviceResource(e.getId(), e.getVehicleId(), e.getModelId(), e.getDeviceCode(), e.getInstalledAt(), e.getIsActive(), e.getConnectionStatus(), e.getLastSignalAt());
    }
}
