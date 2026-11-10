package com.codecraft.bevtrace.platform.traceability.domain.model.commands;

import java.time.Instant;

/**
 * Opens the traceability log of a dispatch that just left the warehouse.
 *
 * @param dispatchOrderId      dispatch order
 * @param priority             priority of the order
 * @param vehicleId            vehicle that carries the cargo
 * @param vehiclePlate         plate of the vehicle
 * @param destinationName      destination
 * @param destinationLatitude  destination latitude
 * @param destinationLongitude destination longitude
 * @param departedAt           departure time
 */
public record StartRouteCommand(Long dispatchOrderId, String priority, Long vehicleId, String vehiclePlate,
                                String destinationName, double destinationLatitude, double destinationLongitude,
                                Instant departedAt) {
}
