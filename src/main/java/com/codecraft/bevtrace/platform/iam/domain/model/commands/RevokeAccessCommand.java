package com.codecraft.bevtrace.platform.iam.domain.model.commands;

/**
 * Revokes the access of a user: the account is disabled and cannot sign in until a permission is granted again.
 *
 * @param actorUserId user that performs the change (cannot be the target)
 * @param actorAdmin  true when the actor is an administrator; any other actor (the logistics manager)
 *                    can only manage warehouse operators
 * @param userId      target user
 */
public record RevokeAccessCommand(Long actorUserId, boolean actorAdmin, Long userId) {
}
