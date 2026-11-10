package com.codecraft.bevtrace.platform.traceability.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.DeliveryRecord;
import com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources.DeliveryRecordResource;

/**
 * Builds the resource of a delivery record.
 */
public final class DeliveryRecordResourceFromEntityAssembler {

    private DeliveryRecordResourceFromEntityAssembler() {
    }

    public static DeliveryRecordResource toResourceFromEntity(DeliveryRecord e) {
        return new DeliveryRecordResource(e.getId(), e.getLogId(), e.getReceivedBy(), e.getSignatureUrl(), e.getDeliveredAt(), e.getStatus(), e.getRejectionReason());
    }
}
