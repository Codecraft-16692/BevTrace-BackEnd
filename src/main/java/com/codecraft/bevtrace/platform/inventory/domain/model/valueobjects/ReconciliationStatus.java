package com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects;

/**
 * {@code IN_REVIEW}: counted units differ from the system and discrepancies must be resolved;
 * {@code RECONCILED}: confirmed.
 */
public enum ReconciliationStatus {
    IN_REVIEW, RECONCILED
}
