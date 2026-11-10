package com.codecraft.bevtrace.platform.subscription.domain.services;

/**
 * Card number rules of the checkout. There is no real payment gateway: this module simulates the
 * charge so the subscription flow can be demonstrated end to end.
 */
public final class CardPolicy {

    /** Test card that the simulated gateway always declines. */
    public static final String DECLINED_TEST_CARD = "4000000000000002";

    private CardPolicy() {
    }

    /**
     * @param value card number as typed
     * @return digits only
     */
    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "");
    }

    /**
     * @param value card number as typed
     * @return true for 16 digits that satisfy the Luhn checksum
     */
    public static boolean isValid(String value) {
        var digits = normalize(value);
        if (!digits.matches("[0-9]{16}")) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < digits.length(); i++) {
            int digit = digits.charAt(digits.length() - 1 - i) - '0';
            if (i % 2 == 1) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }
        return sum % 10 == 0;
    }

    /**
     * @param value card number as typed
     * @return the last four digits
     */
    public static String last4(String value) {
        var digits = normalize(value);
        return digits.substring(Math.max(0, digits.length() - 4));
    }

    /**
     * @param value card number as typed
     * @return true when the simulated gateway declines the card
     */
    public static boolean isDeclined(String value) {
        return DECLINED_TEST_CARD.equals(normalize(value));
    }
}
