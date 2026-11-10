package com.codecraft.bevtrace.platform.incident.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request body that registers a corrective action.
 */
public record RegisterCorrectiveActionResource(@NotBlank @Size(min = 10) String description) {
}
