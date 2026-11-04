package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.WasteRecord;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;

/**
 * Stored form of {@link WasteRecord}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "waste_records")
public class WasteRecordPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long batchId;
    private Long userId;
    private int quantity;
    private String reason;
    private LocalDate reportedDate;

    public static WasteRecordPersistenceEntity fromDomain(WasteRecord d) {
        var e = new WasteRecordPersistenceEntity();
        e.setId(d.getId());
        e.batchId = d.getBatchId();
        e.userId = d.getUserId();
        e.quantity = d.getQuantity();
        e.reason = d.getReason();
        e.reportedDate = d.getReportedDate();
        return e;
    }

    public WasteRecord toDomain() {
        return WasteRecord.restore(getId(), batchId, userId, quantity, reason, reportedDate);
    }
}
