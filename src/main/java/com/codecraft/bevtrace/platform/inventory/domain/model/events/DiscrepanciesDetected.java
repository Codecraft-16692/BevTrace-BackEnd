package com.codecraft.bevtrace.platform.inventory.domain.model.events;

/**
 * A reconciliation found stock differences. Managers are notified by the incident context.
 *
 * @param count number of batches whose count differs from the system
 */
public record DiscrepanciesDetected(int count) {
}
