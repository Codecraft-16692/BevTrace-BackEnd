package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources;

import java.time.Instant;

/**
 * JSON representation of a location stream (field names are the ones the Angular frontend consumes).
 */
public record LocationStreamResource(Long id, Long deviceId, double latitude, double longitude, double speed, double temperature, Instant timestamp) {
}
