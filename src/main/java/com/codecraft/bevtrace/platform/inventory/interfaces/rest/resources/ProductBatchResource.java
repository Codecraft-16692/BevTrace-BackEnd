package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.BatchStatus;
import java.time.Instant;
import java.time.LocalDate;

/**
 * JSON representation of a product batch (field names are the ones the Angular frontend consumes).
 */
public record ProductBatchResource(Long id, Long productId, Long zoneId, String batchNumber, int initialQty, int currentQty, BatchStatus status, Instant receivedAt, LocalDate expirationDate) {
}
