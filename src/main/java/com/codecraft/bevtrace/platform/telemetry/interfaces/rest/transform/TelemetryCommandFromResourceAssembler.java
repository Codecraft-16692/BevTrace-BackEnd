package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.telemetry.domain.model.commands.ProvisionDeviceCommand;
import com.codecraft.bevtrace.platform.telemetry.domain.model.commands.RecordReadingCommand;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.ProvisionDeviceResource;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.RecordReadingResource;

/**
 * Builds the commands of the telemetry context from the request bodies.
 */
public final class TelemetryCommandFromResourceAssembler {

    private TelemetryCommandFromResourceAssembler() {
    }

    public static ProvisionDeviceCommand toCommandFromResource(ProvisionDeviceResource resource) {
        return new ProvisionDeviceCommand(resource.vehicleId(), resource.modelId(), resource.deviceCode());
    }

    public static RecordReadingCommand toCommandFromResource(RecordReadingResource resource) {
        return new RecordReadingCommand(resource.deviceId(), resource.latitude(), resource.longitude(),
                resource.speed(), resource.temperature(), resource.timestamp());
    }
}
