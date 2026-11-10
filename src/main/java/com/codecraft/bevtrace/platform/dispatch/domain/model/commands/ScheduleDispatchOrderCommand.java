package com.codecraft.bevtrace.platform.dispatch.domain.model.commands;

import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchPriority;

import java.time.LocalDate;
import java.util.List;

/**
 * Schedules a dispatch of one or more batches.
 *
 * @param managerId     manager that schedules it
 * @param destinationId client destination
 * @param scheduledDate day of the delivery
 * @param priority      priority of the dispatch
 * @param items         batches and units to ship
 */
public record ScheduleDispatchOrderCommand(Long managerId, Long destinationId, LocalDate scheduledDate,
                                           DispatchPriority priority, List<Item> items) {

    /**
     * Units of one batch.
     *
     * @param batchId  batch
     * @param quantity units
     */
    public record Item(Long batchId, int quantity) {
    }
}
