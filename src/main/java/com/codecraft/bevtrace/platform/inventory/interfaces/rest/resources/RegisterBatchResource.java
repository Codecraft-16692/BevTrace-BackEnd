package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

/**
 * Request body that announces a batch.
 */
public record RegisterBatchResource(@NotNull Long productId, @NotNull Long zoneId,
                                    @NotBlank @Pattern(regexp = "^[A-Za-z0-9-]{4,30}$") String batchNumber,
                                    @Min(1) int initialQty, @NotNull LocalDate expirationDate) {
}
