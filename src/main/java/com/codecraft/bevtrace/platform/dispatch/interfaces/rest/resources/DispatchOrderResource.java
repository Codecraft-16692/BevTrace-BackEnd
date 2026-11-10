package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchPriority;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchStatus;
import java.time.Instant;
import java.time.LocalDate;

/**
 * JSON representation of a dispatch order (field names are the ones the Angular frontend consumes).
 */
public record DispatchOrderResource(Long id, Long managerId, Long vehicleId, Long destinationId, LocalDate scheduledDate, DispatchStatus status, DispatchPriority priority, double estimatedWeight, int requestedQuantity, Integer deliveredQuantity, Instant deliveredAt, Instant departedAt, boolean cargoValidated, Instant createdAt) {
}
