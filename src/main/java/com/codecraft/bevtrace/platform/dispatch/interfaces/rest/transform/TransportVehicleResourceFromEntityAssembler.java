package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.TransportVehicle;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.TransportVehicleResource;

/**
 * Builds the resource of a transport vehicle.
 */
public final class TransportVehicleResourceFromEntityAssembler {

    private TransportVehicleResourceFromEntityAssembler() {
    }

    public static TransportVehicleResource toResourceFromEntity(TransportVehicle e) {
        return new TransportVehicleResource(e.getId(), e.getDriverId(), e.getPlateNumber(), e.getMaxCapacity(), e.getIsAvailable());
    }
}
