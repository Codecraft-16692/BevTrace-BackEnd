package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.CargoAssignment;
import com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources.CargoAssignmentResource;

/**
 * Builds the resource of a cargo assignment.
 */
public final class CargoAssignmentResourceFromEntityAssembler {

    private CargoAssignmentResourceFromEntityAssembler() {
    }

    public static CargoAssignmentResource toResourceFromEntity(CargoAssignment e) {
        return new CargoAssignmentResource(e.getId(), e.getDispatchOrderId(), e.getBatchId(), e.getQuantity(), e.getTotalWeight(), e.getPalletCount());
    }
}
