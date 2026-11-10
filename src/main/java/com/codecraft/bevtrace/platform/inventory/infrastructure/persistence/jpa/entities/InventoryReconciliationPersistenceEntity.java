package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.InventoryReconciliation;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.ReconciliationStatus;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Stored form of {@link InventoryReconciliation}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "inventory_reconciliations")
public class InventoryReconciliationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "reconciliation_date")
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private ReconciliationStatus status;
    private int totalBatches;
    private int matchedBatches;
    private double eriPercentage;
    private Long confirmedBy;
    private Instant confirmedAt;

    public static InventoryReconciliationPersistenceEntity fromDomain(InventoryReconciliation d) {
        var e = new InventoryReconciliationPersistenceEntity();
        e.setId(d.getId());
        e.date = d.getDate();
        e.status = d.getStatus();
        e.totalBatches = d.getTotalBatches();
        e.matchedBatches = d.getMatchedBatches();
        e.eriPercentage = d.getEriPercentage();
        e.confirmedBy = d.getConfirmedBy();
        e.confirmedAt = d.getConfirmedAt();
        return e;
    }

    public InventoryReconciliation toDomain() {
        return InventoryReconciliation.restore(getId(), date, status, totalBatches, matchedBatches, eriPercentage, confirmedBy, confirmedAt);
    }
}
