package com.codecraft.bevtrace.platform.dispatch.domain.model.aggregates;

import com.codecraft.bevtrace.platform.dispatch.domain.services.CargoPolicy;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchPriority;
import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchStatus;

import com.codecraft.bevtrace.platform.dispatch.domain.model.events.DispatchAuthorized;
import com.codecraft.bevtrace.platform.dispatch.domain.model.events.DispatchDeparted;

import com.codecraft.bevtrace.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Aggregate root of the dispatch context. It owns every transition rule of the order, so the
 * application service only orchestrates loading and saving.
 */
public class DispatchOrder extends AbstractDomainAggregateRoot<DispatchOrder> {

    private Long id;
    private Long managerId;
    private Long vehicleId;
    private Long destinationId;
    private LocalDate scheduledDate;
    private DispatchStatus status;
    private DispatchPriority priority;
    private double estimatedWeight;
    private int requestedQuantity;
    private Integer deliveredQuantity;
    private Instant deliveredAt;
    private Instant departedAt;
    private boolean cargoValidated;
    private Instant createdAt;

    protected DispatchOrder() {
    }

    /**
     * Schedules a new dispatch.
     *
     * @param managerId     logistics manager that creates it
     * @param destinationId destination
     * @param scheduledDate delivery day, not in the past
     * @param priority      operational priority
     * @param totalQuantity units across all cargo lines, positive
     * @return a scheduled order without vehicle
     */
    public static DispatchOrder schedule(Long managerId, Long destinationId, LocalDate scheduledDate,
                                         DispatchPriority priority, int totalQuantity) {
        if (scheduledDate.isBefore(LocalDate.now())) {
            throw new IllegalStateException("dispatch.errors.past-date");
        }
        if (totalQuantity <= 0) {
            throw new IllegalStateException("dispatch.errors.no-items");
        }
        var order = new DispatchOrder();
        order.managerId = managerId;
        order.destinationId = destinationId;
        order.scheduledDate = scheduledDate;
        order.status = DispatchStatus.SCHEDULED;
        order.priority = priority;
        order.requestedQuantity = totalQuantity;
        order.estimatedWeight = CargoPolicy.weightOf(totalQuantity);
        order.cargoValidated = false;
        order.createdAt = Instant.now();
        return order;
    }

    /**
     * Assigns a vehicle, which authorizes the dispatch and reserves the vehicle.
     *
     * @param vehicle         candidate vehicle
     * @param driver          driver of the vehicle
     * @param destinationName name used in the notification
     */
    public void assignVehicle(TransportVehicle vehicle, Driver driver, String destinationName) {
        requireStatus(DispatchStatus.SCHEDULED, "dispatch.errors.not-scheduled");
        vehicle.reserve();
        if (driver == null || !driver.isActive()) {
            throw new IllegalStateException("dispatch.errors.driver-unavailable");
        }
        if (estimatedWeight > vehicle.getMaxCapacity()) {
            throw new IllegalStateException("dispatch.errors.capacity-exceeded");
        }
        vehicleId = vehicle.getId();
        status = DispatchStatus.AUTHORIZED;
        registerDomainEvent(new DispatchAuthorized(getId(), destinationName));
    }

    /**
     * Validates the scanned pallet labels against the pallets expected by the cargo.
     *
     * @param expected labels expected
     * @param scanned  labels scanned at the dock
     */
    public void validateCargo(Set<String> expected, Collection<String> scanned) {
        requireStatus(DispatchStatus.AUTHORIZED, "dispatch.errors.not-authorized");
        Set<String> seen = scanned.stream().map(code -> code.trim().toUpperCase()).filter(code -> !code.isEmpty())
                .collect(Collectors.toCollection(LinkedHashSet::new));
        var unexpected = seen.stream().filter(code -> !expected.contains(code)).toList();
        var missing = expected.stream().filter(code -> !seen.contains(code)).toList();
        if (!unexpected.isEmpty() || !missing.isEmpty()) {
            throw new IllegalStateException("dispatch.errors.pallet-mismatch");
        }
        cargoValidated = true;
    }

