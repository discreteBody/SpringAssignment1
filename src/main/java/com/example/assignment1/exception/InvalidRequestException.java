package com.example.assignment1.exception;

// ======================== DO NOT MODIFY THIS FILE ========================

/**
 * Thrown when a request contains invalid data.
 * For example, empty restaurant name, negative price, null fields, etc.
 */
public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }
}
