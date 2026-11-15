package com.codecraft.bevtrace.platform.iam.domain.model.commands;

/**
 * Grants a role to a user and enables the account (approves the sign-up).
 *
 * @param actorUserId user that performs the change (cannot be the target)
 * @param actorAdmin  true when the actor is an administrator; any other actor (the logistics manager)
 *                    can only manage warehouse operators
 * @param userId      target user
 * @param roleId      role to grant
 */
public record AssignRoleCommand(Long actorUserId, boolean actorAdmin, Long userId, Long roleId) {
}
