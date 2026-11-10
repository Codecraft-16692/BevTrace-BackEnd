package com.codecraft.bevtrace.platform.telemetry.domain.model.commands;

import java.time.Duration;

/**
 * Marks as disconnected the devices that have been silent for too long.
 *
 * @param silence maximum silence of a connected device
 */
public record DetectDisconnectionsCommand(Duration silence) {
}
