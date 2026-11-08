package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DispatchOrder;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.DispatchOrderResource;

/**
 * Builds the resource of a dispatch order.
 */
public final class DispatchOrderResourceFromEntityAssembler {

    private DispatchOrderResourceFromEntityAssembler() {
    }

    public static DispatchOrderResource toResourceFromEntity(DispatchOrder e) {
        return new DispatchOrderResource(e.getId(), e.getManagerId(), e.getVehicleId(), e.getDestinationId(), e.getScheduledDate(), e.getStatus(), e.getPriority(), e.getEstimatedWeight(), e.getRequestedQuantity(), e.getDeliveredQuantity(), e.getDeliveredAt(), e.getDepartedAt(), e.getCargoValidated(), e.getCreatedAt());
    }
}
