package com.codecraft.bevtrace.platform.iam.domain.model.commands;

/**
 * Opens a session.
 *
 * @param email    e-mail of the account
 * @param password plain password
 */
public record SignInCommand(String email, String password) {
}
