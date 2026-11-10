package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.DiscrepancyStatus;
import java.time.Instant;

/**
 * JSON representation of a inventory discrepancy (field names are the ones the Angular frontend consumes).
 */
public record InventoryDiscrepancyResource(Long id, Long reconciliationId, Long batchId, int expectedQty, int countedQty, DiscrepancyStatus status, Instant detectedAt, Instant resolvedAt, String resolutionNote) {
}
