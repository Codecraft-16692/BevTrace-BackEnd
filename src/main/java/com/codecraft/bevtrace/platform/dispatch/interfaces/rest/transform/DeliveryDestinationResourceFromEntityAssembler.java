package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DeliveryDestination;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.DeliveryDestinationResource;

/**
 * Builds the resource of a delivery destination.
 */
public final class DeliveryDestinationResourceFromEntityAssembler {

    private DeliveryDestinationResourceFromEntityAssembler() {
    }

    public static DeliveryDestinationResource toResourceFromEntity(DeliveryDestination e) {
        return new DeliveryDestinationResource(e.getId(), e.getClientName(), e.getAddress(), e.getLatitude(), e.getLongitude(), e.getRegion());
    }
}
