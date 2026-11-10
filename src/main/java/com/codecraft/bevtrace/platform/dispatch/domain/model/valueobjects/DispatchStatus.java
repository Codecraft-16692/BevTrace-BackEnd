package com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects;

/**
 * Life cycle of a dispatch order:
 * {@code SCHEDULED -> AUTHORIZED -> IN_TRANSIT -> DELIVERED}, or {@code CANCELLED} before departure.
 */
public enum DispatchStatus {
    SCHEDULED, AUTHORIZED, IN_TRANSIT, DELIVERED, CANCELLED
}
