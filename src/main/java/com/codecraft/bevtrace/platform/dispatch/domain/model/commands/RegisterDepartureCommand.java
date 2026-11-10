package com.codecraft.bevtrace.platform.dispatch.domain.model.commands;

/**
 * Registers the departure of an authorized dispatch.
 *
 * @param dispatchOrderId order
 */
public record RegisterDepartureCommand(Long dispatchOrderId) {
}
