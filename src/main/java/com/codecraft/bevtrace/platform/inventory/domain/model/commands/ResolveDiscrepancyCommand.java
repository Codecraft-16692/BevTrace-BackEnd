package com.codecraft.bevtrace.platform.inventory.domain.model.commands;

/**
 * Resolves a discrepancy and aligns the batch stock with the count.
 *
 * @param discrepancyId discrepancy
 * @param note          explanation
 */
public record ResolveDiscrepancyCommand(Long discrepancyId, String note) {
}
