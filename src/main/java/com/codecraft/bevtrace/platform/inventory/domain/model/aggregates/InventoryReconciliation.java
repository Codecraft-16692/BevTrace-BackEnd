package com.codecraft.bevtrace.platform.inventory.domain.model.aggregates;

import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.ReconciliationStatus;

import com.codecraft.bevtrace.platform.inventory.domain.model.events.DiscrepanciesDetected;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

/**
 * A physical count of the warehouse compared with the system stock.
 *
 * <p>{@code eriPercentage} is the Inventory Record Accuracy: matched batches over counted batches.
 * A count without differences is reconciled immediately; otherwise it waits in review until
 * every discrepancy is resolved and a manager confirms it.</p>
 */
public class InventoryReconciliation extends AbstractDomainAggregateRoot<InventoryReconciliation> {

    private Long id;
    private LocalDate date;
    private ReconciliationStatus status;
    private int totalBatches;
    private int matchedBatches;
    private double eriPercentage;
    private Long confirmedBy;
    private Instant confirmedAt;

    protected InventoryReconciliation() {
    }

    /**
     * Opens a reconciliation from the result of a count.
     *
     * @param totalBatches   batches counted, at least 1
     * @param matchedBatches batches whose count equals the system stock
     * @param userId         user that ran the count
     * @return the reconciliation (already reconciled when nothing differs)
     */
    public static InventoryReconciliation start(int totalBatches, int matchedBatches, Long userId) {
        if (totalBatches <= 0) {
            throw new IllegalStateException("inventory.errors.no-counts");
        }
        var now = Instant.now();
        var reconciliation = new InventoryReconciliation();
        reconciliation.date = LocalDate.now();
        reconciliation.totalBatches = totalBatches;
        reconciliation.matchedBatches = matchedBatches;
        reconciliation.eriPercentage = Math.round((double) matchedBatches / totalBatches * 1000) / 10.0;
        boolean clean = matchedBatches == totalBatches;
        reconciliation.status = clean ? ReconciliationStatus.RECONCILED : ReconciliationStatus.IN_REVIEW;
        if (clean) {
            reconciliation.confirmedBy = userId;
            reconciliation.confirmedAt = now;
        } else {
            reconciliation.registerDomainEvent(new DiscrepanciesDetected(totalBatches - matchedBatches));
        }
        return reconciliation;
    }

    /**
     * Confirms the reconciliation once no discrepancy is open.
     *
     * @param userId        confirming manager
     * @param openDiscrepancies number of discrepancies still open
     */
    public void confirm(Long userId, long openDiscrepancies) {
        if (openDiscrepancies > 0) {
            throw new IllegalStateException("inventory.errors.pending-discrepancies");
        }
        if (status == ReconciliationStatus.RECONCILED) {
            return;
        }
        status = ReconciliationStatus.RECONCILED;
        confirmedBy = userId;
        confirmedAt = Instant.now();
    }

    public LocalDate getDate() {
        return date;
    }

    public ReconciliationStatus getStatus() {
        return status;
    }

    public double getEriPercentage() {
        return eriPercentage;
    }

    public Long getId() {
        return id;
    }

    public int getTotalBatches() {
        return totalBatches;
    }

    public int getMatchedBatches() {
        return matchedBatches;
    }

    public Long getConfirmedBy() {
        return confirmedBy;
    }

    public Instant getConfirmedAt() {
        return confirmedAt;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static InventoryReconciliation restore(Long id, LocalDate date, ReconciliationStatus status, int totalBatches, int matchedBatches, double eriPercentage, Long confirmedBy, Instant confirmedAt) {
        var a = new InventoryReconciliation();
        a.id = id;
        a.date = date;
        a.status = status;
        a.totalBatches = totalBatches;
        a.matchedBatches = matchedBatches;
        a.eriPercentage = eriPercentage;
        a.confirmedBy = confirmedBy;
        a.confirmedAt = confirmedAt;
        return a;
    }
}
