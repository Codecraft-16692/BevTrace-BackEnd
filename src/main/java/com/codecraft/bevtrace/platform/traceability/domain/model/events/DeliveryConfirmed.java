package com.codecraft.bevtrace.platform.traceability.domain.model.events;

import java.time.Instant;

/**
 * The client received the cargo. Dispatch closes the order and managers are notified.
 *
 * @param dispatchOrderId order that was delivered
 * @param destinationName client name
 * @param deliveredAt     hand-over time
 */
public record DeliveryConfirmed(Long dispatchOrderId, String destinationName, Instant deliveredAt) {
}
