package ru.utmn.dyagunov.tax_subsidies.utils;

public class StringUtil {
    public static String convertToSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c) && !result.isEmpty()) {
                result.append('_');
            }

            result.append(Character.toLowerCase(c));
        }

        return result.toString();
    }
}