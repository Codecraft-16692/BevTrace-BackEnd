package com.codecraft.bevtrace.platform.analytics.domain.model.valueobjects;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

/**
 * The operational facts the indicators are computed from, already translated into the analytics language.
 *
 * @param orders          dispatch orders
 * @param batches         stock batches
 * @param waste           waste records
 * @param reconciliations inventory counts
 */
public record OperationalData(List<Order> orders, List<Batch> batches, List<Waste> waste,
                              List<Reconciliation> reconciliations) {

    /**
     * @param delivered          whether it was delivered
     * @param scheduledDate      promised day
     * @param deliveredAt        delivery time
     * @param requestedQuantity  requested units
     * @param deliveredQuantity  delivered units (null when not delivered)
     */
    public record Order(boolean delivered, LocalDate scheduledDate, Instant deliveredAt, int requestedQuantity,
                        Integer deliveredQuantity) {
    }

    /**
     * @param available  whether it can be dispatched
     * @param currentQty units on hand
     * @param initialQty units received
     * @param receivedAt reception time (null while expected)
     */
    public record Batch(boolean available, int currentQty, int initialQty, Instant receivedAt) {
    }

    /**
     * @param reportedDate day of the waste
     * @param quantity     lost units
     */
    public record Waste(LocalDate reportedDate, int quantity) {
    }

    /**
     * @param date          day of the count
     * @param reconciled    whether it was confirmed
     * @param eriPercentage accuracy of the count
     */
    public record Reconciliation(LocalDate date, boolean reconciled, double eriPercentage) {
    }
}
