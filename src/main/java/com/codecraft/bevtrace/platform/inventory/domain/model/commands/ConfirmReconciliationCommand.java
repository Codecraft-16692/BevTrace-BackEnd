package com.codecraft.bevtrace.platform.inventory.domain.model.commands;

/**
 * Confirms a reconciliation once every discrepancy is resolved.
 *
 * @param reconciliationId reconciliation
 * @param userId           confirming manager
 */
public record ConfirmReconciliationCommand(Long reconciliationId, Long userId) {
}
