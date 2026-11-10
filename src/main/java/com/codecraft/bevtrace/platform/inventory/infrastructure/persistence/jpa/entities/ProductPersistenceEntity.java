package com.codecraft.bevtrace.platform.inventory.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.inventory.domain.model.aggregates.Product;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link Product}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "products")
public class ProductPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String skuCode;
    private String name;
    private String category;
    private int volumeMl;
    private String packagingType;

    public static ProductPersistenceEntity fromDomain(Product d) {
        var e = new ProductPersistenceEntity();
        e.setId(d.getId());
        e.skuCode = d.getSkuCode();
        e.name = d.getName();
        e.category = d.getCategory();
        e.volumeMl = d.getVolumeMl();
        e.packagingType = d.getPackagingType();
        return e;
    }

    public Product toDomain() {
        return Product.restore(getId(), skuCode, name, category, volumeMl, packagingType);
    }
}
