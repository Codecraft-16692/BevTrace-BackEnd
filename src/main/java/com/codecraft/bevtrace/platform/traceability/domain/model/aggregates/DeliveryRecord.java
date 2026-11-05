package com.codecraft.bevtrace.platform.traceability.domain.model.aggregates;

import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.DeliveryOutcome;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * Proof of a delivery attempt: who received the cargo, or why it was rejected.
 */
public class DeliveryRecord extends AbstractDomainAggregateRoot<DeliveryRecord> {

    private Long id;
    private Long logId;
    private String receivedBy;
    private String signatureUrl;
    private Instant deliveredAt;
    private DeliveryOutcome status;
    private String rejectionReason;

    protected DeliveryRecord() {
    }

    /**
     * Records a successful hand-over.
     *
     * @param logId      shipment
     * @param receivedBy person that signed, at least 3 characters
     * @param at         hand-over time
     * @return the record
     */
    public static DeliveryRecord delivered(Long logId, String receivedBy, Instant at) {
        if (receivedBy == null || receivedBy.trim().length() < 3) {
            throw new IllegalStateException("traceability.errors.receiver-required");
        }
        var record = new DeliveryRecord();
        record.logId = logId;
        record.receivedBy = receivedBy.trim();
        record.deliveredAt = at;
        record.status = DeliveryOutcome.DELIVERED;
        return record;
    }

    /**
     * Records a rejected hand-over.
     *
     * @param logId  shipment
     * @param reason why the client refused the cargo
     * @param at     attempt time
     * @return the record
     */
    public static DeliveryRecord rejected(Long logId, String reason, Instant at) {
        var record = new DeliveryRecord();
        record.logId = logId;
        record.receivedBy = "-";
        record.deliveredAt = at;
        record.status = DeliveryOutcome.REJECTED;
        record.rejectionReason = reason.trim();
        return record;
    }

    public Long getId() {
        return id;
    }

    public Long getLogId() {
        return logId;
    }

    public String getReceivedBy() {
        return receivedBy;
    }

    public String getSignatureUrl() {
        return signatureUrl;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public DeliveryOutcome getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static DeliveryRecord restore(Long id, Long logId, String receivedBy, String signatureUrl, Instant deliveredAt, DeliveryOutcome status, String rejectionReason) {
        var a = new DeliveryRecord();
        a.id = id;
        a.logId = logId;
        a.receivedBy = receivedBy;
        a.signatureUrl = signatureUrl;
        a.deliveredAt = deliveredAt;
        a.status = status;
        a.rejectionReason = rejectionReason;
        return a;
    }
}
