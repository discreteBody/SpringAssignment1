package com.example.assignment1.exception;

// ======================== DO NOT MODIFY THIS FILE ========================

/**
 * Thrown when attempting to create a resource that already exists.
 * For example, trying to create a second BREAKFAST menu for the same restaurant on the same date.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }
}
