package com.codecraft.bevtrace.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Request body that grants a role (and with it the access to its sections) to a user.
 */
public record GrantPermissionResource(@NotNull Long roleId) {
}
