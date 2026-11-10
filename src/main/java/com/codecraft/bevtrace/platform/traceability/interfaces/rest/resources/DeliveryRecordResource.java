package com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.DeliveryOutcome;
import java.time.Instant;

/**
 * JSON representation of a delivery record (field names are the ones the Angular frontend consumes).
 */
public record DeliveryRecordResource(Long id, Long logId, String receivedBy, String signatureUrl, Instant deliveredAt, DeliveryOutcome status, String rejectionReason) {
}
