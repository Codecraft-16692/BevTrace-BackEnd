package com.codecraft.bevtrace.platform.inventory.domain.model.aggregates;

import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.DiscrepancyStatus;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * Difference between the expected and the counted units of one batch.
 */
public class InventoryDiscrepancy extends AbstractDomainAggregateRoot<InventoryDiscrepancy> {

    private Long id;
    private Long reconciliationId;
    private Long batchId;
    private int expectedQty;
    private int countedQty;
    private DiscrepancyStatus status;
    private Instant detectedAt;
    private Instant resolvedAt;
    private String resolutionNote;

    protected InventoryDiscrepancy() {
    }

    /**
     * Records a difference.
     *
     * @param reconciliationId reconciliation that found it
     * @param batchId          batch concerned
     * @param expectedQty      system stock
     * @param countedQty       physical count
     * @return an open discrepancy
     */
    public static InventoryDiscrepancy detect(Long reconciliationId, Long batchId, int expectedQty, int countedQty) {
        var discrepancy = new InventoryDiscrepancy();
        discrepancy.reconciliationId = reconciliationId;
        discrepancy.batchId = batchId;
        discrepancy.expectedQty = expectedQty;
        discrepancy.countedQty = countedQty;
        discrepancy.status = DiscrepancyStatus.OPEN;
        discrepancy.detectedAt = Instant.now();
        return discrepancy;
    }

    /**
     * Closes the discrepancy with an explanation. The batch stock is adjusted by the caller.
     *
     * @param note explanation, at least 3 characters
     */
    public void resolve(String note) {
        if (status != DiscrepancyStatus.OPEN) {
            throw new IllegalStateException("inventory.errors.discrepancy-closed");
        }
        if (note == null || note.trim().length() < 3) {
            throw new IllegalStateException("inventory.errors.note-required");
        }
        status = DiscrepancyStatus.RESOLVED;
        resolvedAt = Instant.now();
        resolutionNote = note.trim();
    }

    public Long getBatchId() {
        return batchId;
    }

    public int getCountedQty() {
        return countedQty;
    }

    public Long getId() {
        return id;
    }

    public Long getReconciliationId() {
        return reconciliationId;
    }

    public int getExpectedQty() {
        return expectedQty;
    }

    public DiscrepancyStatus getStatus() {
        return status;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public Instant getResolvedAt() {
        return resolvedAt;
    }

    public String getResolutionNote() {
        return resolutionNote;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static InventoryDiscrepancy restore(Long id, Long reconciliationId, Long batchId, int expectedQty, int countedQty, DiscrepancyStatus status, Instant detectedAt, Instant resolvedAt, String resolutionNote) {
        var a = new InventoryDiscrepancy();
        a.id = id;
        a.reconciliationId = reconciliationId;
        a.batchId = batchId;
        a.expectedQty = expectedQty;
        a.countedQty = countedQty;
        a.status = status;
        a.detectedAt = detectedAt;
        a.resolvedAt = resolvedAt;
        a.resolutionNote = resolutionNote;
        return a;
    }
}
