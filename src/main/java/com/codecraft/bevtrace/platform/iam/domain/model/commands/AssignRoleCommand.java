package com.codecraft.bevtrace.platform.iam.domain.model.commands;

/**
 * Moves a user to another role.
 *
 * @param actorUserId user that performs the change (cannot be the target)
 * @param userId      target user
 * @param roleId      new role
 */
public record AssignRoleCommand(Long actorUserId, Long userId, Long roleId) {
}
