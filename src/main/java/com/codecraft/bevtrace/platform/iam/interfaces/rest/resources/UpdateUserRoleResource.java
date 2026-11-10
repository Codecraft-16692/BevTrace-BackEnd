package com.codecraft.bevtrace.platform.iam.interfaces.rest.resources;

import jakarta.validation.constraints.NotNull;

/**
 * Request body that moves a user to another role.
 */
public record UpdateUserRoleResource(@NotNull Long roleId) {
}
