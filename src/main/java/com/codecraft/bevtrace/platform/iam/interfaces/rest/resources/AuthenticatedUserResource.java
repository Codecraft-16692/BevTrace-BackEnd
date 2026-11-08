package com.codecraft.bevtrace.platform.iam.interfaces.rest.resources;

import java.util.Set;

/**
 * Session returned by the sign-in: the user, its roles and the bearer token.
 */
public record AuthenticatedUserResource(Long id, String name, String email, Set<String> roles, String token) {
}
