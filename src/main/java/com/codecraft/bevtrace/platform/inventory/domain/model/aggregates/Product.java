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

    /**
     * Registers a new product of the catalogue.
     *
     * @param skuCode       unique stock-keeping code
     * @param name          product name
     * @param category      category (water, soda, beer...)
     * @param volumeMl      volume of one unit in millilitres, positive
     * @param packagingType packaging (bottle, can...)
     * @return the new product
     */
    public static Product register(String skuCode, String name, String category, int volumeMl, String packagingType) {
        if (volumeMl <= 0) {
            throw new IllegalStateException("inventory.errors.volume-invalid");
        }
        var p = new Product();
        p.skuCode = skuCode.trim().toUpperCase();
        p.name = name.trim();
        p.category = category.trim();
        p.volumeMl = volumeMl;
        p.packagingType = packagingType.trim();
        return p;
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
