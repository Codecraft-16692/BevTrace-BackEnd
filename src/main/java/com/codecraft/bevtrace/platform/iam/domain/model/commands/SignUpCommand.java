package com.codecraft.bevtrace.platform.iam.domain.model.commands;

/**
 * Registers a new account.
 *
 * @param roleId   requested role (administrators cannot self-register)
 * @param name     full name
 * @param email    e-mail, unique
 * @param phone    contact phone
 * @param password plain password (9+ characters, one uppercase, one digit)
 */
public record SignUpCommand(Long roleId, String name, String email, String phone, String password) {
}
