package com.codecraft.bevtrace.platform.iam.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.iam.domain.model.entities.Role;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.RoleResource;

/**
 * Builds the resource of a role.
 */
public final class RoleResourceFromEntityAssembler {

    private RoleResourceFromEntityAssembler() {
    }

    public static RoleResource toResourceFromEntity(Role e) {
        return new RoleResource(e.getId(), e.getName(), e.isAdmin());
    }
}
