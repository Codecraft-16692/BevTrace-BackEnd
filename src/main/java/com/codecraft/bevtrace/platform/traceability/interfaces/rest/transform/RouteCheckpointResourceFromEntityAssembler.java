package com.codecraft.bevtrace.platform.traceability.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.RouteCheckpoint;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.RouteCheckpointResource;

/**
 * Builds the resource of a route checkpoint.
 */
public final class RouteCheckpointResourceFromEntityAssembler {

    private RouteCheckpointResourceFromEntityAssembler() {
    }

    public static RouteCheckpointResource toResourceFromEntity(RouteCheckpoint e) {
        return new RouteCheckpointResource(e.getId(), e.getLogId(), e.getSequence(), e.getLocationName(), e.getLatitude(), e.getLongitude(), e.getStatus(), e.getReachedAt(), e.getObservation());
    }
}
