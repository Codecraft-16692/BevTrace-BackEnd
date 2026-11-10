package com.codecraft.bevtrace.platform.dispatch.domain.model.events;

import java.time.Instant;
import java.util.List;

/**
 * The truck left the warehouse. Inventory deducts the stock and traceability starts following the route.
 *
 * @param dispatchOrderId      order
 * @param priority             operational priority name
 * @param vehicleId            vehicle used
 * @param vehiclePlate         plate of the vehicle
 * @param destinationName      client name
 * @param destinationLatitude  destination latitude
 * @param destinationLongitude destination longitude
 * @param departedAt           departure time
 * @param items                batches and units on board
 */
public record DispatchDeparted(Long dispatchOrderId, String priority, Long vehicleId, String vehiclePlate,
                               String destinationName, double destinationLatitude, double destinationLongitude,
                               Instant departedAt, List<Item> items) {

    /**
     * Units of a batch on board.
     *
     * @param batchId  batch
     * @param quantity units
     */
    public record Item(Long batchId, int quantity) {
    }
}
