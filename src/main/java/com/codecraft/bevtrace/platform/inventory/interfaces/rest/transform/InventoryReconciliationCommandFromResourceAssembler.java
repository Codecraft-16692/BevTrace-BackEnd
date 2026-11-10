package com.codecraft.bevtrace.platform.inventory.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.inventory.domain.model.commands.ConfirmReconciliationCommand;
import com.codecraft.bevtrace.platform.inventory.domain.model.commands.RunReconciliationCommand;
import com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources.RunReconciliationResource;

/**
 * Builds the reconciliation commands from the request.
 */
public final class InventoryReconciliationCommandFromResourceAssembler {

    private InventoryReconciliationCommandFromResourceAssembler() {
    }

    public static RunReconciliationCommand toCommandFromResource(Long userId, RunReconciliationResource resource) {
        return new RunReconciliationCommand(userId, resource.counts().stream()
                .map(line -> new RunReconciliationCommand.BatchCount(line.batchId(), line.countedQty())).toList());
    }

    public static ConfirmReconciliationCommand toCommandFromResource(Long reconciliationId, Long userId) {
        return new ConfirmReconciliationCommand(reconciliationId, userId);
    }
}
