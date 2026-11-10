package com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.CheckpointStatus;
import java.time.Instant;

/**
 * JSON representation of a route checkpoint (field names are the ones the Angular frontend consumes).
 */
public record RouteCheckpointResource(Long id, Long logId, int sequence, String locationName, double latitude, double longitude, CheckpointStatus status, Instant reachedAt, String observation) {
}
