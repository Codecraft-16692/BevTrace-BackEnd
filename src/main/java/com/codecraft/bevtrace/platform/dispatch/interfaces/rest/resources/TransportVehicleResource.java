package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources;

/**
 * JSON representation of a transport vehicle (field names are the ones the Angular frontend consumes).
 */
public record TransportVehicleResource(Long id, Long driverId, String plateNumber, double maxCapacity, boolean isAvailable) {
}
