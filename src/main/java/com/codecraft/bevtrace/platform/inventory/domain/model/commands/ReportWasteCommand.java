package com.codecraft.bevtrace.platform.inventory.domain.model.commands;

/**
 * Reports units lost to damage, expiration or handling.
 *
 * @param userId   reporting user
 * @param batchId  affected batch
 * @param quantity lost units
 * @param reason   explanation
 */
public record ReportWasteCommand(Long userId, Long batchId, int quantity, String reason) {
}
