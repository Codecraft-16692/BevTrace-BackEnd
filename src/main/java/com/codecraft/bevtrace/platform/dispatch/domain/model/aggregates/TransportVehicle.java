package com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.util.Map;

/**
 * A truck of the fleet. It is reserved while a dispatch uses it.
 */
public class TransportVehicle extends AbstractDomainAggregateRoot<TransportVehicle> {

    private Long id;
    private Long driverId;
    private String plateNumber;
    private double maxCapacity;
    private boolean isAvailable;

    protected TransportVehicle() {
    }

    /**
     * Reserves the vehicle for a dispatch.
     */
    public void reserve() {
        if (!isAvailable) {
            throw new IllegalStateException("dispatch.errors.vehicle-unavailable");
        }
        isAvailable = false;
    }

    /**
     * Makes the vehicle available again.
     */
    public void release() {
        isAvailable = true;
    }

    public Long getDriverId() {
        return driverId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public Long getId() {
        return id;
    }

    public boolean getIsAvailable() {
        return isAvailable;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static TransportVehicle restore(Long id, Long driverId, String plateNumber, double maxCapacity, boolean isAvailable) {
        var a = new TransportVehicle();
        a.id = id;
        a.driverId = driverId;
        a.plateNumber = plateNumber;
        a.maxCapacity = maxCapacity;
        a.isAvailable = isAvailable;
        return a;
    }
}
