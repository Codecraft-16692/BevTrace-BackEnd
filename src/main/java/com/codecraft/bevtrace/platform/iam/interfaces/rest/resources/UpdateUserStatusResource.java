package com.codecraft.bevtrace.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Request body that enables or disables a user.
 */
public record UpdateUserStatusResource(@NotNull Boolean active) {
}