    /**
     * Changes the priority of a dispatch that has not left yet.
     *
     * @param newPriority new priority
     */
    public void changePriority(DispatchPriority newPriority) {
        requireEditable();
        priority = newPriority;
    }

    /**
     * Registers the departure of the truck.
     *
     * @param cargo       cargo lines on board
     * @param destination destination of the route
     * @param vehicle     vehicle used
     */
    public void depart(List<CargoAssignment> cargo, DeliveryDestination destination, TransportVehicle vehicle) {
        if (status != DispatchStatus.AUTHORIZED || vehicleId == null) {
            throw new IllegalStateException("dispatch.errors.not-authorized");
        }
        if (!cargoValidated) {
            throw new IllegalStateException("dispatch.errors.cargo-not-validated");
        }
        status = DispatchStatus.IN_TRANSIT;
        departedAt = Instant.now();
        registerDomainEvent(new DispatchDeparted(getId(), priority.name(), vehicle.getId(), vehicle.getPlateNumber(),
                destination.getClientName(), destination.getLatitude(), destination.getLongitude(), departedAt,
                cargo.stream().map(line -> new DispatchDeparted.Item(line.getBatchId(), line.getQuantity())).toList()));
    }

    /**
     * Cancels a dispatch that has not left yet and frees its vehicle.
     *
     * @param vehicle assigned vehicle, or null when none was assigned
     */
    public void cancel(TransportVehicle vehicle) {
        requireEditable();
        if (vehicle != null) {
            vehicle.release();
        }
        status = DispatchStatus.CANCELLED;
    }

    /**
     * Closes the dispatch after the client received the cargo.
     *
     * @param at      delivery time
     * @param vehicle vehicle to free, may be null
     */
    public void complete(Instant at, TransportVehicle vehicle) {
        if (status != DispatchStatus.IN_TRANSIT) {
            return;
        }
        status = DispatchStatus.DELIVERED;
        deliveredAt = at;
        deliveredQuantity = requestedQuantity;
        if (vehicle != null) {
            vehicle.release();
        }
    }

    private void requireEditable() {
        if (status != DispatchStatus.SCHEDULED && status != DispatchStatus.AUTHORIZED) {
            throw new IllegalStateException("dispatch.errors.not-editable");
        }
    }

    private void requireStatus(DispatchStatus expected, String code) {
        if (status != expected) {
            throw new IllegalStateException(code);
        }
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public Long getDestinationId() {
        return destinationId;
    }

    public DispatchStatus getStatus() {
        return status;
    }

    public LocalDate getScheduledDate() {
        return scheduledDate;
    }

    public int getRequestedQuantity() {
        return requestedQuantity;
    }

    public Integer getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public Instant getDeliveredAt() {
        return deliveredAt;
    }

    public Long getId() {
        return id;
    }

    public Long getManagerId() {
        return managerId;
    }

    public DispatchPriority getPriority() {
        return priority;
    }

    public double getEstimatedWeight() {
        return estimatedWeight;
    }

    public Instant getDepartedAt() {
        return departedAt;
    }

    public boolean getCargoValidated() {
        return cargoValidated;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    /**
     * Rebuilds the aggregate from its stored state. Used only by the persistence adapter.
     *
     * @return the aggregate, without domain events
     */
    public static DispatchOrder restore(Long id, Long managerId, Long vehicleId, Long destinationId, LocalDate scheduledDate, DispatchStatus status, DispatchPriority priority, double estimatedWeight, int requestedQuantity, Integer deliveredQuantity, Instant deliveredAt, Instant departedAt, boolean cargoValidated, Instant createdAt) {
        var a = new DispatchOrder();
        a.id = id;
        a.managerId = managerId;
        a.vehicleId = vehicleId;
        a.destinationId = destinationId;
        a.scheduledDate = scheduledDate;
        a.status = status;
        a.priority = priority;
        a.estimatedWeight = estimatedWeight;
        a.requestedQuantity = requestedQuantity;
        a.deliveredQuantity = deliveredQuantity;
        a.deliveredAt = deliveredAt;
        a.departedAt = departedAt;
        a.cargoValidated = cargoValidated;
        a.createdAt = createdAt;
        return a;
    }
}
