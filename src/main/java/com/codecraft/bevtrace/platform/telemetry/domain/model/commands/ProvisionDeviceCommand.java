package com.codecraft.bevtrace.platform.telemetry.domain.model.commands;

/**
 * Installs a device in a vehicle.
 *
 * @param vehicleId  vehicle (one device per vehicle)
 * @param modelId    device model
 * @param deviceCode unique code printed on the device (stored upper-case)
 */
public record ProvisionDeviceCommand(Long vehicleId, Long modelId, String deviceCode) {
}
