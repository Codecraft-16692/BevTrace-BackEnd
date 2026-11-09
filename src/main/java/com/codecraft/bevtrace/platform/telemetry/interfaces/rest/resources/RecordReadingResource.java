package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

/**
 * Request body of a device reading (sent by the IoT gateway).
 */
public record RecordReadingResource(@NotNull Long deviceId,
                                    @DecimalMin("-90") @DecimalMax("90") double latitude,
                                    @DecimalMin("-180") @DecimalMax("180") double longitude,
                                    @DecimalMin("0") double speed, double temperature, Instant timestamp) {
}
