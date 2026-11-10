package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.DeliveryRecord;
import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.DeliveryOutcome;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link DeliveryRecord}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "delivery_records")
public class DeliveryRecordPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long logId;
    private String receivedBy;
    private String signatureUrl;
    private Instant deliveredAt;
    @Enumerated(EnumType.STRING)
    private DeliveryOutcome status;
    private String rejectionReason;

    public static DeliveryRecordPersistenceEntity fromDomain(DeliveryRecord d) {
        var e = new DeliveryRecordPersistenceEntity();
        e.setId(d.getId());
        e.logId = d.getLogId();
        e.receivedBy = d.getReceivedBy();
        e.signatureUrl = d.getSignatureUrl();
        e.deliveredAt = d.getDeliveredAt();
        e.status = d.getStatus();
        e.rejectionReason = d.getRejectionReason();
        return e;
    }

    public DeliveryRecord toDomain() {
        return DeliveryRecord.restore(getId(), logId, receivedBy, signatureUrl, deliveredAt, status, rejectionReason);
    }
}
