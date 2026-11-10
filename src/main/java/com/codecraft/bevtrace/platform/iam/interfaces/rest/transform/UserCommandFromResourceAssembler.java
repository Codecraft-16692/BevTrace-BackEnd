package com.codecraft.bevtrace.platform.iam.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.iam.domain.model.commands.AssignRoleCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.ChangeUserStatusCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.SignInCommand;
import com.codecraft.bevtrace.platform.iam.domain.model.commands.SignUpCommand;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.SignInResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.SignUpResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.UpdateUserRoleResource;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.UpdateUserStatusResource;

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

    public static AssignRoleCommand toCommandFromResource(Long actorUserId, Long userId, UpdateUserRoleResource resource) {
        return new AssignRoleCommand(actorUserId, userId, resource.roleId());
    }

    public static ChangeUserStatusCommand toCommandFromResource(Long actorUserId, Long userId, UpdateUserStatusResource resource) {
        return new ChangeUserStatusCommand(actorUserId, userId, resource.active());
    }
}
