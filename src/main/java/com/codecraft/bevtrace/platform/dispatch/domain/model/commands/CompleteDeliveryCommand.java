package com.codecraft.bevtrace.platform.dispatch.domain.model.commands;

import java.time.Instant;

/**
 * Closes a dispatch once the client received the cargo.
 *
 * @param dispatchOrderId order
 * @param deliveredAt     hand-over time
 */
public record CompleteDeliveryCommand(Long dispatchOrderId, Instant deliveredAt) {
}
