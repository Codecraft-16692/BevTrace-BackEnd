package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryReconciliation;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.InventoryReconciliationResource;

/**
 * Builds the resource of a inventory reconciliation.
 */
public final class InventoryReconciliationResourceFromEntityAssembler {

    private InventoryReconciliationResourceFromEntityAssembler() {
    }

    public static InventoryReconciliationResource toResourceFromEntity(InventoryReconciliation e) {
        return new InventoryReconciliationResource(e.getId(), e.getDate(), e.getStatus(), e.getTotalBatches(), e.getMatchedBatches(), e.getEriPercentage(), e.getConfirmedBy(), e.getConfirmedAt());
    }
}
