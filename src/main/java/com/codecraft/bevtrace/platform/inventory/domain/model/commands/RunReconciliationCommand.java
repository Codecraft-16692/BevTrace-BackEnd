package com.codecraft.bevtrace.platform.inventory.domain.model.commands;

import java.util.List;

/**
 * Compares physical counts with the system stock.
 *
 * @param userId user running the count
 * @param counts counted batches
 */
public record RunReconciliationCommand(Long userId, List<BatchCount> counts) {

    /**
     * Physical count of one batch.
     *
     * @param batchId    counted batch
     * @param countedQty units found
     */
    public record BatchCount(Long batchId, int countedQty) {
    }
}
