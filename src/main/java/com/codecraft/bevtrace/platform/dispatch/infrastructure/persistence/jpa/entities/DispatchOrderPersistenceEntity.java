package com.codecraft.bevtrace.platform.dispatch.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates.DispatchOrder;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchPriority;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchStatus;
import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

/**
 * Stored form of {@link DispatchOrder}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "dispatch_orders")
public class DispatchOrderPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long managerId;
    private Long vehicleId;
    private Long destinationId;
    private LocalDate scheduledDate;
    @Enumerated(EnumType.STRING)
    private DispatchStatus status;
    @Enumerated(EnumType.STRING)
    private DispatchPriority priority;
    private double estimatedWeight;
    private int requestedQuantity;
    private Integer deliveredQuantity;
    private Instant deliveredAt;
    private Instant departedAt;
    private boolean cargoValidated;

    public static DispatchOrderPersistenceEntity fromDomain(DispatchOrder d) {
        var e = new DispatchOrderPersistenceEntity();
        e.setId(d.getId());
        e.managerId = d.getManagerId();
        e.vehicleId = d.getVehicleId();
        e.destinationId = d.getDestinationId();
        e.scheduledDate = d.getScheduledDate();
        e.status = d.getStatus();
        e.priority = d.getPriority();
        e.estimatedWeight = d.getEstimatedWeight();
        e.requestedQuantity = d.getRequestedQuantity();
        e.deliveredQuantity = d.getDeliveredQuantity();
        e.deliveredAt = d.getDeliveredAt();
        e.departedAt = d.getDepartedAt();
        e.cargoValidated = d.getCargoValidated();
        return e;
    }

    public DispatchOrder toDomain() {
        return DispatchOrder.restore(getId(), managerId, vehicleId, destinationId, scheduledDate, status, priority, estimatedWeight, requestedQuantity, deliveredQuantity, deliveredAt, departedAt, cargoValidated, getCreatedAt() == null ? null : getCreatedAt().toInstant());
    }
}
