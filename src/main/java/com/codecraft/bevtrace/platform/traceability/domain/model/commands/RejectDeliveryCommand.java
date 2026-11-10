package com.codecraft.bevtrace.platform.traceability.domain.model.commands;

/**
 * Registers a rejected delivery (the shipment stays open).
 *
 * @param logId  log of the shipment
 * @param reason why the client rejected it
 */
public record RejectDeliveryCommand(Long logId, String reason) {
}
