package com.codecraft.bevtrace.platform.traceability.domain.model.commands;

/**
 * Confirms the delivery of a shipment.
 *
 * @param logId      log of the shipment
 * @param receivedBy person that signed the reception
 */
public record ConfirmDeliveryCommand(Long logId, String receivedBy) {
}
