package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * Request body that provisions a device in a vehicle.
 */
public record ProvisionDeviceResource(@NotNull Long vehicleId, @NotNull Long modelId,
                                      @NotBlank @Pattern(regexp = "^[A-Za-z0-9-]{4,20}$") String deviceCode) {
}
