package com.hana8.hanaro.common.util;

public final class AccountNumberFormatter {

    private AccountNumberFormatter() {
    }

    public static String normalize(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\D", "");
    }

    public static String format(String normalized) {
        if (normalized == null) {
            return null;
        }
        if (normalized.length() != 11) {
            return normalized;
        }
        return normalized.substring(0, 3) + "-" + normalized.substring(3, 7) + "-" + normalized.substring(7);
    }
}
