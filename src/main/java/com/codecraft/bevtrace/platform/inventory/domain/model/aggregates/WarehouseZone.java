package com.codecraft.bevtrace.platform.inventory.domain.model.aggregates;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

/**
 * A storage zone of the warehouse (reference data).
 */
public class WarehouseZone extends AbstractDomainAggregateRoot<WarehouseZone> {

    private Long id;
    private String name;
    private String aisle;
    private String rack;

    protected WarehouseZone() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAisle() {
        return aisle;
    }

    public String getRack() {
        return rack;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static WarehouseZone restore(Long id, String name, String aisle, String rack) {
        var a = new WarehouseZone();
        a.id = id;
        a.name = name;
        a.aisle = aisle;
        a.rack = rack;
        return a;
    }
}
