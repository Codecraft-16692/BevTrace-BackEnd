package com.codecraft.bevtrace.platform.iam.domain.services;

/**
 * Password rules, identical to the ones enforced by the sign-up form of the frontend.
 */
public final class PasswordPolicy {

    public static final int MIN_LENGTH = 9;

    private PasswordPolicy() {
    }

    /**
     * Validates a plain password.
     *
     * @param password candidate password
     * @throws IllegalStateException when it is shorter than {@link #MIN_LENGTH} or lacks an uppercase letter or a digit
     */
    public static void requireStrong(String password) {
        boolean ok = password != null && password.length() >= MIN_LENGTH
                && password.chars().anyMatch(Character::isUpperCase)
                && password.chars().anyMatch(Character::isDigit);
        if (!ok) {
            throw new IllegalStateException("iam.errors.password-weak");
        }
    }
}
