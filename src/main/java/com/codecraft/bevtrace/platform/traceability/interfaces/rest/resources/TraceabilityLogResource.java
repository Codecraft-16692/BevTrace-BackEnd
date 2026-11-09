package com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.LogStatus;
import java.time.Instant;

/**
 * JSON representation of a traceability log (field names are the ones the Angular frontend consumes).
 */
public record TraceabilityLogResource(Long id, Long dispatchOrderId, LogStatus currentStatus, Instant startTime, Instant endTime, Instant estimatedArrival, double currentLatitude, double currentLongitude, String rejectionReason, String orderPriority, String destinationName, String vehiclePlate, Long deviceId) {
}
