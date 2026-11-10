package com.codecraft.bevtrace.platform.iam.domain.model.commands;

/**
 * Enables or disables an account.
 *
 * @param actorUserId user that performs the change (cannot be the target)
 * @param userId      target user
 * @param active      new state
 */
public record ChangeUserStatusCommand(Long actorUserId, Long userId, boolean active) {
}
