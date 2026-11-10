package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources;

/**
 * JSON representation of a cargo assignment (field names are the ones the Angular frontend consumes).
 */
public record CargoAssignmentResource(Long id, Long dispatchOrderId, Long batchId, int quantity, double totalWeight, int palletCount) {
}
