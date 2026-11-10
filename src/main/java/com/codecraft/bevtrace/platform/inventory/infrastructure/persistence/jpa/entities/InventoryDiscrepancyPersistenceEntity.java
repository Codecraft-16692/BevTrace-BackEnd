package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryDiscrepancy;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.DiscrepancyStatus;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link InventoryDiscrepancy}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "inventory_discrepancies")
public class InventoryDiscrepancyPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long reconciliationId;
    private Long batchId;
    private int expectedQty;
    private int countedQty;
    @Enumerated(EnumType.STRING)
    private DiscrepancyStatus status;
    private Instant detectedAt;
    private Instant resolvedAt;
    private String resolutionNote;

    public static InventoryDiscrepancyPersistenceEntity fromDomain(InventoryDiscrepancy d) {
        var e = new InventoryDiscrepancyPersistenceEntity();
        e.setId(d.getId());
        e.reconciliationId = d.getReconciliationId();
        e.batchId = d.getBatchId();
        e.expectedQty = d.getExpectedQty();
        e.countedQty = d.getCountedQty();
        e.status = d.getStatus();
        e.detectedAt = d.getDetectedAt();
        e.resolvedAt = d.getResolvedAt();
        e.resolutionNote = d.getResolutionNote();
        return e;
    }

    public InventoryDiscrepancy toDomain() {
        return InventoryDiscrepancy.restore(getId(), reconciliationId, batchId, expectedQty, countedQty, status, detectedAt, resolvedAt, resolutionNote);
    }
}
