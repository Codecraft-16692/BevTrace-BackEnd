package com.codecraft.bevtrace.platform.subscription.domain.model.valueobjects;

import java.util.regex.Pattern;

/**
 * E-mail format rule of the subscription context (newsletter and contact form).
 */
public final class EmailAddress {

    private static final Pattern FORMAT = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$");

    private EmailAddress() {
    }

    /**
     * @param email candidate
     * @return true when it looks like an e-mail address
     */
    public static boolean isValid(String email) {
        return email != null && FORMAT.matcher(email.trim()).matches();
    }
}
