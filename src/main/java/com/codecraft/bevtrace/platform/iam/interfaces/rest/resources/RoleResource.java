package com.codecraft.bevtrace.platform.iam.interfaces.rest.resources;

/**
 * JSON representation of a role (field names are the ones the Angular frontend consumes).
 */
public record RoleResource(Long id, String name, boolean isAdmin) {
}
