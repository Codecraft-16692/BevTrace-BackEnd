package com.codecraft.bevtrace.platform.telemetry.domain.model.commands;

import java.time.Instant;

/**
 * Ingests a reading sent by a device.
 *
 * @param deviceId    device
 * @param latitude    latitude
 * @param longitude   longitude
 * @param speed       speed in km/h
 * @param temperature cargo temperature in Celsius
 * @param timestamp   moment of the reading, now when null
 */
public record RecordReadingCommand(Long deviceId, double latitude, double longitude, double speed,
                                   double temperature, Instant timestamp) {
}
