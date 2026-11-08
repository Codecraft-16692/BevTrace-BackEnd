package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Request body: ScheduleDispatchOrder.
 */
public record ScheduleDispatchOrderResource(@NotNull Long destinationId, @NotNull LocalDate scheduledDate, @NotNull DispatchPriority priority, @NotEmpty List<@Valid DispatchItemResource> items) {
}
