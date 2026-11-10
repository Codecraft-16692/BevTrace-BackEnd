package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryDiscrepancy;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.InventoryDiscrepancyResource;

/**
 * Builds the resource of a inventory discrepancy.
 */
public final class InventoryDiscrepancyResourceFromEntityAssembler {

    private InventoryDiscrepancyResourceFromEntityAssembler() {
    }

    public static InventoryDiscrepancyResource toResourceFromEntity(InventoryDiscrepancy e) {
        return new InventoryDiscrepancyResource(e.getId(), e.getReconciliationId(), e.getBatchId(), e.getExpectedQty(), e.getCountedQty(), e.getStatus(), e.getDetectedAt(), e.getResolvedAt(), e.getResolutionNote());
    }
}
