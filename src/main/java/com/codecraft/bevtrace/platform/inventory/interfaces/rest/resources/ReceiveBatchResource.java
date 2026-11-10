package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Request body that receives a batch by its scanned code.
 */
public record ReceiveBatchResource(@NotBlank @Pattern(regexp = "^[A-Za-z0-9-]{4,30}$") String scanCode) {
}
