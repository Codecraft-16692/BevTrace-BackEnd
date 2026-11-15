package com.codecraft.bevtrace.platform.iam.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.iam.domain.model.commands.AssignRoleCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.RevokeAccessCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.SignInCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.SignUpCommand;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.SignInResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.SignUpResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.GrantPermissionResource;

/**
 * Builds the commands of the IAM context from the request bodies.
 */
public final class UserCommandFromResourceAssembler {

    private UserCommandFromResourceAssembler() {
    }

    public static SignInCommand toCommandFromResource(SignInResource resource) {
        return new SignInCommand(resource.email(), resource.password());
    }

    public static SignUpCommand toCommandFromResource(SignUpResource resource) {
        return new SignUpCommand(resource.roleId(), resource.name(), resource.email(), resource.phone(), resource.password());
    }

    public static AssignRoleCommand toCommandFromResource(Long actorUserId, boolean actorAdmin, Long userId,
                                                          GrantPermissionResource resource) {
        return new AssignRoleCommand(actorUserId, actorAdmin, userId, resource.roleId());
    }

    public static RevokeAccessCommand toRevokeCommand(Long actorUserId, boolean actorAdmin, Long userId) {
        return new RevokeAccessCommand(actorUserId, actorAdmin, userId);
    }
}
