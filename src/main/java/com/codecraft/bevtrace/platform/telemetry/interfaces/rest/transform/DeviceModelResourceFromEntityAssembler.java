package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.DeviceModel;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.DeviceModelResource;

/**
 * Builds the resource of a device model.
 */
public final class DeviceModelResourceFromEntityAssembler {

    private DeviceModelResourceFromEntityAssembler() {
    }

    public static DeviceModelResource toResourceFromEntity(DeviceModel e) {
        return new DeviceModelResource(e.getId(), e.getName(), e.getManufacturer(), e.getSensors());
    }
}
