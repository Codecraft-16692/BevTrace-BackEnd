package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import java.time.LocalDate;

/**
 * JSON representation of a waste record (field names are the ones the Angular frontend consumes).
 */
public record WasteRecordResource(Long id, Long batchId, Long userId, int quantity, String reason, LocalDate reportedDate) {
}
