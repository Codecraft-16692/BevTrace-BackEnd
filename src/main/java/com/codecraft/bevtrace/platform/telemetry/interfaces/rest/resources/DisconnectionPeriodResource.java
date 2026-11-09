package com.codecraft.bevtrace.platform.telemetry.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.telemetry.domain.model.valueobjects.DataStatus;
import java.time.Instant;

/**
 * JSON representation of a disconnection period (field names are the ones the Angular frontend consumes).
 */
public record DisconnectionPeriodResource(Long id, Long deviceId, Instant startedAt, Instant endedAt, DataStatus dataStatus) {
}
