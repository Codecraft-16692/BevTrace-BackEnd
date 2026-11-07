package com.codecraft.bevtrace.platform.traceability.infrastructure.persistence.jpa.entities;

import com.codecraft.bevtrace.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import com.codecraft.bevtrace.platform.traceability.domain.model.aggregates.TraceabilityLog;
import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.LogStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Stored form of {@link TraceabilityLog}. It only maps columns: the rules live in the aggregate.
 */
@Entity
@Table(name = "traceability_logs")
public class TraceabilityLogPersistenceEntity extends AuditableAbstractPersistenceEntity {

    private Long dispatchOrderId;
    @Enumerated(EnumType.STRING)
    private LogStatus currentStatus;
    private Instant startTime;
    private Instant endTime;
    private Instant estimatedArrival;
    private double currentLatitude;
    private double currentLongitude;
    private String rejectionReason;
    private String orderPriority;
    private String destinationName;
    private String vehiclePlate;
    private Long deviceId;

    public static TraceabilityLogPersistenceEntity fromDomain(TraceabilityLog d) {
        var e = new TraceabilityLogPersistenceEntity();
        e.setId(d.getId());
        e.dispatchOrderId = d.getDispatchOrderId();
        e.currentStatus = d.getCurrentStatus();
        e.startTime = d.getStartTime();
        e.endTime = d.getEndTime();
        e.estimatedArrival = d.getEstimatedArrival();
        e.currentLatitude = d.getCurrentLatitude();
        e.currentLongitude = d.getCurrentLongitude();
        e.rejectionReason = d.getRejectionReason();
        e.orderPriority = d.getOrderPriority();
        e.destinationName = d.getDestinationName();
        e.vehiclePlate = d.getVehiclePlate();
        e.deviceId = d.getDeviceId();
        return e;
    }

    public TraceabilityLog toDomain() {
        return TraceabilityLog.restore(getId(), dispatchOrderId, currentStatus, startTime, endTime, estimatedArrival, currentLatitude, currentLongitude, rejectionReason, orderPriority, destinationName, vehiclePlate, deviceId);
    }
}
