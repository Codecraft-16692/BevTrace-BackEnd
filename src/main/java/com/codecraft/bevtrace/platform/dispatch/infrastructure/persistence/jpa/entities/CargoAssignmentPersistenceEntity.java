package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.CargoAssignment;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Stored form of {@link CargoAssignment}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "cargo_assignments")
public class CargoAssignmentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long dispatchOrderId;
    private Long batchId;
    private int quantity;
    private double totalWeight;
    private int palletCount;

    public static CargoAssignmentPersistenceEntity fromDomain(CargoAssignment d) {
        var e = new CargoAssignmentPersistenceEntity();
        e.setId(d.getId());
        e.dispatchOrderId = d.getDispatchOrderId();
        e.batchId = d.getBatchId();
        e.quantity = d.getQuantity();
        e.totalWeight = d.getTotalWeight();
        e.palletCount = d.getPalletCount();
        return e;
    }

    public CargoAssignment toDomain() {
        return CargoAssignment.restore(getId(), dispatchOrderId, batchId, quantity, totalWeight, palletCount);
    }
}
