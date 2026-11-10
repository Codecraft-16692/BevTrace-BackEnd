package com.codecraft.bevtrace.platform.iam.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.iam.domain.model.aggregates.User;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.UserResource;

/**
 * Builds the resource of a user.
 */
public final class UserResourceFromEntityAssembler {

    private UserResourceFromEntityAssembler() {
    }

    public static UserResource toResourceFromEntity(User e) {
        return new UserResource(e.getId(), e.getRoleId(), e.getName(), e.getEmail(), e.getPhone(), e.isActive(), e.getCreatedAt());
    }
}
