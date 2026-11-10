package com.codecraft.bevtrace.platform.inventory.domain.model.aggregates;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.LocalDate;

/**
 * Units of a batch lost to damage, expiration or handling.
 */
public class WasteRecord extends AbstractDomainAggregateRoot<WasteRecord> {

    private Long id;
    private Long batchId;
    private Long userId;
    private int quantity;
    private String reason;
    private LocalDate reportedDate;

    protected WasteRecord() {
    }

    /**
     * Records a waste. The caller deducts the units from the batch in the same transaction.
     *
     * @param batchId  affected batch
     * @param userId   reporting user
     * @param quantity lost units
     * @param reason   free-text explanation
     * @return the record
     */
    public static WasteRecord report(Long batchId, Long userId, int quantity, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new IllegalStateException("inventory.errors.reason-required");
        }
        var record = new WasteRecord();
        record.batchId = batchId;
        record.userId = userId;
        record.quantity = quantity;
        record.reason = reason.trim();
        record.reportedDate = LocalDate.now();
        return record;
    }

    public Long getBatchId() {
        return batchId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static WasteRecord restore(Long id, Long batchId, Long userId, int quantity, String reason, LocalDate reportedDate) {
        var a = new WasteRecord();
        a.id = id;
        a.batchId = batchId;
        a.userId = userId;
        a.quantity = quantity;
        a.reason = reason;
        a.reportedDate = reportedDate;
        return a;
    }
}
