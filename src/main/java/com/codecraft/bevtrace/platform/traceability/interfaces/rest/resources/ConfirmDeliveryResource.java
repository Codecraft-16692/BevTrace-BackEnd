package com.codecraft.bevtrace.platform.traceability.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body that confirms a delivery.
 */
public record ConfirmDeliveryResource(@NotBlank @Size(min = 3) String receivedBy) {
}
