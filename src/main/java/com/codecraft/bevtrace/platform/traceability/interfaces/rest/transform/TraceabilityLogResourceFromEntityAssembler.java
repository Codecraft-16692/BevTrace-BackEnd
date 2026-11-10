package com.codecraft.bevtrace.platform.traceability.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.TraceabilityLog;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.TraceabilityLogResource;

/**
 * Builds the resource of a traceability log.
 */
public final class TraceabilityLogResourceFromEntityAssembler {

    private TraceabilityLogResourceFromEntityAssembler() {
    }

    public static TraceabilityLogResource toResourceFromEntity(TraceabilityLog e) {
        return new TraceabilityLogResource(e.getId(), e.getDispatchOrderId(), e.getCurrentStatus(), e.getStartTime(), e.getEndTime(), e.getEstimatedArrival(), e.getCurrentLatitude(), e.getCurrentLongitude(), e.getRejectionReason(), e.getOrderPriority(), e.getDestinationName(), e.getVehiclePlate(), e.getDeviceId());
    }
}
