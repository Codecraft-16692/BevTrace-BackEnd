package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.ProductBatch;
import com.codecraft.bevtrace.platform.inventory.domain.model.valueobjects.BatchStatus;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Stored form of {@link ProductBatch}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "product_batches")
public class ProductBatchPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long productId;
    private Long zoneId;
    private String batchNumber;
    private int initialQty;
    private int currentQty;
    @Enumerated(EnumType.STRING)
    private BatchStatus status;
    private Instant receivedAt;
    private LocalDate expirationDate;

    public static ProductBatchPersistenceEntity fromDomain(ProductBatch d) {
        var e = new ProductBatchPersistenceEntity();
        e.setId(d.getId());
        e.productId = d.getProductId();
        e.zoneId = d.getZoneId();
        e.batchNumber = d.getBatchNumber();
        e.initialQty = d.getInitialQty();
        e.currentQty = d.getCurrentQty();
        e.status = d.getStatus();
        e.receivedAt = d.getReceivedAt();
        e.expirationDate = d.getExpirationDate();
        return e;
    }

    public ProductBatch toDomain() {
        return ProductBatch.restore(getId(), productId, zoneId, batchNumber, initialQty, currentQty, status, receivedAt, expirationDate);
    }
}
