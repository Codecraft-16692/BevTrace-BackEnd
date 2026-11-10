package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.ProductBatch;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ProductBatchResource;

/**
 * Builds the resource of a product batch.
 */
public final class ProductBatchResourceFromEntityAssembler {

    private ProductBatchResourceFromEntityAssembler() {
    }

    public static ProductBatchResource toResourceFromEntity(ProductBatch e) {
        return new ProductBatchResource(e.getId(), e.getProductId(), e.getZoneId(), e.getBatchNumber(), e.getInitialQty(), e.getCurrentQty(), e.getStatus(), e.getReceivedAt(), e.getExpirationDate());
    }
}
