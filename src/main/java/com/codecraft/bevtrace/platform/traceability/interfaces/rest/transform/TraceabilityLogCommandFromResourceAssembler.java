package com.codecraft.bevtrace.platform.traceability.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.traceability.domain.model.commands.ConfirmDeliveryCommand;
import com.codecraft.bevtrace.platform.traceability.domain.model.commands.RejectDeliveryCommand;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.ConfirmDeliveryResource;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.RejectDeliveryResource;

/**
 * Builds the commands of the shipment log from the request bodies.
 */
public final class TraceabilityLogCommandFromResourceAssembler {

    private TraceabilityLogCommandFromResourceAssembler() {
    }

    public static ConfirmDeliveryCommand toCommandFromResource(Long logId, ConfirmDeliveryResource resource) {
        return new ConfirmDeliveryCommand(logId, resource.receivedBy());
    }

    public static RejectDeliveryCommand toCommandFromResource(Long logId, RejectDeliveryResource resource) {
        return new RejectDeliveryCommand(logId, resource.reason());
    }
}
