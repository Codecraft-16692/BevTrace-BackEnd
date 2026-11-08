package com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

/**
 * A client location that receives deliveries (reference data).
 */
public class DeliveryDestination extends AbstractDomainAggregateRoot<DeliveryDestination> {

    private Long id;
    private String clientName;
    private String address;
    private double latitude;
    private double longitude;
    private String region;

    protected DeliveryDestination() {
    }

    public String getClientName() {
        return clientName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getRegion() {
        return region;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static DeliveryDestination restore(Long id, String clientName, String address, double latitude, double longitude, String region) {
        var a = new DeliveryDestination();
        a.id = id;
        a.clientName = clientName;
        a.address = address;
        a.latitude = latitude;
        a.longitude = longitude;
        a.region = region;
        return a;
    }
}
