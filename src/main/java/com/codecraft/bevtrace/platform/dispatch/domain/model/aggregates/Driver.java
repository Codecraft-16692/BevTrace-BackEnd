package com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates;

import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DriverStatus;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

/**
 * A licensed driver (reference data).
 */
public class Driver extends AbstractDomainAggregateRoot<Driver> {

    private Long id;
    private String fullName;
    private String licenseNumber;
    private DriverStatus status;

    protected Driver() {
    }

    public boolean isActive() {
        return status == DriverStatus.ACTIVE;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public DriverStatus getStatus() {
        return status;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static Driver restore(Long id, String fullName, String licenseNumber, DriverStatus status) {
        var a = new Driver();
        a.id = id;
        a.fullName = fullName;
        a.licenseNumber = licenseNumber;
        a.status = status;
        return a;
    }
}
