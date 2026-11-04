package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WasteRecord;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.WasteRecordResource;

/**
 * Builds the resource of a waste record.
 */
public final class WasteRecordResourceFromEntityAssembler {

    private WasteRecordResourceFromEntityAssembler() {
    }

    public static WasteRecordResource toResourceFromEntity(WasteRecord e) {
        return new WasteRecordResource(e.getId(), e.getBatchId(), e.getUserId(), e.getQuantity(), e.getReason(), e.getReportedDate());
    }
}
