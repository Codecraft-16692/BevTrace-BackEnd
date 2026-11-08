package com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates;

import com.codecraft.bevtrace.platform.dispatch.domain.services.CargoPolicy;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

/**
 * Units of one batch reserved by a dispatch order.
 */
public class CargoAssignment extends AbstractDomainAggregateRoot<CargoAssignment> {

    private Long id;
    private Long dispatchOrderId;
    private Long batchId;
    private int quantity;
    private double totalWeight;
    private int palletCount;

    protected CargoAssignment() {
    }

    /**
     * Reserves units of a batch for an order.
     *
     * @param dispatchOrderId order
     * @param batchId         batch
     * @param quantity        units
     * @return the assignment, with weight and pallets derived from the quantity
     */
    public static CargoAssignment of(Long dispatchOrderId, Long batchId, int quantity) {
        var cargo = new CargoAssignment();
        cargo.dispatchOrderId = dispatchOrderId;
        cargo.batchId = batchId;
        cargo.quantity = quantity;
        cargo.totalWeight = CargoPolicy.weightOf(quantity);
        cargo.palletCount = CargoPolicy.palletsOf(quantity);
        return cargo;
    }

    public Long getBatchId() {
        return batchId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPalletCount() {
        return palletCount;
    }

    public Long getId() {
        return id;
    }

    public Long getDispatchOrderId() {
        return dispatchOrderId;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static CargoAssignment restore(Long id, Long dispatchOrderId, Long batchId, int quantity, double totalWeight, int palletCount) {
        var a = new CargoAssignment();
        a.id = id;
        a.dispatchOrderId = dispatchOrderId;
        a.batchId = batchId;
        a.quantity = quantity;
        a.totalWeight = totalWeight;
        a.palletCount = palletCount;
        return a;
    }
}
