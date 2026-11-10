package com.codecraft.bevtrace.platform.dispatch.interfaces.rest.resources;

import com.codecraft.bevtrace.platform.dispatch.domain.model.valueobjects.DispatchPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Request body: AssignVehicle.
 */
public record AssignVehicleResource(@NotNull Long vehicleId) {
}
