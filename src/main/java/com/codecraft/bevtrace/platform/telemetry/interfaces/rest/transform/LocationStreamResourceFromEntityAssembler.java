package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.telemetry.domain.model.aggregates.LocationStream;
import com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources.LocationStreamResource;

/**
 * Builds the resource of a location stream.
 */
public final class LocationStreamResourceFromEntityAssembler {

    private LocationStreamResourceFromEntityAssembler() {
    }

    public static LocationStreamResource toResourceFromEntity(LocationStream e) {
        return new LocationStreamResource(e.getId(), e.getDeviceId(), e.getLatitude(), e.getLongitude(), e.getSpeed(), e.getTemperature(), e.getTimestamp());
    }
}
