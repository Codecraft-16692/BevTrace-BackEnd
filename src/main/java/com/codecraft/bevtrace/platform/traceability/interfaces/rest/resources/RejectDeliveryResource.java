package com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body that registers a rejected delivery.
 */
public record RejectDeliveryResource(@NotBlank @Size(min = 5) String reason) {
}
