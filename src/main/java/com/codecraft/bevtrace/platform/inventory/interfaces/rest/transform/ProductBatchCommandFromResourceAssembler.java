package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.commands.ReceiveBatchCommand;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.RegisterBatchCommand;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.ReceiveBatchResource;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.RegisterBatchResource;

/**
 * Builds the batch commands from the request bodies.
 */
public final class ProductBatchCommandFromResourceAssembler {

    private ProductBatchCommandFromResourceAssembler() {
    }

    public static RegisterBatchCommand toCommandFromResource(RegisterBatchResource resource) {
        return new RegisterBatchCommand(resource.productId(), resource.zoneId(), resource.batchNumber(),
                resource.initialQty(), resource.expirationDate());
    }

    public static ReceiveBatchCommand toCommandFromResource(ReceiveBatchResource resource) {
        return new ReceiveBatchCommand(resource.scanCode());
    }
}
