package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request body that runs a reconciliation.
 */
public record RunReconciliationResource(@NotEmpty List<@Valid CountLine> counts) {

    /**
     * Count of one batch.
     */
    public record CountLine(@NotNull Long batchId, @Min(0) int countedQty) {
    }
}
