package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.Driver;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.DriverResource;

/**
 * Builds the resource of a driver.
 */
public final class DriverResourceFromEntityAssembler {

    private DriverResourceFromEntityAssembler() {
    }

    public static DriverResource toResourceFromEntity(Driver e) {
        return new DriverResource(e.getId(), e.getFullName(), e.getLicenseNumber(), e.getStatus());
    }
}
