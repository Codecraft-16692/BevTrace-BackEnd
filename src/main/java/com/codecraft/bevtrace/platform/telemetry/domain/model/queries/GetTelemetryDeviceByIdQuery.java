package com.codecraft.bevtrace.platform.telemetry.domain.model.queries;

/**
 * Reads one telemetry device.
 *
 * @param id identifier
 */
public record GetTelemetryDeviceByIdQuery(Long id) {
}
