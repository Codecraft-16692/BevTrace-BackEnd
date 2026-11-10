package com.codecraft.bevtrace.platform.dispatch.domain.model.commands;

/**
 * Cancels a pending dispatch.
 *
 * @param dispatchOrderId order
 */
public record CancelDispatchOrderCommand(Long dispatchOrderId) {
}
