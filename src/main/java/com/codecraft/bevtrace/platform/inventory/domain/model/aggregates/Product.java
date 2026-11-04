package com.codecraft.bevtrace.platform.inventory.domain.model.aggregates;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

/**
 * A beverage product of the catalogue (reference data).
 */
public class Product extends AbstractDomainAggregateRoot<Product> {

    private Long id;
    private String skuCode;
    private String name;
    private String category;
    private int volumeMl;
    private String packagingType;

    protected Product() {
    }

    public Long getId() {
        return id;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getVolumeMl() {
        return volumeMl;
    }

    public String getPackagingType() {
        return packagingType;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static Product restore(Long id, String skuCode, String name, String category, int volumeMl, String packagingType) {
        var a = new Product();
        a.id = id;
        a.skuCode = skuCode;
        a.name = name;
        a.category = category;
        a.volumeMl = volumeMl;
        a.packagingType = packagingType;
        return a;
    }
}
