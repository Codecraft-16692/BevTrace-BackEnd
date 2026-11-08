package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.TransportVehicle;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link TransportVehicle}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "transport_vehicles")
public class TransportVehiclePersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long driverId;
    private String plateNumber;
    private double maxCapacity;
    private boolean isAvailable;

    public static TransportVehiclePersistenceEntity fromDomain(TransportVehicle d) {
        var e = new TransportVehiclePersistenceEntity();
        e.setId(d.getId());
        e.driverId = d.getDriverId();
        e.plateNumber = d.getPlateNumber();
        e.maxCapacity = d.getMaxCapacity();
        e.isAvailable = d.getIsAvailable();
        return e;
    }

    public TransportVehicle toDomain() {
        return TransportVehicle.restore(getId(), driverId, plateNumber, maxCapacity, isAvailable);
    }
}
