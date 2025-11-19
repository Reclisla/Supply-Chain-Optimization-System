package com.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ApplicationUtil {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static DecimalFormat priceFormat = new DecimalFormat("#,##0.00");

    // Formats a date to a readable string.
    // Example: 2025-11-06 14:23:45
     
    public static String formatDate(Date date) {
        if (date == null)
            return "N/A";
        return dateFormat.format(date);
    }

    // Formats a double value as currency-like price.
    // Example: 1299.5 -> "1,299.50"
    
    public static String formatPrice(double price) {
        return priceFormat.format(price);
    }

    // Checks if a given string is null or empty.
     
    public static boolean isEmpty(String input) {
        return (input == null || input.trim().isEmpty());
    }

    // Validates if a given string looks like an email address.
     
    public static boolean isValidEmail(String email) {
        if (isEmpty(email))
            return false;
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    // Generates a simple timestamp string for logging or debugging.
     
    public static String now() {
        return dateFormat.format(new Date());
    }

    // Converts null values to a safe default string.
     
    public static String safeString(String input) {
        return (input == null) ? "" : input.trim();
    }

    // Validates a numeric string and converts it to integer.
    // Returns -1 if invalid.
     
    public static int parseIntSafe(String input) {
        try {
            return Integer.parseInt(input.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    // Validates a numeric string and converts it to double.
    // Returns -1.0 if invalid.
     
    public static double parseDoubleSafe(String input) {
        try {
            return Double.parseDouble(input.trim());
        } catch (Exception e) {
            return -1.0;
        }
    }
}
