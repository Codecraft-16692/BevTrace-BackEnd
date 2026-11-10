package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Request body that reports a waste.
 */
public record ReportWasteResource(@NotNull Long batchId, @Min(1) int quantity, @NotBlank String reason) {
}
