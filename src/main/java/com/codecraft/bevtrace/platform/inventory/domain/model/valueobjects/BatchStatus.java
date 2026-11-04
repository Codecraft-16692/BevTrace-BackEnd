package com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects;

/**
 * Life cycle of a batch: announced by the plant ({@code EXPECTED}), scanned at the dock
 * ({@code AVAILABLE}) and finally exhausted ({@code DEPLETED}).
 */
public enum BatchStatus {
    EXPECTED, AVAILABLE, DEPLETED
}
