package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.ReconciliationStatus;
import java.time.Instant;
import java.time.LocalDate;

/**
 * JSON representation of a inventory reconciliation (field names are the ones the Angular frontend consumes).
 */
public record InventoryReconciliationResource(Long id, LocalDate date, ReconciliationStatus status, int totalBatches, int matchedBatches, double eriPercentage, Long confirmedBy, Instant confirmedAt) {
}
