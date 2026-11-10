package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DeliveryDestination;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link DeliveryDestination}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "delivery_destinations")
public class DeliveryDestinationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private String clientName;
    private String address;
    private double latitude;
    private double longitude;
    private String region;

    public static DeliveryDestinationPersistenceEntity fromDomain(DeliveryDestination d) {
        var e = new DeliveryDestinationPersistenceEntity();
        e.setId(d.getId());
        e.clientName = d.getClientName();
        e.address = d.getAddress();
        e.latitude = d.getLatitude();
        e.longitude = d.getLongitude();
        e.region = d.getRegion();
        return e;
    }

    public DeliveryDestination toDomain() {
        return DeliveryDestination.restore(getId(), clientName, address, latitude, longitude, region);
    }
}
