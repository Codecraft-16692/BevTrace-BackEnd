package com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects;

/**
 * State of a shipment being followed. {@code DELIVERY_REJECTED} keeps the shipment open so it can still be
 * delivered or returned.
 */
public enum LogStatus {
    IN_TRANSIT, COMPLETED, DELIVERY_REJECTED
}
