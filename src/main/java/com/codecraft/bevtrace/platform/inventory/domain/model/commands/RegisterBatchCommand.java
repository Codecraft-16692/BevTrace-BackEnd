package com.codecraft.bevtrace.platform.inventory.domain.model.commands;

import java.time.LocalDate;

/**
 * Registers a batch announced by the plant.
 *
 * @param productId      product of the batch
 * @param zoneId         destination zone
 * @param batchNumber    label code
 * @param initialQty     announced units
 * @param expirationDate expiration date
 */
public record RegisterBatchCommand(Long productId, Long zoneId, String batchNumber, int initialQty,
                                   LocalDate expirationDate) {
}
