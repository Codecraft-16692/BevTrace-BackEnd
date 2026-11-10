package com.codecraft.bevtrace.platform.inventory.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

/**
 * Request body that resolves a discrepancy.
 */
public record ResolveDiscrepancyResource(@NotBlank String note) {
}
