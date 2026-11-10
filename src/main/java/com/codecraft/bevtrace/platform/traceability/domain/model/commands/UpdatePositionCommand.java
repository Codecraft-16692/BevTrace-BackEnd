package com.codecraft.bevtrace.platform.traceability.domain.model.commands;

/**
 * Moves the shipment followed by a device.
 *
 * @param deviceId  device that reported
 * @param latitude  new latitude
 * @param longitude new longitude
 */
public record UpdatePositionCommand(Long deviceId, double latitude, double longitude) {
}
