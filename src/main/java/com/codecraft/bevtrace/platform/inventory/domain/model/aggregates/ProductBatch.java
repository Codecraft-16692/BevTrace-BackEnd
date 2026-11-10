package com.codecraft.bevtrace.platform.inventory.domain.model.aggregates;

import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.BatchStatus;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Map;

/**
 * A production batch of a product stored in a zone. It owns the stock rules:
 * receiving, deducting (dispatch, waste) and adjusting after a physical count.
 */
public class ProductBatch extends AbstractDomainAggregateRoot<ProductBatch> {

    private Long id;
    private Long productId;
    private Long zoneId;
    private String batchNumber;
    private int initialQty;
    private int currentQty;
    private BatchStatus status;
    private Instant receivedAt;
    private LocalDate expirationDate;

    protected ProductBatch() {
    }

    /**
     * Registers a batch announced by the plant; it has no stock until it is scanned.
     *
     * @param productId      product of the batch
     * @param zoneId         destination zone
     * @param batchNumber    unique code printed on the label
     * @param initialQty     units announced
     * @param expirationDate expiration date
     * @return the new batch
     */
    public static ProductBatch announce(Long productId, Long zoneId, String batchNumber, int initialQty,
                                        LocalDate expirationDate) {
        var batch = new ProductBatch();
        batch.productId = productId;
        batch.zoneId = zoneId;
        batch.batchNumber = batchNumber;
        batch.initialQty = initialQty;
        batch.currentQty = 0;
        batch.status = BatchStatus.EXPECTED;
        batch.expirationDate = expirationDate;
        return batch;
    }

    /**
     * Registers the entry at the dock: the whole announced quantity becomes available.
     *
     * @param at reception time
     */
    public void receive(Instant at) {
        if (status != BatchStatus.EXPECTED) {
            throw new IllegalStateException("inventory.errors.already-received");
        }
        status = BatchStatus.AVAILABLE;
        currentQty = initialQty;
        receivedAt = at;
    }

    /**
     * Removes units from the stock (dispatch departure or waste).
     *
     * @param quantity units to remove, positive
     */
    public void deduct(int quantity) {
        if (quantity <= 0) {
            throw new IllegalStateException("inventory.errors.quantity-invalid");
        }
        if (status != BatchStatus.AVAILABLE) {
            throw new IllegalStateException("inventory.errors.batch-not-available");
        }
        if (quantity > currentQty) {
            throw new IllegalStateException("inventory.errors.waste-exceeds-stock");
        }
        currentQty -= quantity;
        refreshStatus();
    }

    /**
     * Sets the stock to the physically counted quantity.
     *
     * @param counted units found in the warehouse
     */
    public void adjustTo(int counted) {
        currentQty = counted;
        refreshStatus();
    }

    private void refreshStatus() {
        status = currentQty == 0 ? BatchStatus.DEPLETED : BatchStatus.AVAILABLE;
    }

    public Long getProductId() {
        return productId;
    }

    public String getBatchNumber() {
        return batchNumber;
    }

    public int getInitialQty() {
        return initialQty;
    }

    public int getCurrentQty() {
        return currentQty;
    }

    public BatchStatus getStatus() {
        return status;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getZoneId() {
        return zoneId;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static ProductBatch restore(Long id, Long productId, Long zoneId, String batchNumber, int initialQty, int currentQty, BatchStatus status, Instant receivedAt, LocalDate expirationDate) {
        var a = new ProductBatch();
        a.id = id;
        a.productId = productId;
        a.zoneId = zoneId;
        a.batchNumber = batchNumber;
        a.initialQty = initialQty;
        a.currentQty = currentQty;
        a.status = status;
        a.receivedAt = receivedAt;
        a.expirationDate = expirationDate;
        return a;
    }
}
