package com.exception;

public class SupplyChainException extends Exception {

    // Default constructor
    public SupplyChainException() {
        super("An unknown supply chain error occurred.");
    }

    // Message-only constructor
    public SupplyChainException(String message) {
        super(message);
    }

    // Message and cause constructor
    public SupplyChainException(String message, Throwable cause) {
        super(message, cause);
    }

    // Cause-only constructor
    public SupplyChainException(Throwable cause) {
        super(cause);
    }

    public static SupplyChainException invalidId(String entity, String id) {
        return new SupplyChainException("Invalid " + entity + " ID: " + id);
    }

    public static SupplyChainException notFound(String entity, String id) {
        return new SupplyChainException(entity + " with ID '" + id + "' not found.");
    }

    public static SupplyChainException validation(String message) {
        return new SupplyChainException("Validation error: " + message);
    }

    public static SupplyChainException insufficientStock(String productId) {
        return new SupplyChainException("Insufficient stock for product ID: " + productId);
    }

    public static SupplyChainException emailFormat(String email) {
        return new SupplyChainException("Invalid email format: " + email);
    }
}
