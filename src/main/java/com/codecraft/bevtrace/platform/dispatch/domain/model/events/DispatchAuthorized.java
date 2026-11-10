package com.codecraft.bevtrace.platform.dispatch.domain.model.events;

/**
 * A vehicle was assigned to a dispatch. The warehouse operators must validate the pallets.
 *
 * @param dispatchOrderId   order
 * @param destinationName   client that will receive the cargo
 */
public record DispatchAuthorized(Long dispatchOrderId, String destinationName) {
}
