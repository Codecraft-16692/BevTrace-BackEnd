package com.codecraft.bevtrace.platform.dispatch.domain.model.commands;

/**
 * Assigns a vehicle to a dispatch (SCHEDULED to AUTHORIZED).
 *
 * @param dispatchOrderId order
 * @param vehicleId       vehicle
 */
public record AssignVehicleCommand(Long dispatchOrderId, Long vehicleId) {
}
