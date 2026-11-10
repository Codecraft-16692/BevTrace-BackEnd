package com.codecraft.bevtrace.platform.telemetry.domain.model.events;

import java.time.Instant;

/**
 * A device reported its position. Traceability moves the shipment it follows.
 *
 * @param deviceId    emitting device
 * @param latitude    latitude
 * @param longitude   longitude
 * @param temperature cargo temperature in Celsius
 * @param timestamp   measurement time
 */
public record ReadingRecorded(Long deviceId, double latitude, double longitude, double temperature, Instant timestamp) {
}
