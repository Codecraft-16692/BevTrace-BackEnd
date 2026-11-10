package com.codecraft.bevtrace.platform.iam.interfaces.rest.transform;

import com.codecraft.bevtrace.platform.iam.application.commandservices.UserCommandService.AuthenticatedUser;
import com.codecraft.bevtrace.platform.iam.interfaces.rest.resources.AuthenticatedUserResource;

import java.util.Set;

/**
 * Builds the session resource returned by the sign-in.
 */
public final class AuthenticatedUserResourceFromEntityAssembler {

    private AuthenticatedUserResourceFromEntityAssembler() {
    }

    public static AuthenticatedUserResource toResourceFromEntity(AuthenticatedUser session) {
        var user = session.user();
        return new AuthenticatedUserResource(user.getId(), user.getName(), user.getEmail(),
                Set.of(session.role().getName()), session.token());
    }
}
