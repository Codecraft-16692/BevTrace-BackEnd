package com.codecraft.bevtrace.platform.traceability.domain.model.aggregates;

import com.codecraft.bevtrace.platform.traceability.domain.model.valueobjects.LogStatus;
import com.codecraft.bevtrace.platform.traceability.domain.services.RoutePlanner;

import com.codecraft.bevtrace.platform.traceability.domain.model.events.DeliveryConfirmed;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;

/**
 * The follow-up of one dispatch from departure to delivery. Position and checkpoints are produced
 * by the platform (telemetry), never typed by users.
 */
public class TraceabilityLog extends AbstractDomainAggregateRoot<TraceabilityLog> {

    private Long id;
    private Long dispatchOrderId;
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

    protected TraceabilityLog() {
    }

    /**
     * Starts following a shipment that just left the warehouse.
     *
     * @param dispatchOrderId order being followed
     * @param priority        priority name copied from the order
     * @param destinationName client name
     * @param vehiclePlate    truck plate
     * @param deviceId        IoT device of the truck, null when none is installed
     * @param departedAt      departure time
     * @param estimatedArrival planned arrival
     * @return the log, positioned at the warehouse
     */
    public static TraceabilityLog start(Long dispatchOrderId, String priority, String destinationName,
                                        String vehiclePlate, Long deviceId, Instant departedAt,
                                        Instant estimatedArrival) {
        var log = new TraceabilityLog();
        log.dispatchOrderId = dispatchOrderId;
        log.currentStatus = LogStatus.IN_TRANSIT;
        log.startTime = departedAt;
        log.estimatedArrival = estimatedArrival;
        log.currentLatitude = RoutePlanner.ORIGIN_LATITUDE;
        log.currentLongitude = RoutePlanner.ORIGIN_LONGITUDE;
        log.orderPriority = priority;
        log.destinationName = destinationName;
        log.vehiclePlate = vehiclePlate;
        log.deviceId = deviceId;
        return log;
    }

    /**
     * Updates the last known position reported by the device.
     *
     * @param latitude  latitude
     * @param longitude longitude
     */
    public void moveTo(double latitude, double longitude) {
        if (currentStatus == LogStatus.COMPLETED) {
            return;
        }
        currentLatitude = latitude;
        currentLongitude = longitude;
    }

    /**
     * Confirms the delivery; the dispatch context closes the order when it receives the event.
     *
     * @param at delivery time
     */
    public void complete(Instant at) {
        requireOpen();
        currentStatus = LogStatus.COMPLETED;
        endTime = at;
        rejectionReason = null;
        registerDomainEvent(new DeliveryConfirmed(dispatchOrderId, destinationName, at));
    }

    /**
     * Registers that the client rejected the cargo; the shipment stays open.
     *
     * @param reason explanation, at least 5 characters
     */
    public void reject(String reason) {
        requireOpen();
        if (reason == null || reason.trim().length() < 5) {
            throw new IllegalStateException("traceability.errors.reason-required");
        }
        currentStatus = LogStatus.DELIVERY_REJECTED;
        rejectionReason = reason.trim();
    }

    private void requireOpen() {
        if (currentStatus == LogStatus.COMPLETED) {
            throw new IllegalStateException("traceability.errors.already-completed");
        }
    }

    public Long getDispatchOrderId() {
        return dispatchOrderId;
    }

    public LogStatus getCurrentStatus() {
        return currentStatus;
    }

    public Instant getEstimatedArrival() {
        return estimatedArrival;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public Long getId() {
        return id;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public double getCurrentLatitude() {
        return currentLatitude;
    }

    public double getCurrentLongitude() {
        return currentLongitude;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public String getOrderPriority() {
        return orderPriority;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static TraceabilityLog restore(Long id, Long dispatchOrderId, LogStatus currentStatus, Instant startTime, Instant endTime, Instant estimatedArrival, double currentLatitude, double currentLongitude, String rejectionReason, String orderPriority, String destinationName, String vehiclePlate, Long deviceId) {
        var a = new TraceabilityLog();
        a.id = id;
        a.dispatchOrderId = dispatchOrderId;
        a.currentStatus = currentStatus;
        a.startTime = startTime;
        a.endTime = endTime;
        a.estimatedArrival = estimatedArrival;
        a.currentLatitude = currentLatitude;
        a.currentLongitude = currentLongitude;
        a.rejectionReason = rejectionReason;
        a.orderPriority = orderPriority;
        a.destinationName = destinationName;
        a.vehiclePlate = vehiclePlate;
        a.deviceId = deviceId;
        return a;
    }
}
